package com.example.jobtracker.data.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.jobtracker.data.model.JobApplication;
import com.example.jobtracker.data.repository.JobRepository;

import java.util.List;

public class JobViewModel extends AndroidViewModel {

    private final JobRepository repository;
    private final LiveData<List<JobApplication>> allApplications;

    public JobViewModel(@NonNull Application application) {
        super(application);
        repository = new JobRepository(application);
        allApplications = repository.getAllApplications();
    }

    public void insert(JobApplication application) {
        repository.insert(application);
    }

    public LiveData<List<JobApplication>> getAllApplications() {
        return allApplications;
    }
}
