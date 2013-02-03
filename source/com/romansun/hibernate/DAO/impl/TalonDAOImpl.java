package com.romansun.hibernate.DAO.impl;

import java.util.Collection;

import org.hibernate.Query;
import org.hibernate.Session;

import com.romansun.hibernate.DAO.TalonDAO;
import com.romansun.hibernate.factory.Factory;
import com.romansun.hibernate.logic.Talon;
import com.romansun.hibernate.logic.Visitor;

public class TalonDAOImpl implements TalonDAO {

	@Override
	public void addTalon(Talon talon) throws Exception {
		Session session = null;
		session = Factory.getSessionFactory().openSession();
		session.beginTransaction();
		session.save(talon);
		session.getTransaction().commit();

		if (session != null && session.isOpen()) {
			session.close();
		}
	}

	@Override
	public void updateTalonForVisitor(Visitor visitor, Talon newTalon)
			throws Exception {
		Session session = null;
		session = Factory.getSessionFactory().openSession();
		session.beginTransaction();
		session.update(newTalon);
		session.getTransaction().commit();

		if (session != null && session.isOpen()) {
			session.close();
		}
	}

	@Override
	public void deleteTalon(Talon talon) throws Exception {
		Session session = null;

		session = Factory.getSessionFactory().openSession();
		session.beginTransaction();
		session.delete(talon);
		session.getTransaction().commit();

		if (session != null && session.isOpen()) {
			session.close();
		}
	}

	@Override
	public void resetAllTalons() throws Exception {
		Session session = null;

		session = Factory.getSessionFactory().openSession();
		session.beginTransaction();
		Query query = session
				.createQuery(" update Talon t set t.count_lunch=0, t.count_dinner=0 ");
		query.executeUpdate();
		session.getTransaction().commit();

		if (session != null && session.isOpen()) {
			session.close();
		}

	}

	@Override
	public Collection<Talon> getAllTalons() throws Exception {
		Session session = null;
		Collection<Talon> talons = null;
		session = Factory.getSessionFactory().openSession();
		talons = session.createCriteria(Talon.class).list();

		if (session != null && session.isOpen()) {
			session.close();
		}

		return talons;
	}

	@Override
	public void resetLunchById(Long talon_id) throws Exception {
		Session session = null;
		session = Factory.getSessionFactory().openSession();
		session.beginTransaction();
		Query query = session.createQuery(
				" update Talon t set t.count_lunch=0 where talon_id = :id ")
				.setLong("id", talon_id);
		query.executeUpdate();
		session.getTransaction().commit();

		if (session != null && session.isOpen()) {
			session.close();
		}

	}

	@Override
	public void resetDinnerById(Long talon_id) throws Exception {
		Session session = null;
		session = Factory.getSessionFactory().openSession();
		session.beginTransaction();
		Query query = session.createQuery(
				" update Talon t set t.count_dinner=0 where talon_id = :id ")
				.setLong("id", talon_id);
		query.executeUpdate();
		session.getTransaction().commit();
		if (session != null && session.isOpen()) {
			session.close();
		}

	}
}
