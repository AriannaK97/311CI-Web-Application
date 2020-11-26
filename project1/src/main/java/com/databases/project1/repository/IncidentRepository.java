package com.databases.project1.repository;

import com.databases.project1.entity.Incident;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface IncidentRepository extends PagingAndSortingRepository<Incident, UUID> {

}
