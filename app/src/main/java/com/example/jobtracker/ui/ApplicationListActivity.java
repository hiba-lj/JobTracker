package com.example.jobtracker.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.jobtracker.R;
import com.example.jobtracker.data.viewmodel.ApplicationViewModel;

public class ApplicationListActivity extends AppCompatActivity {
    private TextView tvEmptyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_list);

        tvEmptyList = findViewById(R.id.tvEmptyList);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ApplicationAdapter adapter = new ApplicationAdapter();
        recyclerView.setAdapter(adapter);

        int userId = getIntent().getIntExtra("USER_ID", -1);
        ApplicationViewModel viewModel = new ViewModelProvider(this).get(ApplicationViewModel.class);

        viewModel.getApplicationsByUser(userId).observe(this, applications -> {
            if (applications != null && !applications.isEmpty()) {
                adapter.setApplications(applications);
                tvEmptyList.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                tvEmptyList.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        });
    }
}