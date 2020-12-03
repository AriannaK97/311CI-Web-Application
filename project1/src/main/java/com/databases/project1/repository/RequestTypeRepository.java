package com.databases.project1.repository;

import com.databases.project1.entity.RequestType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

public interface RequestTypeRepository extends PagingAndSortingRepository<RequestType, UUID> {

    @Query(value = "select name from request_type where name is not null ;\n ",nativeQuery = true)
    List<String> findAllRequestTypeNames();
}
