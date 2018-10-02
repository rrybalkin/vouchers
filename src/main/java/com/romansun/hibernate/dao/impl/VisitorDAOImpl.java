package com.romansun.hibernate.dao.impl;

import static com.romansun.hibernate.dao.utils.QueryStorage.ASSOCIATION_BIND;
import static com.romansun.hibernate.dao.utils.QueryStorage.GET_VISITORS_BY_ASSOCIATION;
import static com.romansun.hibernate.dao.utils.QueryStorage.GET_VISITORS_BY_MASK;
import static com.romansun.hibernate.dao.utils.QueryStorage.GET_VISITORS_BY_MASK_AND_ASSOCIATION;
import static com.romansun.hibernate.dao.utils.QueryStorage.GET_VISITORS_CNT_BY_ASSOCIATION;
import static com.romansun.hibernate.dao.utils.QueryStorage.MASK_BIND;

import java.util.Collection;

import org.hibernate.Query;
import org.hibernate.Session;

import com.romansun.hibernate.dao.VisitorDAO;
import com.romansun.hibernate.factory.Invoker;
import com.romansun.hibernate.entity.Association;
import com.romansun.hibernate.entity.Visitor;

public class VisitorDAOImpl implements VisitorDAO {

	@Override
	public void add(final Visitor v) throws Exception 
	{
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
	public void update(final Visitor v) throws Exception 
	{
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
	public void delete(final Visitor v) throws Exception
	{	
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
	public Visitor getById(final long id) throws Exception 
	{
		if (id <= 0) throw new IllegalArgumentException("ID must be positive");
		
		return new Invoker<Visitor>() {

			@Override
			public Visitor task(Session session) {
				Visitor visitor = (Visitor) session.load(Visitor.class, id);
				return visitor;
			}
		}.invoke();
	}

	@Override
	public Collection<Visitor> getAll() throws Exception 
	{
		return new Invoker<Collection<Visitor>>() {
			
			@Override
			public Collection<Visitor> task(Session session) {
				Collection<Visitor> visitors = session.createCriteria(Visitor.class).list();
				return visitors;
			}
		}.invoke();
	}

	@Override
	public Collection<Visitor> getVisitorsByCriteria(final Association a, final String mask) throws Exception {
		return new Invoker<Collection<Visitor>>() {
			@Override
			public Collection<Visitor> task(Session session) {
				Query query = null;
				if (a != null && (mask != null && !mask.isEmpty())) 
				{
					query = session
							.createQuery(GET_VISITORS_BY_MASK_AND_ASSOCIATION)
							.setString(MASK_BIND, "%" + mask.toLowerCase() + "%")
							.setLong(ASSOCIATION_BIND, a.getId());
				} 
				else if (a != null) 
				{
					query = session
							.createQuery(GET_VISITORS_BY_ASSOCIATION)
							.setLong(ASSOCIATION_BIND, a.getId());
				} 
				else if (mask != null && !mask.isEmpty()) 
				{
					query = session
							.createQuery(GET_VISITORS_BY_MASK)
							.setString(MASK_BIND, "%" + mask.toLowerCase() + "%");
				}

				return (Collection<Visitor>) query.list();
			}
		}.invoke();
	}

	@Override
	public long getCountVisitors(Association association) throws Exception
	{
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
