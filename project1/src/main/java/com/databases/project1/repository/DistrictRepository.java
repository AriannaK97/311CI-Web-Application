package com.databases.project1.repository;

import com.databases.project1.entity.District;
import com.databases.project1.entity.Incident;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DistrictRepository extends PagingAndSortingRepository<District, UUID> {


    @Query(value = "select id from district " +
            "where community_area= :communityArea and police_district= :policeDistrict " +
            "and ward= :ward and zip_code= :zipcode;", nativeQuery = true )
    public Optional<District> findIfDistrictExists(@Param("communityArea") Integer communityArea,
                                                   @Param("policeDistrict") Integer policeDistrict,
                                                   @Param("ward") Integer ward, @Param("zipcode") Integer zipcode);
}
