package com.romansun.hibernate.dao.impl;

import com.romansun.hibernate.dao.VisitorDAO;
import com.romansun.hibernate.entity.Association;
import com.romansun.hibernate.entity.Visitor;
import com.romansun.hibernate.factory.Invoker;
import com.romansun.utils.SuppressFBWarnings;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.Collection;
import java.util.Locale;

import static com.romansun.hibernate.dao.utils.QueryStorage.*;

@SuppressFBWarnings("SIC_INNER_SHOULD_BE_STATIC_ANON")
public class VisitorDAOImpl implements VisitorDAO {

	@Override
	public void add(final Visitor v) {
		if (v == null) throw new IllegalArgumentException("Visitor must be not null");
		
		new Invoker<Void>() {
			@Override
			public Void task(Session session) {
				session.save(v);
				return null;
			}
		}.invoke();
	}

	@Override
	public void update(final Visitor v) {
		if (v == null) throw new IllegalArgumentException("Visitor must be not null");
		
		new Invoker<Void>() {
			@Override
			public Void task(Session session) {
				session.update(v);
				return null;
			}
		}.invoke();
	}
	
	@Override
	public void delete(final Visitor v) {
		if (v == null) throw new IllegalArgumentException("Visitor must be not null");
		
		new Invoker<Void>() {
			@Override
			public Void task(Session session) {
				session.delete(v);
				return null;
			}
		}.invoke();
	}

	@Override
	public Visitor getById(final long id) {
		if (id <= 0) throw new IllegalArgumentException("ID must be positive");
		
		return new Invoker<Visitor>() {

			@Override
			public Visitor task(Session session) {
				return session.load(Visitor.class, id);
			}
		}.invoke();
	}

	@Override
	public Collection<Visitor> getAll() {
		return new Invoker<Collection<Visitor>>() {
			
			@Override
			public Collection<Visitor> task(Session session) {
				return (Collection<Visitor>) session.createCriteria(Visitor.class).list();
			}
		}.invoke();
	}

	@Override
	public Collection<Visitor> getVisitorsByCriteria(final Association a, final String mask) {
		return new Invoker<Collection<Visitor>>() {
			@Override
			public Collection<Visitor> task(Session session) {
				Query query;
				if (a != null && (mask != null && !mask.isEmpty())) {
					query = session
							.createQuery(GET_VISITORS_BY_MASK_AND_ASSOCIATION)
							.setString(MASK_BIND, "%" + mask.toLowerCase(Locale.getDefault()) + "%")
							.setLong(ASSOCIATION_BIND, a.getId());
				}
				else if (a != null) {
					query = session
							.createQuery(GET_VISITORS_BY_ASSOCIATION)
							.setLong(ASSOCIATION_BIND, a.getId());
				} 
				else if (mask != null && !mask.isEmpty()) {
					query = session
							.createQuery(GET_VISITORS_BY_MASK)
							.setString(MASK_BIND, "%" + mask.toLowerCase(Locale.getDefault()) + "%");
				}
				else {
					throw new IllegalArgumentException("Association or mask shouldn't be null");
				}

				return (Collection<Visitor>) query.list();
			}
		}.invoke();
	}

	@Override
	public long getCountVisitors(Association association) {
		return new Invoker<Long>() {

			@Override
			public Long task(Session session) {
				return (Long) session
						.createQuery(GET_VISITORS_CNT_BY_ASSOCIATION)
						.setLong(ASSOCIATION_BIND, association.getId())
						.uniqueResult();
			}
		}.invoke();
	}
}
