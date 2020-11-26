package com.databases.project1.repository;

import com.databases.project1.entity.PotHolesReported;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface PotHolesRepository extends PagingAndSortingRepository<PotHolesReported, UUID> {

}
