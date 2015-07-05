package com.vampir2236.workers.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.vampir2236.workers.R;
import com.vampir2236.workers.sync.SyncFragment;
import com.vampir2236.workers.sync.SyncModel;

import java.util.Date;

public class SplashActivity extends ActionBarActivity implements SyncModel.SyncObserver {

    private final int MIN_SHOWING_TIME = 1000;
    private final String SYNC_FRAGMENT = "SYNC_FRAGMENT";
    private SyncModel mSyncModel;
    private Date dateStartSync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();

        SyncFragment syncFragment =
                (SyncFragment) getFragmentManager().findFragmentByTag(SYNC_FRAGMENT);

        if (syncFragment == null) {
            syncFragment = new SyncFragment();
            getFragmentManager().beginTransaction()
                    .add(syncFragment, SYNC_FRAGMENT)
                    .commit();
        }

        mSyncModel = syncFragment.getSyncModel();
        mSyncModel.registerSyncObserver(this);
        mSyncModel.sync();
    }

    /**
     * При закрытии активности отписываемся от сообщений
     * и останавливаем поток синхронизации данных
     */
    @Override
    protected void onDestroy() {
        mSyncModel.unregisterSyncObserver(this);

        if (isFinishing()) {
            mSyncModel.stopSync();
        }
        super.onDestroy();
    }

    /**
     * События модели синхронизации данных
     */
    @Override
    public void onSyncStarted(SyncModel syncModel) {
        dateStartSync = new Date();
    }

    @Override
    public void onSyncSucceeded(SyncModel syncModel) {
        waitMinTimeBeforeCloseActivity();
    }

    @Override
    public void onSyncFailed(SyncModel syncModel) {
        waitMinTimeBeforeCloseActivity();
        Toast.makeText(this, getString(R.string.sync_error), Toast.LENGTH_LONG).show();
    }

    /**
     * Ожидание истечения минимального времени
     * перед переходом на главную активность
     */
    private void waitMinTimeBeforeCloseActivity() {
        final long diff = MIN_SHOWING_TIME - (new Date().getTime() - dateStartSync.getTime());

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                gotoStartActivity();
            }
        }, diff);
    }

    public void gotoStartActivity() {
        finish();
        startActivity(new Intent(this, SpecialtiesActivity.class));
    }
}
