package com.databases.project1.repository;

import com.databases.project1.entity.TreeDebris;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface TreeDebrisRepository extends PagingAndSortingRepository<TreeDebris, UUID> {

}
