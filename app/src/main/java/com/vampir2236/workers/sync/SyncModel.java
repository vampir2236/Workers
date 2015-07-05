package com.vampir2236.workers.sync;

import android.database.Observable;
import android.os.AsyncTask;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.vampir2236.workers.models.JsonWorkers;
import com.vampir2236.workers.models.Specialty;
import com.vampir2236.workers.models.Worker;
import com.vampir2236.workers.models.WorkerSpecialty;

import java.io.IOException;

/**
 * Модель синхронизации данных
 */
public class SyncModel {

    private final SyncObservable mSyncObservable = new SyncObservable();
    private SyncTask mSyncTask;
    private boolean mIsSync;
    private final String URL = "http://65apps.com/images/testTask.json";


    public void sync() {
        if (mIsSync) {
            return;
        }

        mSyncObservable.notifySyncStarted();

        mIsSync = true;
        mSyncTask = new SyncTask();
        mSyncTask.execute(URL);
    }

    public void stopSync() {
        if (mIsSync) {
            mSyncTask.cancel(true);
            mIsSync = false;
        }
    }

    public void registerSyncObserver(final SyncObserver syncObserver) {
        mSyncObservable.registerObserver(syncObserver);
        if (mIsSync) {
            syncObserver.onSyncStarted(this);
        }
    }

    public void unregisterSyncObserver(final SyncObserver syncObserver) {
        mSyncObservable.unregisterObserver(syncObserver);
    }


    /**
     * загрузка json, парсинг и сохранение в БД
     */
    private class SyncTask extends AsyncTask<String, Void, Boolean> {

        private final String TAG = getClass().getSimpleName();

        private String getJson(String url) throws IOException {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();
        }

        private void fillDatabase(JsonWorkers jsonWorkers) {
            ActiveAndroid.beginTransaction();
            try {
                new Delete().from(WorkerSpecialty.class).execute();
                new Delete().from(Specialty.class).execute();
                new Delete().from(Worker.class).execute();

                for (JsonWorkers.Worker jsonWorker : jsonWorkers.getWorkers()) {
                    Worker worker = new Worker(
                            jsonWorker.getFirstName(),
                            jsonWorker.getLastName(),
                            jsonWorker.getBirthday(),
                            jsonWorker.getAvatarUrl());
                    worker.save();

                    for (JsonWorkers.Specialty jsonSpecialty : jsonWorker.getSpecialties()) {
                        Specialty specialty = new Select()
                                .from(Specialty.class)
                                .where("specialty_id  = ?", jsonSpecialty.getSpecialtyId())
                                .executeSingle();
                        if (specialty == null) {
                            specialty = new Specialty(
                                    jsonSpecialty.getSpecialtyId(),
                                    jsonSpecialty.getName());
                            specialty.save();
                        }

                        WorkerSpecialty workerSpecialty = new WorkerSpecialty(worker, specialty);
                        workerSpecialty.save();
                    }
                }
                ActiveAndroid.setTransactionSuccessful();
            } finally {
                ActiveAndroid.endTransaction();
            }
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                String json = getJson(params[0]);
                JsonWorkers jsonWorkers = new Gson().fromJson(json, JsonWorkers.class);
                fillDatabase(jsonWorkers);

                return true;
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            mIsSync = false;

            if (success) {
                mSyncObservable.notifySyncSuccess();
            } else {
                mSyncObservable.notifySyncFailed();
            }
        }
    }


    public interface SyncObserver {
        void onSyncStarted(SyncModel syncModel);

        void onSyncSucceeded(SyncModel syncModel);

        void onSyncFailed(SyncModel syncModel);
    }

    public class SyncObservable extends Observable<SyncObserver> {
        public void notifySyncStarted() {
            for (SyncObserver observer : mObservers) {
                observer.onSyncStarted(SyncModel.this);
            }
        }

        public void notifySyncSuccess() {
            for (SyncObserver observer : mObservers) {
                observer.onSyncSucceeded(SyncModel.this);
            }
        }

        public void notifySyncFailed() {
            for (SyncObserver observer : mObservers) {
                observer.onSyncFailed(SyncModel.this);
            }
        }
    }
}
