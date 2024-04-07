package com.jumtospring.rest.service;

import com.jumtospring.rest.entity.User;

import java.util.List;

public interface UserService {
    String register(User newUser);
    void modify(User newUser);
    void remove(String id);

    User find(String id);
    List<User> findAll();
}
