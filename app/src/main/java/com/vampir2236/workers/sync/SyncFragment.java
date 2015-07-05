package com.vampir2236.workers.sync;

import android.app.Fragment;
import android.os.Bundle;

/**
 * Фрагмент используется для хранения модели синхронизации данных
 */
public class SyncFragment extends Fragment {

    private final SyncModel mSyncModel = new SyncModel();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    public SyncModel getSyncModel() {
        return mSyncModel;
    }
}
