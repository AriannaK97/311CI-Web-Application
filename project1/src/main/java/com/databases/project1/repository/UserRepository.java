package com.databases.project1.repository;

import com.databases.project1.entity.RegisteredUser;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<RegisteredUser,Long> {
    RegisteredUser findByUserName(String username);

}
