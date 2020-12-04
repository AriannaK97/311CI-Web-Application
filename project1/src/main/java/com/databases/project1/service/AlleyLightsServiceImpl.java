package com.databases.project1.service;

import com.databases.project1.entity.AlleyLightsOut;
import com.databases.project1.entity.PotHolesReported;
import com.databases.project1.repository.AlleyLightsRepository;
import com.databases.project1.repository.PotHolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlleyLightsServiceImpl implements  AlleyLightsService{

    @Autowired
    AlleyLightsRepository alleyLightsRepository;

    public boolean saveAlleyLights(AlleyLightsOut alleyLightsOut) {
        return (alleyLightsRepository.save(alleyLightsOut) != null);
    }

}
