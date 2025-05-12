package com.example.jobtracker.data.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.jobtracker.data.model.JobApplication;
import com.example.jobtracker.data.repository.JobRepository;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ApplicationViewModel extends AndroidViewModel {
    private final JobRepository repository;
    private final Executor executor;
    private final MutableLiveData<Boolean> operationSuccess = new MutableLiveData<>();
    private final MutableLiveData<JobApplication> currentApplication = new MutableLiveData<>();

    public ApplicationViewModel(@NonNull Application application) {
        super(application);
        this.repository = new JobRepository(application);
        this.executor = Executors.newSingleThreadExecutor();
    }

    // Opérations CRUD
    public void insertApplication(JobApplication application) {
        executor.execute(() -> {
            repository.insert(application);
            operationSuccess.postValue(true);
        });
    }

    public void updateApplication(JobApplication application) {
        executor.execute(() -> {
            repository.update(application);
            operationSuccess.postValue(true);
        });
    }

    public void deleteApplication(JobApplication application) {
        executor.execute(() -> {
            repository.delete(application);
            operationSuccess.postValue(true);
        });
    }

    // Requêtes
    public LiveData<List<JobApplication>> getAllApplications() {
        return repository.getAllApplications();
    }

    public LiveData<List<JobApplication>> getApplicationsByUser(int userId) {
        return repository.getApplicationsByUser(userId);
    }

    public void loadApplicationById(int applicationId) {
        executor.execute(() -> {
            JobApplication application = repository.getApplicationById(applicationId);
            currentApplication.postValue(application);
        });
    }

    public LiveData<JobApplication> getCurrentApplication() {
        return currentApplication;
    }

    // Opérations spécifiques
    public void updateApplicationStatus(int applicationId, String newStatus) {
        executor.execute(() -> {
            repository.updateStatus(applicationId, newStatus);
            operationSuccess.postValue(true);
        });
    }

    // Observables
    public LiveData<Boolean> getOperationSuccess() {
        return operationSuccess;
    }
}