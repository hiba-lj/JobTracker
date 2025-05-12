package com.example.jobtracker.ui;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.jobtracker.R;
import com.example.jobtracker.data.model.JobApplication;
import java.util.ArrayList;
import java.util.List;

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ApplicationViewHolder> {
    private List<JobApplication> applications = new ArrayList<>();

    public static class ApplicationViewHolder extends RecyclerView.ViewHolder {
        TextView tvCompany, tvPosition, tvDate, tvStatus, tvDetails;

        public ApplicationViewHolder(View itemView) {
            super(itemView);
            tvCompany = itemView.findViewById(R.id.tvCompany);
            tvPosition = itemView.findViewById(R.id.tvPosition);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvDetails = itemView.findViewById(R.id.tvDetails);
        }
    }

    public void setApplications(List<JobApplication> applications) {
        this.applications = applications;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ApplicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_application, parent, false);
        return new ApplicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicationViewHolder holder, int position) {
        JobApplication application = applications.get(position);

        holder.tvCompany.setText(application.getCompanyName());
        holder.tvPosition.setText("Poste: " + application.getPosition());
        holder.tvDate.setText("Date: " + application.getFormattedDate());
        holder.tvDetails.setText(application.getApplicantInfo());

        // Gestion du statut avec couleur
        switch (application.getStatus()) {
            case "Accepté":
                holder.tvStatus.setText("Statut: Accepté");
                holder.tvStatus.setTextColor(Color.GREEN);
                break;
            case "Refusé":
                holder.tvStatus.setText("Statut: Refusé");
                holder.tvStatus.setTextColor(Color.RED);
                break;
            default:
                holder.tvStatus.setText("Statut: En cours");
                holder.tvStatus.setTextColor(Color.GRAY);
        }
    }

    @Override
    public int getItemCount() {
        return applications.size();
    }
}