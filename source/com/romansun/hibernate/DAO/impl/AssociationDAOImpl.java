package com.romansun.hibernate.DAO.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.hibernate.Session;

import com.romansun.hibernate.DAO.AssociationDAO;
import com.romansun.hibernate.factory.Factory;
import com.romansun.hibernate.logic.Association;

public class AssociationDAOImpl implements AssociationDAO {

	@Override
	public void addAssociation(Association association) throws SQLException {
		Session session = null;
		try {
			session = Factory.getSessionFactory().openSession();
			session.beginTransaction();
			session.save(association);
			session.getTransaction().commit();
		}
		catch (Exception ex) {
			System.out.println("Ошибка при добавлении элемента." + ex);
		}
		finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	@Override
	public void updateAssociation(Long association_id,
			Association newAssociation) throws SQLException {
		Session session = null;
		try {
			session = Factory.getSessionFactory().openSession();
			session.beginTransaction();
			session.update(newAssociation);
			session.getTransaction().commit();
		}
		catch (Exception ex) {
			System.out.println("Ошибка при добавлении элемента." + ex);
		}
		finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	@Override
	public Association getAssociationById(Long association_id)
			throws SQLException {
		Session session = null;
		Association association = null;
		try {
			session = Factory.getSessionFactory().openSession();
			association = (Association) session.load(Association.class, association_id);
		}
		catch (Exception ex) {
			System.out.println("Ошибка 'findById'" + ex);
		}
		finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return association;
	}

	@Override
	public Collection<Association> getAllAssociations() throws SQLException {
		Session session = null;
		Collection<Association> associations = new ArrayList<Association>();
		try {
			session = Factory.getSessionFactory().openSession();
			associations = session.createCriteria(Association.class).list();
		}
		catch (Exception ex) {
			System.out.println("Ошибка 'getAll'");
			ex.printStackTrace();
		}
		finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return associations;
	}

	@Override
	public void deleteAssociation(Association association) throws SQLException {
		Session session = null;
		try {
			session = Factory.getSessionFactory().openSession();
			session.beginTransaction();
			session.delete(association);
			session.getTransaction().commit();
		}
		catch (Exception ex) {
			System.out.println("Ошибка при добавлении элемента.");
		}
		finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

}
