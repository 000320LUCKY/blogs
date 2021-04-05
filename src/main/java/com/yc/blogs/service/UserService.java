package com.yc.blogs.service;

import com.yc.blogs.po.User;

public interface UserService {
    User checkUser(String username, String password);
}
