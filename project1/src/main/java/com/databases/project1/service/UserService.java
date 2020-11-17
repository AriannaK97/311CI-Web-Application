package com.databases.project1.service;

import com.databases.project1.entity.RegisteredUser;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

	public RegisteredUser findByUserName(String userName);

	public void save(RegisteredUser appUser);
}
