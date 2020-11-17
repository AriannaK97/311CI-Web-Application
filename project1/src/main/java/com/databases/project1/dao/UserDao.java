package com.databases.project1.dao;


import com.databases.project1.entity.RegisteredUser;

public interface UserDao {

    public RegisteredUser findByUserName(String username);
    
    public void save(RegisteredUser user);
    
}
