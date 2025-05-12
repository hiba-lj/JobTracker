package com.example.jobtracker.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.jobtracker.data.database.AppDatabase;
import com.example.jobtracker.data.local.ApplicationDao;
import com.example.jobtracker.data.model.JobApplication;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class JobRepository {
    private final ApplicationDao dao;
    private final Executor executor;

    public JobRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        this.dao = db.applicationDao();
        this.executor = Executors.newFixedThreadPool(4);
    }

    // Opérations CRUD
    public void insert(JobApplication application) {
        executor.execute(() -> dao.insert(application));
    }

    public void update(JobApplication application) {
        executor.execute(() -> dao.update(application));
    }

    public void delete(JobApplication application) {
        executor.execute(() -> dao.delete(application));
    }

    // Requêtes
    public LiveData<List<JobApplication>> getAllApplications() {
        return dao.getAllApplications();
    }

    public LiveData<List<JobApplication>> getApplicationsByUser(int userId) {
        return dao.getApplicationsByUser(userId);
    }

    // Correction ici - Retour direct de l'objet sans LiveData
    public JobApplication getApplicationById(int applicationId) {
        return dao.getApplicationById(applicationId);
    }

    // Opérations spécifiques
    public void updateStatus(int applicationId, String newStatus) {
        executor.execute(() -> {
            JobApplication application = dao.getApplicationById(applicationId);
            if (application != null) {
                application.setStatus(newStatus);
                dao.update(application);
            }
        });
    }
}