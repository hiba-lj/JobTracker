package com.example.jobtracker.data.remote;

import com.example.jobtracker.data.model.JobApplication;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {
    @GET("applications")
    Call<List<JobApplication>> getApplications();

    @POST("applications")
    Call<JobApplication> addApplication(@Body JobApplication application);
}
