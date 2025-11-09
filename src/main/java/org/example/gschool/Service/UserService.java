package org.example.gschool.Service;



import org.example.gschool.Dao.UserDao;
import org.example.gschool.Entity.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    public boolean authenticate(String email, String password) {
        Admin user = userDao.findByEmail(email);
        return user != null && user.getPassword().equals(password);
    }
}
