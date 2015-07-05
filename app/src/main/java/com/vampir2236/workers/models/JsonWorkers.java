package com.vampir2236.workers.models;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class JsonWorkers {
    @SerializedName("response")
    private List<Worker> mWorkers;

    public List<Worker> getWorkers() {
        return mWorkers;
    }

    public class Worker {

        @SerializedName("f_name")
        private String mFirstName;

        @SerializedName("l_name")
        private String mLastName;

        @SerializedName("birthday")
        private String mBirthday;

        @SerializedName("avatr_url")
        private String mAvatarUrl;

        @SerializedName("specialty")
        private List<Specialty> mSpecialties;

        private String capitalize(String inputString) {
            return inputString.substring(0, 1).toUpperCase() +
                    inputString.substring(1).toLowerCase();
        }

        public String getFirstName() {
            return capitalize(mFirstName);
        }

        public String getLastName() {
            return capitalize(mLastName);
        }

        public String getAvatarUrl() {
            return mAvatarUrl;
        }

        /**
         * Конвертирование в строку вида DD.MM.YYYY для записи в БД
         * из формтов YYYY-MM-DD, DD-MM-YYYY
         * @param stringDate
         * @return
         */
        private String convertToDDMMYYYY(String stringDate) {
            stringDate = stringDate == null ? "" : stringDate;
            String datePattern = "^(\\d{2})-(\\d{2})-(\\d{4})$";

            if (stringDate.matches(datePattern)) {
                stringDate = stringDate.replaceAll(datePattern, "$3-$2-$1");
            }

            SimpleDateFormat dateFormatFrom = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat dateFormatTo = new SimpleDateFormat("dd.MM.yyyy");
            try {
                Date date = dateFormatFrom.parse(stringDate);
                return dateFormatTo.format(date);
            } catch (ParseException e) {
                return "";
            }
        }

        public String getBirthday() {
            return convertToDDMMYYYY(mBirthday);
        }

        public List<Specialty> getSpecialties() {
            return mSpecialties;
        }
    }

    public class Specialty {
        @SerializedName("specialty_id")
        private long mSpecialtyId;

        @SerializedName("name")
        private String mName;

        public long getSpecialtyId() {
            return mSpecialtyId;
        }

        public String getName() {
            return mName;
        }
    }
}
