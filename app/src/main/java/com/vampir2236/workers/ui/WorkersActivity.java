package com.vampir2236.workers.ui;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.vampir2236.workers.R;
import com.vampir2236.workers.adapters.WorkerAdapter;
import com.vampir2236.workers.models.Worker;
import com.vampir2236.workers.sync.SyncFragment;
import com.vampir2236.workers.sync.SyncModel;

import java.util.List;

public class WorkersActivity extends ActionBarActivity
        implements AdapterView.OnItemClickListener {

    private ListView mWorkersList;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workers);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mWorkersList = (ListView) findViewById(R.id.workersList);
        mWorkersList.setOnItemClickListener(this);

        setTitle(getIntent().getStringExtra("specialty"));
        showWorkers(getIntent().getLongExtra("specialtyId", -1));
    }


    private void showWorkers(long specialtyId) {
        List<Worker> workers = Worker.getWorkers(specialtyId);
        WorkerAdapter workerAdapter = new WorkerAdapter(this, workers);
        mWorkersList.setAdapter(workerAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, WorkerDetailActivity.class);
        WorkerAdapter workerAdapter = (WorkerAdapter) parent.getAdapter();
        Worker worker = (Worker) workerAdapter.getItem(position);

        intent.putExtra("workerId", id);
        intent.putExtra("avatarUrl", worker.avatarUrl);
        intent.putExtra("firstName", worker.firstName);
        intent.putExtra("lastName", worker.lastName);
        intent.putExtra("birthday", worker.birthday);
        intent.putExtra("age", worker.getAge());

        startActivity(intent);
    }
}
