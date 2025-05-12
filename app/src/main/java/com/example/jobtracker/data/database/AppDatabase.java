package com.example.jobtracker.data.database;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import android.content.Context;
import android.util.Log;

import com.example.jobtracker.data.local.ApplicationDao;
import com.example.jobtracker.data.DAO.UserDao;
import com.example.jobtracker.data.model.JobApplication;
import com.example.jobtracker.data.model.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {JobApplication.class, User.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ApplicationDao applicationDao();
    public abstract UserDao userDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "job_tracker_db")
                            .fallbackToDestructiveMigration() // Ajouté pour gérer les changements de schéma
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    Log.d("AppDatabase", "Database created");
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public static ExecutorService getWriteExecutor() {
        return databaseWriteExecutor;
    }
}