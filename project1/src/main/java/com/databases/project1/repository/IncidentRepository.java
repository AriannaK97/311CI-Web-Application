package com.databases.project1.repository;

import com.databases.project1.entity.Incident;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface IncidentRepository extends PagingAndSortingRepository<Incident, UUID> {

    @Query(value = "select count(*) cnt, I.request_type from incident I\n" +
            "where CREATION_DATE between :first and :second \n" +
            "group by I.request_type;", nativeQuery = true )
    public List<Object[]> findtotalRequestsPerType(@Param("first") LocalDate first, @Param("second") LocalDate second);


}

