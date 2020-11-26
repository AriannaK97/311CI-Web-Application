package com.databases.project1.repository;

import com.databases.project1.entity.AlleyLightsOut;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface AlleyLightsRepository extends PagingAndSortingRepository<AlleyLightsOut, UUID> {

}
