package com.databases.project1.service;

import com.databases.project1.entity.AbandonedVehicle;
import com.databases.project1.repository.AbandonedVehicleRepository;
import org.springframework.stereotype.Service;

@Service
public class AbandonedVehicleServiceImpl implements AbandonedVehicleService {

    AbandonedVehicleRepository abandonedVehicleRepository;

    public boolean saveVehicle(AbandonedVehicle abandonedVehicle) {
        return (abandonedVehicleRepository.save(abandonedVehicle) != null);

    }

}
