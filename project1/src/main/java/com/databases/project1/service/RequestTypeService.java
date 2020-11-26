package com.databases.project1.service;

import com.databases.project1.entity.RequestType;

public interface RequestTypeService {

    public RequestType findByName(String name);

    public void save(RequestType requestType);

}
