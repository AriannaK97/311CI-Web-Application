package com.databases.project1.service;

import com.databases.project1.entity.District;
import org.springframework.data.repository.query.Param;

public interface DistrictService {
    public boolean saveDistrict(District district);
    public District findIfDistrictExists(Integer communityArea, Integer policeDistrict, Integer ward, Integer zipcode);
}
