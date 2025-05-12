package com.example.jobtracker.data.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.jobtracker.R;
import com.example.jobtracker.data.model.JobApplication;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {
    private List<JobApplication> jobList = new ArrayList<>();
    private final SimpleDateFormat dateFormat =
            new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_job, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        JobApplication app = jobList.get(position);
        holder.company.setText(app.getCompanyName());
        holder.position.setText(app.getPosition());
        holder.status.setText(app.getStatus());
        holder.date.setText(dateFormat.format(app.getCreatedAt()));
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public void submitList(List<JobApplication> jobs) {
        this.jobList = new ArrayList<>(jobs);
        notifyDataSetChanged();
    }

    static class JobViewHolder extends RecyclerView.ViewHolder {
        final TextView company, position, status, date;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            company = itemView.findViewById(R.id.companyText);
            position = itemView.findViewById(R.id.positionText);
            status = itemView.findViewById(R.id.statusText);
            date = itemView.findViewById(R.id.dateText);
        }
    }
}