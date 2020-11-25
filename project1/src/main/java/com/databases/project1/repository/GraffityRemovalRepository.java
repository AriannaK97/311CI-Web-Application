package com.databases.project1.repository;
import com.databases.project1.entity.GraffitiRemoval;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface GraffityRemovalRepository extends PagingAndSortingRepository<GraffitiRemoval, UUID> {

}
