package com.romansun.hibernate.DAO.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.hibernate.Session;

import com.romansun.hibernate.DAO.AssociationDAO;
import com.romansun.hibernate.factory.Factory;
import com.romansun.hibernate.logic.Association;

public class AssociationDAOImpl implements AssociationDAO {

	@Override
	public void addAssociation(Association association) throws Exception {
		Session session = null;
		session = Factory.getSessionFactory().openSession();
		session.beginTransaction();
		session.save(association);
		session.getTransaction().commit();
		if (session != null && session.isOpen()) {
			session.close();
		}
	}

	@Override
	public void updateAssociation(Long association_id,
			Association newAssociation) throws Exception {
		Session session = null;
		session = Factory.getSessionFactory().openSession();
		session.beginTransaction();
		session.update(newAssociation);
		session.getTransaction().commit();

		if (session != null && session.isOpen()) {
			session.close();
		}
	}

	@Override
	public Association getAssociationById(Long association_id) throws Exception {
		Session session = null;
		Association association = null;
		session = Factory.getSessionFactory().openSession();
		association = (Association) session.load(Association.class,
				association_id);

		if (session != null && session.isOpen()) {
			session.close();
		}
		return association;
	}

	@Override
	public Collection<Association> getAllAssociations() throws Exception {
		Session session = null;
		session = Factory.getSessionFactory().openSession();
		List<Association> associations = session.createCriteria(Association.class).list();
		Collections.sort(associations);

		if (session != null && session.isOpen()) {
			session.close();
		}
		return associations;
	}

	@Override
	public void deleteAssociation(Association association) throws Exception {
		Session session = null;
		session = Factory.getSessionFactory().openSession();
		session.beginTransaction();
		session.delete(association);
		session.getTransaction().commit();

		if (session != null && session.isOpen()) {
			session.close();
		}

	}

}
