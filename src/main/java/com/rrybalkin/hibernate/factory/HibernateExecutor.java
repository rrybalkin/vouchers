package com.rrybalkin.hibernate.factory;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.function.Function;

public class HibernateExecutor {

	/**
	 * Executes a function within a hibernate transaction and open session.
	 * @param task the task to execute
	 * @param <T> optional return object type
	 * @return the task result
	 */
	public static <T> T execute(Function<Session, T> task) {
		Session session = null;
		Transaction tx = null;
		try {
			session = Factory.getSessionFactory().openSession();
			tx = session.beginTransaction();
			T res = task.apply(session);
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
}
