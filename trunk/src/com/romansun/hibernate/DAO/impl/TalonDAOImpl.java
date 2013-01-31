package com.romansun.hibernate.DAO.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.hibernate.Query;
import org.hibernate.Session;

import com.romansun.hibernate.DAO.TalonDAO;
import com.romansun.hibernate.factory.Factory;
import com.romansun.hibernate.logic.Talon;
import com.romansun.hibernate.logic.Visitor;

public class TalonDAOImpl implements TalonDAO {

	@Override
	public void addTalon(Talon talon) throws SQLException {
		Session session = null;
		try {
			session = Factory.getSessionFactory().openSession();
			session.beginTransaction();
			session.save(talon);
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
	public void updateTalonForVisitor(Visitor visitor, Talon newTalon)
			throws SQLException {
		Session session = null;
		try {
			session = Factory.getSessionFactory().openSession();
			session.beginTransaction();
			session.update(newTalon);
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
	public void deleteTalon(Talon talon) throws SQLException {
		Session session = null;
		try {
			session = Factory.getSessionFactory().openSession();
			session.beginTransaction();
			session.delete(talon);
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
}
