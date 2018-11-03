package com.rrybalkin.hibernate.dao.impl;

import com.rrybalkin.hibernate.dao.AssociationDAO;
import com.rrybalkin.hibernate.entity.Association;
import com.rrybalkin.hibernate.factory.HibernateExecutor;

import java.util.Collection;
import java.util.Objects;

import static com.rrybalkin.hibernate.dao.utils.QueryStorage.GET_COUNT_OF_ASSOCIATIONS;

public class AssociationDAOImpl implements AssociationDAO {

	@Override
	public void add(final Association a) {
		Objects.requireNonNull(a, "Association must not be null");
		HibernateExecutor.execute(session -> session.save(a));
	}

	@Override
	public void update(final Association a) {
		Objects.requireNonNull(a, "Association must not be null");
		HibernateExecutor.execute(session -> {
			session.update(a);
			return a;
		});
	}

	@Override
	public void delete(final Association a) {
		Objects.requireNonNull(a, "Association must not be null");
		HibernateExecutor.execute(session -> {
			session.delete(a);
			return a;
		});
	}

	@Override
	public Association getById(final long id) {
		if (id <= 0)
			throw new IllegalArgumentException("ID must be positive");

		return HibernateExecutor.execute(session -> session.load(Association.class, id));
	}

	@Override
	public Collection<Association> getAll() {
		return HibernateExecutor.execute(
				session -> session.createCriteria(Association.class).list());
	}

	@Override
	public int getCount() {
		return HibernateExecutor.execute(
				session -> (Integer) session.createQuery(GET_COUNT_OF_ASSOCIATIONS).uniqueResult());
	}

}
