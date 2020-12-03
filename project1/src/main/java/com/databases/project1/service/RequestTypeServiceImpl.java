package com.databases.project1.service;

import com.databases.project1.dao.RequestTypeDao;
import com.databases.project1.entity.RegisteredUser;
import com.databases.project1.entity.RequestType;
import com.databases.project1.repository.RequestTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class RequestTypeServiceImpl implements RequestTypeService {

    @Autowired
    RequestTypeRepository requestTypeRepository;

    public List<String> findAllNames() {
        return  requestTypeRepository.findAllRequestTypeNames();
    }

}
