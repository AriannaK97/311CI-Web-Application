package com.databases.project1.dao;


import com.databases.project1.entity.RequestType;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class RequestTypeDaoImpl implements RequestTypeDao{

    @Autowired
    private EntityManager entityManager;

    public RequestType findByName(String name) {
        Session currentSession = entityManager.unwrap(Session.class);
        Query<RequestType> theQuery = currentSession.createQuery("from RequestType where name=:name", RequestType.class);
        theQuery.setParameter("name", name);
        RequestType requestType = null;
        try {
            requestType = theQuery.getSingleResult();
        } catch (Exception e) {
            requestType = null;
        }

        return requestType;
    }

    @Override
    public void save(RequestType requestType) {
        Session currentSession = entityManager.unwrap(Session.class);
        currentSession.saveOrUpdate(requestType);
    }


}
