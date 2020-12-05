package com.databases.project1.service;

import com.databases.project1.entity.UserActionLog;
import com.databases.project1.repository.UserActionLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl implements LoggingService{

    @Autowired
    UserActionLogRepository logRepository;

    public UserActionLog logAction(UserActionLog log) {
        return logRepository.save(log);
    }

}
