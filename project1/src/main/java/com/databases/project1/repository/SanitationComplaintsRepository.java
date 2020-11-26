package com.databases.project1.repository;

import com.databases.project1.entity.SanitationCodeComplaints;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface SanitationComplaintsRepository extends PagingAndSortingRepository<SanitationCodeComplaints, UUID> {

}
