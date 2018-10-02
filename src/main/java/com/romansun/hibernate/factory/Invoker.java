package com.romansun.hibernate.factory;

import org.hibernate.Session;
import org.hibernate.Transaction;

public abstract class Invoker<T> {
	
	public T invoke() {
		Session session = null;
		Transaction tx = null;
		try {
			session = Factory.getSessionFactory().openSession();
			tx = session.beginTransaction();
			T res = task(session);
			tx.commit();
			
			return res;
		} catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}
	
	public abstract T task(Session session);
}
