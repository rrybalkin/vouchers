package com.romansun.hibernate.DAO.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.hibernate.Query;
import org.hibernate.Session;

import com.romansun.hibernate.DAO.VisitorDAO;
import com.romansun.hibernate.factory.Factory;
import com.romansun.hibernate.logic.Association;
import com.romansun.hibernate.logic.Visitor;

public class VisitorDAOImpl implements VisitorDAO {

	@Override
	public void addVisitor(Visitor visitor) throws Exception {
		Session session = null;
		session = Factory.getSessionFactory().openSession();
		session.beginTransaction();
		session.save(visitor);
		session.getTransaction().commit();
		if (session != null && session.isOpen()) {
			session.close();
		}

	}

	@Override
	public void updateVisitor(Long visitor_id, Visitor newVisitor)
			throws Exception {
		Session session = null;
		session = Factory.getSessionFactory().openSession();
		session.beginTransaction();
		session.update(newVisitor);
		session.getTransaction().commit();

		if (session != null && session.isOpen()) {
			session.close();
		}

	}

	@Override
	public Visitor getVisitorById(Long visitor_id) throws Exception {
		Session session = null;
		Visitor visitor = null;
		session = Factory.getSessionFactory().openSession();
		visitor = (Visitor) session.load(Visitor.class, visitor_id);

		if (session != null && session.isOpen()) {
			session.close();
		}

		return visitor;
	}

	@Override
	public Collection<Visitor> getAllVisitors() throws Exception {
		Session session = null;
		Collection<Visitor> visitors = new ArrayList<Visitor>();
		session = Factory.getSessionFactory().openSession();
		visitors = session.createCriteria(Visitor.class).list();

		if (session != null && session.isOpen()) {
			session.close();
		}

		return visitors;
	}

	@Override
	public Collection<Visitor> getVisitorsByMask(String mask,
			Association association) throws Exception {
		Session session = null;
		Collection<Visitor> visitors = new ArrayList<Visitor>();
		session = Factory.getSessionFactory().openSession();
		session.beginTransaction();
		Query query = session
				.createQuery(
						" select v "
								+ " from Visitor v where firstname like :mask or lastname like :mask or middlename like :mask"
								+ " and association=:association_id")
				.setString("mask", "%" + mask + "%")
				.setLong("association_id", association.getId());
		visitors = query.list();
		session.getTransaction().commit();

		if (session != null && session.isOpen()) {
			session.close();
		}

		return visitors;
	}

	@Override
	public void deleteVisitor(Visitor visitor) throws Exception {
		Session session = null;
		session = Factory.getSessionFactory().openSession();
		session.beginTransaction();
		session.delete(visitor);
		session.getTransaction().commit();

		if (session != null && session.isOpen()) {
			session.close();
		}

	}

	@Override
	public Collection<Visitor> getVisitorsByAssociation(Association association)
			throws Exception {
		Session session = null;
		Collection<Visitor> visitors = new ArrayList<Visitor>();
		session = Factory.getSessionFactory().openSession();
		session.beginTransaction();
		Query query = session.createQuery(
				" select v "
						+ " from Visitor v where association=:association_id")
				.setLong("association_id", association.getId());
		visitors = query.list();
		session.getTransaction().commit();
		if (session != null && session.isOpen()) {
			session.close();
		}

		return visitors;
	}

	@Override
	public Collection<Visitor> getVisitorsByCriteria(Association association,
			String mask) throws Exception {
		Session session = null;
		Collection<Visitor> visitors = new ArrayList<Visitor>();
		session = Factory.getSessionFactory().openSession();
		session.beginTransaction();
		Query query = null;
		if (association != null && (mask != null && !mask.isEmpty())) {
			query = session
					.createQuery(
							" select v "
									+ " from Visitor v where (LOWER(lastname) || ' ' || LOWER(firstname) || ' ' || LOWER(middlename)) like :mask "
									+ " and association=:association_id")
					.setString("mask", "%" + mask.toLowerCase() + "%")
					.setLong("association_id", association.getId());
		} else if (association != null) {
			query = session
					.createQuery(
							" select v "
									+ " from Visitor v where association=:association_id")
					.setLong("association_id", association.getId());
		} else if (mask != null && !mask.isEmpty()) {
			query = session
					.createQuery(
							" select v "
									+ " from Visitor v where (LOWER(lastname) || ' ' || LOWER(firstname) || ' ' || LOWER(middlename)) like :mask")
					.setString("mask", "%" + mask.toLowerCase() + "%");
		}
		visitors = query.list();
		session.getTransaction().commit();
		if (session != null && session.isOpen()) {
			session.close();
		}

		return visitors;
	}
}
