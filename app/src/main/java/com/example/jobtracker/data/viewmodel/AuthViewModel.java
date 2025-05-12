package com.example.jobtracker.data.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.jobtracker.data.database.AppDatabase;
import com.example.jobtracker.data.DAO.UserDao;
import com.example.jobtracker.data.model.User;
import com.example.jobtracker.data.repository.AuthRepository;

public class AuthViewModel extends AndroidViewModel {
    private final AuthRepository authRepo;
    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public AuthViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getDatabase(application);
        UserDao userDao = db.userDao();
        this.authRepo = new AuthRepository(userDao);
    }

    public void register(String email, String password, String firstName, String lastName, String phone) {
        User user = new User(email, password, firstName, lastName, phone);
        authRepo.register(user, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(User user) {
                userLiveData.postValue(user);
            }

            @Override
            public void onError(String message) {
                errorLiveData.postValue(message);
            }
        });
    }

    public void login(String email, String password) {
        authRepo.login(email, password, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(User user) {
                userLiveData.postValue(user);
            }

            @Override
            public void onError(String message) {
                errorLiveData.postValue(message);
            }
        });
    }

    public MutableLiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public MutableLiveData<String> getErrorLiveData() {
        return errorLiveData;
    }
}