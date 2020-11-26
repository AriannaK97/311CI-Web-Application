package com.databases.project1.repository;

import com.databases.project1.entity.ExtraIncidentInfo;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface ExtraIncidentInfoRepository extends PagingAndSortingRepository<ExtraIncidentInfo, UUID> {

}
