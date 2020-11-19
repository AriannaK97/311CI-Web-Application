package com.databases.project1.dao;

import com.databases.project1.entity.RegisteredUser;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class UserDaoImpl implements UserDao {

	@Autowired
	private EntityManager entityManager;

	@Override
	public RegisteredUser findByUserName(String username) {
		Session currentSession = entityManager.unwrap(Session.class);
		Query<RegisteredUser> theQuery = currentSession.createQuery("from RegisteredUser where userName=:username", RegisteredUser.class);
		theQuery.setParameter("username", username);
		RegisteredUser user = null;
		try {
			user = theQuery.getSingleResult();
		} catch (Exception e) {
			user = null;
		}

		return user;
	}

	@Override
	public void save(RegisteredUser user) {
		Session currentSession = entityManager.unwrap(Session.class);
		currentSession.saveOrUpdate(user);
	}

}
