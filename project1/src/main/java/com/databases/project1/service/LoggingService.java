package com.databases.project1.service;

import com.databases.project1.entity.UserActionLog;

public interface LoggingService {

    public UserActionLog logAction(UserActionLog log);

}
