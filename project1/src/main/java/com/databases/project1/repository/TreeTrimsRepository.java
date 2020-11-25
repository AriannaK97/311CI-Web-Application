package com.databases.project1.repository;
import com.databases.project1.entity.TreeTrims;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface TreeTrimsRepository extends PagingAndSortingRepository<TreeTrims, UUID> {

}
