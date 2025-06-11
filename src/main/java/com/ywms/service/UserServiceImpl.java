package com.ywms.service;

import com.ywms.dao.User;
import com.ywms.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUserById(int id) {
        return userRepository.findById(id).orElseThrow(RuntimeException::new);
    }


    @Override
    public User validateUser(int userId, String password) {

        Optional<User> userOptional = userRepository.findByUserId(userId);


        if (userOptional.isEmpty()) {
            return null;
        }


        User user = userOptional.get();
        if (user.getPassword().equals(password)) {
            return user;
        } else {
            return null;
        }
    }



}
