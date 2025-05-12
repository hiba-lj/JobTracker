package com.example.jobtracker.data.local;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.jobtracker.data.model.JobApplication;

@Database(entities = {JobApplication.class}, version = 1)
public abstract class JobDatabase extends RoomDatabase {
    private static JobDatabase instance;

    public abstract ApplicationDao applicationDao();

    public static synchronized JobDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            JobDatabase.class, "job_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
