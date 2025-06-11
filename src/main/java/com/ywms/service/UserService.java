package com.ywms.service;

import com.ywms.dao.User;

public interface UserService {
    public User getUserById(int id);

    User validateUser(int userId, String password);
}
