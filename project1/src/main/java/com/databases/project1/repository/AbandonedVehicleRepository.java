package com.databases.project1.repository;

import com.databases.project1.entity.AbandonedVehicle;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface AbandonedVehicleRepository extends PagingAndSortingRepository<AbandonedVehicle, UUID> {

}
