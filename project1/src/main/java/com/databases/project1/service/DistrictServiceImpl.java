package com.databases.project1.service;

import com.databases.project1.entity.District;
import com.databases.project1.repository.DistrictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DistrictServiceImpl implements DistrictService {

    @Autowired
    DistrictRepository districtRepository;

    public District saveDistrict(District district) {
        return districtRepository.save(district);
    }

    public District findIfDistrictExists(Integer communityArea, Integer policeDistrict, Integer ward, Integer zipcode){
        return districtRepository.findByCommunityAreaAndPoliceDistrictAndWardAndZipcode(communityArea, policeDistrict, ward, zipcode).orElse(null);
    }

}
