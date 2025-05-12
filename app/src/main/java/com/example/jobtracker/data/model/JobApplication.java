package com.example.jobtracker.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity(tableName = "job_applications")
public class JobApplication {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "user_id")
    private int userId;

    @ColumnInfo(name = "first_name")
    private String firstName;

    @ColumnInfo(name = "last_name")
    private String lastName;

    private String position;
    private String phone;
    private String email;
    private String city;

    @ColumnInfo(name = "company_name")
    private String companyName;

    private String status = "En cours"; // Valeur par défaut

    @ColumnInfo(name = "created_at")
    private long createdAt;

    public JobApplication(int userId, String firstName, String lastName, String position,
                          String phone, String email, String city, String companyName) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.phone = phone;
        this.email = email;
        this.city = city;
        this.companyName = companyName;
        this.createdAt = System.currentTimeMillis();
    }

    // Méthodes utilitaires
    public String getFormattedDate() {
        return new SimpleDateFormat("dd/MM/yyyy à HH:mm", Locale.FRENCH).format(new Date(createdAt));
    }

    public String getApplicantInfo() {
        return firstName + " " + lastName + "\n" + email + " | " + phone;
    }

    // Getters et Setters - Tous les champs doivent avoir des setters pour Room
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}