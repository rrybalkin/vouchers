package com.romansun.hibernate.factory;

import java.io.File;
import java.util.Locale;

import org.hibernate.cfg.Configuration;
import org.hibernate.SessionFactory;

public class Factory {
	private static final SessionFactory sessionFactory;
	private static final String PATH_TO_CONFIG_FILE = "config\\hibernate.cfg.xml";
	static {
		// This line is needed for right working
		Locale.setDefault(Locale.ENGLISH);
		try {
			sessionFactory = new Configuration().configure(new File(PATH_TO_CONFIG_FILE)).buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}
