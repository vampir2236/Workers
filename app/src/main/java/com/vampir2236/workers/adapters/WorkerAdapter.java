package com.vampir2236.workers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vampir2236.workers.R;
import com.vampir2236.workers.models.Worker;
import com.vampir2236.workers.picasso.PicassoUtils;

import java.util.List;

public class WorkerAdapter extends BaseAdapter {

    private Context mContext;
    private List<Worker> mWorkers;

    public WorkerAdapter(Context context, List<Worker> workers) {
        this.mContext = context;
        this.mWorkers = workers;
    }

    @Override
    public int getCount() {
        return mWorkers.size();
    }

    @Override
    public Object getItem(int position) {
        return mWorkers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mWorkers.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.worker_item, parent, false);
        }
        Worker worker = mWorkers.get(position);

        ImageView avatar = (ImageView) convertView.findViewById(R.id.avatar);
        PicassoUtils.loadImage(mContext, worker.avatarUrl, R.drawable.guest, avatar);

        TextView lastname = (TextView) convertView.findViewById(R.id.lastname);
        lastname.setText(worker.lastName);

        TextView firstname = (TextView) convertView.findViewById(R.id.firstname);
        firstname.setText(worker.firstName);

        TextView age = (TextView) convertView.findViewById(R.id.age);
        TextView ageTitle = (TextView) convertView.findViewById(R.id.ageTitle);
        String workerAge = worker.getAge();

        if (workerAge.isEmpty()) {
            age.setText("");
            ageTitle.setVisibility(View.GONE);
        } else {
            age.setText(workerAge);
            ageTitle.setVisibility(View.VISIBLE);
        }

        return convertView;
    }
}
