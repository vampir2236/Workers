package com.vampir2236.workers.ui;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.vampir2236.workers.R;
import com.vampir2236.workers.adapters.SpecialtyAdapter;
import com.vampir2236.workers.models.Specialty;
import com.vampir2236.workers.sync.SyncFragment;
import com.vampir2236.workers.sync.SyncModel;

import java.util.List;

public class SpecialtiesActivity extends ActionBarActivity
        implements SyncModel.SyncObserver, SwipeRefreshLayout.OnRefreshListener,
        AdapterView.OnItemClickListener {

    private final String SYNC_FRAGMENT = "SYNC_FRAGMENT";
    private SyncModel mSyncModel;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView mSpecialtiesList;
    private boolean mIsRefreshing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specialties);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mSpecialtiesList = (ListView) findViewById(R.id.specialtiesList);
        mSpecialtiesList.setOnItemClickListener(this);
        showSpecialties();

        SyncFragment syncFragment =
                (SyncFragment) getFragmentManager().findFragmentByTag(SYNC_FRAGMENT);

        if (syncFragment == null) {
            syncFragment = new SyncFragment();
            getFragmentManager().beginTransaction()
                    .add(syncFragment, SYNC_FRAGMENT)
                    .commit();
        }

        mSyncModel = syncFragment.getSyncModel();
    }

    @Override
    protected void onDestroy() {
        if (mIsRefreshing) {
            mSyncModel.unregisterSyncObserver(this);
        }
        if (isFinishing()) {
            mSyncModel.stopSync();
        }
        super.onDestroy();
    }

    private void showSpecialties() {
        List<Specialty> specialties = Specialty.all();
        SpecialtyAdapter specialtyAdapter = new SpecialtyAdapter(this, specialties);
        mSpecialtiesList.setAdapter(specialtyAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, WorkersActivity.class);

        SpecialtyAdapter specialtyAdapter = (SpecialtyAdapter) parent.getAdapter();
        Specialty specialty = (Specialty) specialtyAdapter.getItem(position);

        intent.putExtra("specialtyId", id);
        intent.putExtra("specialty", specialty.name);

        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        mSyncModel.registerSyncObserver(this);
        mSyncModel.sync();
    }

    public void finishRefresh() {
        mSyncModel.unregisterSyncObserver(this);
        mIsRefreshing = false;
        mSwipeRefreshLayout.setRefreshing(mIsRefreshing);
        showSpecialties();
    }

    /**
     * События модели синхронизации данных
     */
    @Override
    public void onSyncStarted(SyncModel syncModel) {
        mIsRefreshing = true;
        mSwipeRefreshLayout.setRefreshing(mIsRefreshing);
    }

    @Override
    public void onSyncSucceeded(SyncModel syncModel) {
        finishRefresh();
    }

    @Override
    public void onSyncFailed(SyncModel syncModel) {
        finishRefresh();
        Toast.makeText(this, getResources().getString(R.string.sync_error),
                Toast.LENGTH_LONG).show();
    }
}
