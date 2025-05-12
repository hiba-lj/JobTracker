package com.example.jobtracker.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.jobtracker.R;
import com.example.jobtracker.data.model.JobApplication;
import com.example.jobtracker.data.viewmodel.ApplicationViewModel;

public class AddApplicationActivity extends AppCompatActivity {

    private EditText etFirstName, etLastName, etPosition, etPhone, etEmail, etCity, etCompany;
    private ApplicationViewModel viewModel;
    private int userId; // Ajout pour stocker l'ID utilisateur

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_application);

        // Récupérer l'ID utilisateur de l'Intent
        userId = getIntent().getIntExtra("USER_ID", -1);
        if (userId == -1) {
            Toast.makeText(this, "Erreur: Utilisateur non identifié", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialisation des vues
        initViews();

        // ViewModel
        viewModel = new ViewModelProvider(this).get(ApplicationViewModel.class);

        setupSubmitButton();
    }

    private void initViews() {
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etPosition = findViewById(R.id.etPosition);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etCity = findViewById(R.id.etCity);
        etCompany = findViewById(R.id.etCompany);
    }

    private void setupSubmitButton() {
        Button btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(v -> submitApplication());
    }

    private void submitApplication() {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String position = etPosition.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String city = etCity.getText().toString().trim();
        String company = etCompany.getText().toString().trim();

        if (validateInputs(firstName, lastName, position, phone, email, city, company)) {
            JobApplication application = new JobApplication(
                    userId, // Utilisation de l'ID utilisateur réel
                    firstName,
                    lastName,
                    position,
                    phone,
                    email,
                    city,
                    company
            );

            viewModel.insertApplication(application); // Correction du nom de méthode
            Toast.makeText(this, "Candidature ajoutée", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private boolean validateInputs(String... inputs) {
        for (String input : inputs) {
            if (input.isEmpty()) {
                Toast.makeText(this, "Tous les champs sont obligatoires", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        // Validation supplémentaire de l'email
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(inputs[4]).matches()) {
            etEmail.setError("Email invalide");
            return false;
        }

        return true;
    }
}