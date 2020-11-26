package com.databases.project1.repository;

import com.databases.project1.entity.UserActionLog;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface UserActionLogRepository extends PagingAndSortingRepository<UserActionLog, UUID> {

    UserActionLog findByUserName(String username);

}
