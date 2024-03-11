package com.example.clinicmanagementsystem.services.userService;

import com.example.clinicmanagementsystem.domain.User;
import com.example.clinicmanagementsystem.repository.userRepo.UserSpringData;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserSvc implements IUserService {
    private final UserSpringData userRepo;
    private final BCryptPasswordEncoder encoder;

    public UserSvc(UserSpringData userRepo, BCryptPasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    @Override
    public void createNewUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(encoder.encode(password));
        userRepo.save(user);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepo.findByUsername(username)
                .orElse(null);
    }

    @Override
    public boolean deleteUser(String username) {
        userRepo.delete(findUserByUsername(username));
        return userRepo.findByUsername(username).isEmpty();
    }

    @Override
    public User findUserById(Long id) {
        return userRepo.findById(id).orElse(null);
    }
}
