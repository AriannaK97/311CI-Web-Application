package com.databases.project1.repository;

import com.databases.project1.entity.District;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface DistrictRepository extends PagingAndSortingRepository<District, UUID> {
}
