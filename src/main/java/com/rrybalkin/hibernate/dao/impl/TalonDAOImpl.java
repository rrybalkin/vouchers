package com.rrybalkin.hibernate.dao.impl;

import com.rrybalkin.hibernate.dao.TalonDAO;
import com.rrybalkin.hibernate.entity.Talon;
import com.rrybalkin.hibernate.factory.HibernateExecutor;

import java.util.List;
import java.util.Objects;

import static com.rrybalkin.hibernate.dao.utils.QueryStorage.*;

public class TalonDAOImpl implements TalonDAO {

	@Override
	public void add(final Talon t) {
		Objects.requireNonNull(t, "Talon must not be null");
		HibernateExecutor.execute(session -> session.save(t));
	}

	@Override
	public void update(final Talon t) {
		Objects.requireNonNull(t, "Talon must not be null");
		HibernateExecutor.execute(session -> {
			session.update(t);
			return t;
		});
	}

	@Override
	public void delete(final Talon t) {
		Objects.requireNonNull(t, "Talon must not be null");
		HibernateExecutor.execute(session -> {
			session.delete(t);
			return t;
		});
	}

	@Override
	public Talon getById(final long id) {
		if (id <= 0)
			throw new IllegalArgumentException("ID must be positive");

		return HibernateExecutor.execute(session -> session.load(Talon.class, id));
	}

	@Override
	public List<Talon> getAll() {
		return HibernateExecutor.execute(session -> session.createCriteria(Talon.class).list());
	}

	@Override
	public void resetAllTalons() {
		HibernateExecutor.execute(
				session -> session.createQuery(RESET_ALL_TALONS).executeUpdate());
	}

	@Override
	public void resetBreakfastById(long id) {
		HibernateExecutor.execute(
				session -> session.createQuery(RESET_BREAKFASTS_BY_TALON).setLong(TALON_BIND, id).executeUpdate());
	}

	@Override
	public void resetLunchById(long id) {
		HibernateExecutor.execute(
				session -> session.createQuery(RESET_LUNCHES_BY_TALON).setLong(TALON_BIND, id).executeUpdate());
	}

	@Override
	public void resetDinnerById(long id) {
		HibernateExecutor.execute(
				session -> session.createQuery(RESET_DINNERS_BY_TALON).setLong(TALON_BIND, id).executeUpdate());
	}

	@Override
	public int getCountBreakfasts() {
		return HibernateExecutor.execute(
				session -> (Integer) session.createQuery(GET_COUNT_OF_BREAKFASTS).uniqueResult());
	}

	@Override
	public int getCountLunches() {
		return HibernateExecutor.execute(
				session -> (Integer) session.createQuery(GET_COUNT_OF_LUNCHES).uniqueResult());
	}

	@Override
	public int getCountDinners() {
		return HibernateExecutor.execute(
				session -> (Integer) session.createQuery(GET_COUNT_OF_DINNERS).uniqueResult());
	}
}
