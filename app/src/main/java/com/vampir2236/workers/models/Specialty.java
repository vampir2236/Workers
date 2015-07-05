package com.vampir2236.workers.models;

import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

@Table(name = "specialty", id = BaseColumns._ID)
public class Specialty extends Model {
    @Column(name = "specialty_id")
    public long specialtyId;

    @Column(name = "name")
    public String name;

    public Specialty() {
        super();
    }

    public Specialty(long specialtyId, String name) {
        super();
        this.specialtyId = specialtyId;
        this.name = name;
    }

    public String toString() {
        return name;
    }

    /**
     * Все специальности
     * @return
     */
    public static List<Specialty> all() {
        return new Select()
                .from(Specialty.class)
                .orderBy("name ASC")
                .execute();
    }

    /**
     * Список специальностей работника
     * @param workerId
     * @return
     */
    public static List<Specialty> getSpecialties(long workerId) {
        return new Select()
                .from(Specialty.class).as("s")
                .innerJoin(WorkerSpecialty.class).as("ws")
                .on("s._id = ws.Specialty")
                .where("ws.Worker = ?", workerId)
                .orderBy("name ASC")
                .execute();
    }
}
