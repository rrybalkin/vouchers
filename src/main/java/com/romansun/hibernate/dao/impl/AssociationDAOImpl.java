package com.romansun.hibernate.dao.impl;

import com.romansun.hibernate.dao.AssociationDAO;
import com.romansun.hibernate.entity.Association;
import com.romansun.hibernate.factory.Invoker;
import org.hibernate.Session;

import java.util.Collection;

import static com.romansun.hibernate.dao.utils.QueryStorage.GET_COUNT_OF_ASSOCIATIONS;

public class AssociationDAOImpl implements AssociationDAO {

	@Override
	public void add(final Association a) {
		if (a == null)
			throw new IllegalArgumentException("Association must be not null");

		new Invoker<Void>() {
			@Override
			public Void task(Session session) {
				session.save(a);
				return null;
			}
		}.invoke();
	}

	@Override
	public void update(final Association a) {
		if (a == null)
			throw new IllegalArgumentException("Association must be not null");

		new Invoker<Void>() {
			@Override
			public Void task(Session session) {
				session.update(a);
				return null;
			}
		}.invoke();
	}

	@Override
	public void delete(final Association a) {
		if (a == null)
			throw new IllegalArgumentException("Association must be not null");

		new Invoker<Void>() {
			@Override
			public Void task(Session session) {
				session.delete(a);
				return null;
			}
		}.invoke();
	}

	@Override
	public Association getById(final long id) {
		if (id <= 0)
			throw new IllegalArgumentException("ID must be positive");

		return new Invoker<Association>() {

			@Override
			public Association task(Session session) {
				Association asc = session.load(Association.class,	id);
				return asc;
			}
		}.invoke();
	}

	@Override
	public Collection<Association> getAll() {
		return new Invoker<Collection<Association>>() {

			@Override
			public Collection<Association> task(Session session) {
				Collection<Association> ascs = session.createCriteria(Association.class).list();
				return ascs;
			}
		}.invoke();
	}

	@Override
	public int getCount() {
		return new Invoker<Integer>() {

			@Override
			public Integer task(Session session) {
				return (Integer) session.createQuery(GET_COUNT_OF_ASSOCIATIONS).uniqueResult();
			}
		}.invoke();
	}

}
