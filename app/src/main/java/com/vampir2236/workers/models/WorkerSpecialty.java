package com.vampir2236.workers.models;

import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

@Table(name="worker_specialty", id=BaseColumns._ID)
public class WorkerSpecialty extends Model {

    @Column(name="Worker")
    public Worker worker;

    @Column(name="Specialty")
    public Specialty specialty;

    public WorkerSpecialty() {
        super();
    }

    public WorkerSpecialty(Worker worker, Specialty specialty) {
        super();
        this.worker = worker;
        this.specialty = specialty;
    }

}
