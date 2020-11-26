package com.databases.project1.repository;

import com.databases.project1.entity.RodentBaiting;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface RodentBaitingRepository extends PagingAndSortingRepository<RodentBaiting, UUID> {

}
