package com.databases.project1.repository;

import com.databases.project1.entity.Incident;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

public interface IncidentRepository extends PagingAndSortingRepository<Incident, UUID> {

    @Query(value = "SELECT i.id, i.request_type, i.location, ST_Distance(i.location,ST_SetSRID(ST_Point(:userLongitude,:userLatitude),4326)) AS distance "
            + "FROM incident i "
            + "ORDER BY i.location  <-> ST_SetSRID(ST_Point(:userLongitude,:userLatitude),4326) "
            + "LIMIT 1"
            , nativeQuery = true)

    List<Incident> findTopIncidentTypeForSpecificBoundedBoxOnAGivenDate(@Param("userLongitude") Float userLongitude,@Param("userLatitude")  Float userLatitude);

}

