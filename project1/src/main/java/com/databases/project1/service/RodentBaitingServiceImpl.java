package com.databases.project1.service;

import com.databases.project1.entity.RodentBaiting;
import com.databases.project1.repository.RodentBaitingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RodentBaitingServiceImpl implements RodentBaitingService {

    @Autowired
    RodentBaitingRepository rodentBaitingRepository;

    public boolean saveRodentBaiting(RodentBaiting rodentBaiting) {
        return (rodentBaitingRepository.save(rodentBaiting) != null);
    }

}
