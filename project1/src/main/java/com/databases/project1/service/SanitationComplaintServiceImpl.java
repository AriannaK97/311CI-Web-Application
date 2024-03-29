package com.databases.project1.service;

import com.databases.project1.entity.SanitationCodeComplaints;
import com.databases.project1.repository.SanitationComplaintsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SanitationComplaintServiceImpl implements SanitationComplaintService {

    @Autowired
    SanitationComplaintsRepository sanitationComplaintsRepository;

    public boolean saveSanitationComplaint(SanitationCodeComplaints sanitationCodeComplaints) {
        return (sanitationComplaintsRepository.save(sanitationCodeComplaints) != null);
    }

}
