package com.vampir2236.workers.ui;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vampir2236.workers.R;
import com.vampir2236.workers.adapters.SpecialtyAdapter;
import com.vampir2236.workers.models.Specialty;
import com.vampir2236.workers.picasso.CircleTransformation;
import com.vampir2236.workers.picasso.PicassoUtils;

import java.util.List;

public class WorkerDetailActivity extends ActionBarActivity {

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
        setContentView(R.layout.activity_worker_detail);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadData();
    }

    private void loadData() {
        Intent intent = getIntent();

        ImageView avatar = (ImageView) findViewById(R.id.avatar);
        String avatarUrl = intent.getStringExtra("avatarUrl");
        PicassoUtils.loadImage(this, avatarUrl, R.drawable.guest, avatar);

        TextView firstname = (TextView) findViewById(R.id.firstname);
        firstname.setText(intent.getStringExtra("firstName"));

        TextView lastname = (TextView) findViewById(R.id.lastname);
        lastname.setText(intent.getStringExtra("lastName"));

        TextView birthday = (TextView) findViewById(R.id.birthday);
        String workerBirthday = intent.getStringExtra("birthday");
        birthday.setText(workerBirthday.isEmpty() ? "-" : workerBirthday);

        TextView age = (TextView) findViewById(R.id.age);
        String workerAge = intent.getStringExtra("age");
        age.setText(workerAge.isEmpty() ? "-" : workerAge);

        TextView specialties = (TextView) findViewById(R.id.specialties);
        long workerId = getIntent().getLongExtra("workerId", -1);
        List<Specialty> specialtiesList = Specialty.getSpecialties(workerId);
        specialties.setText(TextUtils.join(", ", specialtiesList.toArray()));
    }
}
