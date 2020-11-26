package com.databases.project1.repository;
import com.databases.project1.entity.GarbageCarts;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface GarbageCartsRepository extends PagingAndSortingRepository<GarbageCarts, UUID> {

}
