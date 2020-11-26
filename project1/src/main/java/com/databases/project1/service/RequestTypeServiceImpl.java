package com.databases.project1.service;

import com.databases.project1.dao.RequestTypeDao;
import com.databases.project1.entity.RegisteredUser;
import com.databases.project1.entity.RequestType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
public class RequestTypeServiceImpl implements RequestTypeService {

    @Autowired
	private RequestTypeDao requestTypeDao;

    @Override
    @Transactional
    public RequestType findByName(String name) {
        return requestTypeDao.findByName(name);
    }

    @Override
    @Transactional
    public void save(RequestType requestType) {
        requestTypeDao.save(requestType);
    }


}
