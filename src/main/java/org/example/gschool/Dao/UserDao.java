package org.example.gschool.Dao;

import org.example.gschool.Entity.Admin;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
@Repository
public class UserDao {
    private static List<Admin> users = new ArrayList<>();

    static {
        users.add(new Admin("admin@gmail.com", "admin123")); // Utilisateur de test
    }

    public Admin findByEmail(String email) {
        for (Admin user : users) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }
}

