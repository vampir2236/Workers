package com.vampir2236.workers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vampir2236.workers.R;
import com.vampir2236.workers.models.Specialty;

import java.util.List;

public class SpecialtyAdapter extends BaseAdapter {

    private Context mContext;
    private List<Specialty> mSpecialties;

    public SpecialtyAdapter(Context context, List<Specialty> specialties) {
        this.mContext = context;
        this.mSpecialties = specialties;
    }

    @Override
    public int getCount() {
        return mSpecialties.size();
    }

    @Override
    public Object getItem(int position) {
        return mSpecialties.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mSpecialties.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.specialty_item, parent, false);
        }

        TextView specialty = (TextView) convertView.findViewById(R.id.specialty);
        specialty.setText(mSpecialties.get(position).name);

        return convertView;
    }
}
