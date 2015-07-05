package com.vampir2236.workers.models;

import android.provider.BaseColumns;
import android.text.format.DateUtils;
import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.activeandroid.util.SQLiteUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Table(name="worker", id=BaseColumns._ID)
public class Worker extends Model {
    @Column(name="first_name")
    public String firstName;

    @Column(name="last_name")
    public String lastName;

    @Column(name="birthday")
    public String birthday;

    @Column(name="avatar_url")
    public String avatarUrl;

    /**
     * Вычисление возраста
     * @param birthday день рождения
     * @return количество лет
     */
    private int calculateAge(Date birthday) {
        Calendar birth = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        birth.setTime(birthday);

        int age = today.get(Calendar.YEAR) - birth.get(Calendar.YEAR);

        if (today.get(Calendar.MONTH) < birth.get(Calendar.MONTH) &&
                today.get(Calendar.DAY_OF_MONTH) < birth.get(Calendar.DAY_OF_MONTH)) age--;
        return age;
    }

    /**
     * Возраст сотрудника
     * @return
     */
    public String getAge() {
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        try {
            Date birthday = df.parse(this.birthday);
            return String.valueOf(calculateAge(birthday));
        } catch (Exception e) {
            return "";
        }
    }

    public Worker() {
        super();
    }

    public Worker(String firstName, String lastName, String birthday, String avatarUrl) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.avatarUrl = avatarUrl;
    }

    /**
     * Список сотрудников по указанной специальности
     * @param specialtyId
     * @return
     */
    public static List<Worker> getWorkers(long specialtyId) {
        return new Select()
                .from(Worker.class).as("w")
                .innerJoin(WorkerSpecialty.class).as("ws")
                .on("w._id = ws.Worker")
                .where("ws.Specialty = ?", specialtyId)
                .orderBy("last_name ASC, first_name ASC")
                .execute();
    }
}
