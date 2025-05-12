package com.example.jobtracker.data.repository;

import com.example.jobtracker.data.DAO.UserDao;
import com.example.jobtracker.data.model.User;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AuthRepository {
    private final UserDao userDao;
    private final Executor executor;

    public AuthRepository(UserDao userDao) {
        this.userDao = userDao;
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void register(User user, AuthCallback callback) {
        executor.execute(() -> {
            try {
                if (userDao.getUserByEmail(user.email) != null) {
                    callback.onError("Email déjà utilisé");
                    return;
                }
                long id = userDao.insert(user);
                User registeredUser = userDao.getUserById((int) id);
                if (registeredUser != null) {
                    callback.onSuccess(registeredUser);
                } else {
                    callback.onError("Erreur lors de l'inscription");
                }
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        });
    }

    public void login(String email, String password, AuthCallback callback) {
        executor.execute(() -> {
            try {
                User user = userDao.getUserByEmail(email);
                if (user != null && user.password.equals(password)) {
                    callback.onSuccess(user);
                } else {
                    callback.onError("Identifiants incorrects");
                }
            } catch (Exception e) {
                callback.onError("Erreur de connexion");
            }
        });
    }

    public interface AuthCallback {
        void onSuccess(User user);
        void onError(String message);
    }
}