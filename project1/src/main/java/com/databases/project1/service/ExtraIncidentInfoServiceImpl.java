package com.databases.project1.service;

import com.databases.project1.entity.ExtraIncidentInfo;
import com.databases.project1.repository.ExtraIncidentInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExtraIncidentInfoServiceImpl implements ExtraIncidentInfoService {

    @Autowired
    ExtraIncidentInfoRepository extraIncidentInfoRepository;

    public boolean saveExtraIncidentInfo(ExtraIncidentInfo extraIncidentInfo){
        return (extraIncidentInfoRepository.save(extraIncidentInfo) != null);
    }

}
