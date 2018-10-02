package com.romansun.hibernate.dao.impl;

import com.romansun.hibernate.dao.TalonDAO;
import com.romansun.hibernate.entity.Talon;
import com.romansun.hibernate.factory.Invoker;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.Collection;

import static com.romansun.hibernate.dao.utils.QueryStorage.*;

public class TalonDAOImpl implements TalonDAO {

	@Override
	public void add(final Talon t) {
		if (t == null)
			throw new IllegalArgumentException("Talon must be not null");

		new Invoker<Void>() {
			@Override
			public Void task(Session session) {
				session.save(t);
				return null;
			}
		}.invoke();
	}

	@Override
	public void update(final Talon t) {
		if (t == null)
			throw new IllegalArgumentException("Talon must be not null");

		new Invoker<Void>() {
			@Override
			public Void task(Session session) {
				session.update(t);
				return null;
			}
		}.invoke();
	}

	@Override
	public void delete(final Talon t) {
		if (t == null)
			throw new IllegalArgumentException("Talon must be not null");

		new Invoker<Void>() {
			@Override
			public Void task(Session session) {
				session.delete(t);
				return null;
			}
		}.invoke();
	}

	@Override
	public Talon getById(final long id) {
		if (id <= 0)
			throw new IllegalArgumentException("ID must be positive");

		return new Invoker<Talon>() {

			@Override
			public Talon task(Session session) {
				return session.load(Talon.class, id);
			}
		}.invoke();
	}

	@Override
	public Collection<Talon> getAll() {
		return new Invoker<Collection<Talon>>() {
			
			@Override
			public Collection<Talon> task(Session session) {
				Collection<Talon> talons = session.createCriteria(Talon.class).list();
				return talons;
			}
		}.invoke();
	}

	@Override
	public void resetAllTalons() {
		new Invoker<Void>() {

			@Override
			public Void task(Session session) {
				Query query = session.createQuery(RESET_ALL_TALONS);
				query.executeUpdate();
				return null;
			}
		}.invoke();
	}

	@Override
	public void resetLunchById(final long id) {
		new Invoker<Void>() {

			@Override
			public Void task(Session session) {
				Query query = session.createQuery(RESET_LUNCHES_BY_TALON).setLong(TALON_BIND, id);
				query.executeUpdate();
				return null;
			}
		}.invoke();
	}

	@Override
	public void resetDinnerById(final long id) {
		new Invoker<Void>() {

			@Override
			public Void task(Session session) {
				Query query = session.createQuery(RESET_DINNERS_BY_TALON).setLong(TALON_BIND, id);
				query.executeUpdate();
				return null;
			}
		}.invoke();
	}

	@Override
	public int getCountLunches() {
		return new Invoker<Integer>() {

			@Override
			public Integer task(Session session) {
				return (Integer) session.createQuery(GET_COUNT_OF_LUNCHES).uniqueResult();
			}
		}.invoke();
	}

	@Override
	public int getCountDinners() {
		return new Invoker<Integer>() {

			@Override
			public Integer task(Session session) {
				return (Integer) session.createQuery(GET_COUNT_OF_DINNERS).uniqueResult();
			}
		}.invoke();
	}
}
