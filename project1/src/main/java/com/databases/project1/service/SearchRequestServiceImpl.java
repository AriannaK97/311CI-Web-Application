package com.databases.project1.service;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.List;

@Service
public class SearchRequestServiceImpl implements SearchRequestService {

    @Autowired
    EntityManager entityManager;

    @Transactional
    public List<Object[]> findtotalRequestsPerType(LocalDate firstDate, LocalDate secondDate) {

        Session currentSession = entityManager.unwrap(Session.class);
        Query query = entityManager.createNativeQuery("select count(*) cnt, I.request_type from incident I\n" +
                "where CREATION_DATE between :first and :second \n" +
                "group by I.request_type;");
        query.setParameter("first", firstDate);
        query.setParameter("second", secondDate);
        List<Object[]> totalRequestsPerType = query.getResultList();
        return totalRequestsPerType;
    }

}
