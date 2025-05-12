package com.example.jobtracker.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.jobtracker.R;
import com.google.android.material.card.MaterialCardView;

public class DashboardActivity extends AppCompatActivity {
    private static final String TAG = "DashboardActivity";
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Log.d(TAG, "onCreate");

        // 1. Récupération de l'ID utilisateur
        userId = getIntent().getIntExtra("USER_ID", -1);
        if (userId == -1) {
            showErrorAndFinish("Erreur d'authentification");
            return;
        }

        // 2. Initialisation et configuration des vues
        setupViewComponents();
    }

    private void setupViewComponents() {
        MaterialCardView addCard = findViewById(R.id.addApplicationCard);
        MaterialCardView trackCard = findViewById(R.id.trackApplicationsCard);

        if (addCard == null || trackCard == null) {
            showErrorAndFinish("Erreur d'interface");
            return;
        }

        addCard.setOnClickListener(v -> navigateToActivity(AddApplicationActivity.class));
        trackCard.setOnClickListener(v -> navigateToActivity(ApplicationListActivity.class));
    }

    private void navigateToActivity(Class<?> activityClass) {
        try {
            Intent intent = new Intent(this, activityClass);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);

            // Optionnel: Animations de transition
            try {
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } catch (Resources.NotFoundException e) {
                Log.w(TAG, "Animations non trouvées, utilisation des transitions par défaut");
            }
        } catch (Exception e) {
            Log.e(TAG, "Erreur de navigation: " + e.getMessage());
            Toast.makeText(this, "Impossible d'ouvrir cette fonctionnalité", Toast.LENGTH_SHORT).show();
        }
    }

    private void showErrorAndFinish(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        Log.e(TAG, errorMessage);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}