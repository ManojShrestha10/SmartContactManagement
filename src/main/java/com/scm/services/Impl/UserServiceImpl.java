package com.scm.services.Impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.scm.entities.User;
import com.scm.helper.ResourceNotFoundException;
import com.scm.repositories.UserRepo;
import com.scm.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public User saveUser(User user) {
        // user id have to generate
        String userId = UUID.randomUUID().toString();
        // set the user id
        user.setUserId(userId);
        return userRepo.save(user);
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepo.findById(id);
    }

    @Override
    public Optional<User> updateuser(User user) {
        User newUser = userRepo.findById(user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        // update newUser with the values from user
        newUser.setName(user.getName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        newUser.setPhoneNumber(user.getPhoneNumber());
        newUser.setAbout(user.getAbout());
        newUser.setProfilePic(user.getProfilePic());
        newUser.setEnabled(user.isEnabled());
        newUser.setEmailVerified(user.isEmailVerified());
        newUser.setPhoneVerified(user.isPhoneVerified());
        newUser.setProvider(user.getProvider());
        newUser.setProviderUserId(user.getProviderUserId());
        // save the updated user
        User userSave = userRepo.save(newUser);
        return Optional.ofNullable(userSave);
    }

    @Override
    public void deleteUser(String id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        // delete the user
        userRepo.delete(user);
    }

    @Override
    public boolean isUserExist(String userId) {
        User user = userRepo.findById(userId)
                .orElse(null);
        return user != null ? true : false;
    }

    @Override
    public boolean isUserExistByEmail(String email) {
        User user = userRepo.findByEmail(email).orElse(null);
        return user != null ? true : false;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

}
