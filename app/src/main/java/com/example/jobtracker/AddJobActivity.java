package com.example.jobtracker;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddJobActivity extends AppCompatActivity {

    private EditText companyEdit, positionEdit, statusEdit;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);

        companyEdit = findViewById(R.id.companyEdit);
        positionEdit = findViewById(R.id.positionEdit);
        statusEdit = findViewById(R.id.statusEdit);
        saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(v -> {
            String company = companyEdit.getText().toString();
            String position = positionEdit.getText().toString();
            String status = statusEdit.getText().toString();

            if (!company.isEmpty() && !position.isEmpty() && !status.isEmpty()) {
                // Ici tu ajouteras le code pour insérer en base
                Toast.makeText(this, "Candidature ajoutée", Toast.LENGTH_SHORT).show();
                finish(); // Retour à l'écran principal
            } else {
                Toast.makeText(this, "Remplis tous les champs", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
