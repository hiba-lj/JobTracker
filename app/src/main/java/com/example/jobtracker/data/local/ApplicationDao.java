package com.example.jobtracker.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;
import com.example.jobtracker.data.model.JobApplication;
import java.util.List;

@Dao
public interface ApplicationDao {
    // Opérations CRUD de base
    @Insert
    void insert(JobApplication application);

    @Update
    void update(JobApplication application);

    @Delete
    void delete(JobApplication application);

    // Requêtes spécifiques
    @Query("SELECT * FROM job_applications ORDER BY created_at DESC")
    LiveData<List<JobApplication>> getAllApplications();

    @Query("SELECT * FROM job_applications WHERE user_id = :userId ORDER BY created_at DESC")
    LiveData<List<JobApplication>> getApplicationsByUser(int userId);

    @Query("SELECT * FROM job_applications WHERE id = :applicationId LIMIT 1")
    JobApplication getApplicationById(int applicationId);

    @Query("DELETE FROM job_applications WHERE id = :applicationId")
    void deleteById(int applicationId);

    // Requêtes supplémentaires utiles
    @Query("SELECT COUNT(*) FROM job_applications WHERE user_id = :userId")
    LiveData<Integer> getApplicationCountForUser(int userId);

    @Query("UPDATE job_applications SET status = :newStatus WHERE id = :applicationId")
    void updateStatus(int applicationId, String newStatus);
}