package com.databases.project1.service;

import com.databases.project1.entity.PotHolesReported;
import com.databases.project1.repository.PotHolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PotHolesServiceImpl implements PotHolesService {

    @Autowired
    PotHolesRepository potHolesRepository;

    public boolean savePotHoles(PotHolesReported potHolesReported) {
        return (potHolesRepository.save(potHolesReported) != null);
    }

}
