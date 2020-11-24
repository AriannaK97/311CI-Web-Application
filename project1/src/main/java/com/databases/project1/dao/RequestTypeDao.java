package com.databases.project1.dao;

import com.databases.project1.entity.RegisteredUser;
import com.databases.project1.entity.RequestType;

public interface RequestTypeDao {

    public RequestType findByName(String name);

    public void save(RequestType requestType);

}
