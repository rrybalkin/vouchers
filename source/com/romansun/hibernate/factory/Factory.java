package com.romansun.hibernate.factory;

import java.io.File;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.hibernate.cfg.Configuration;
import org.hibernate.SessionFactory;

import com.romansun.gui.utils.Resources;

public class Factory {
	private static final Logger LOG = Logger.getLogger(Factory.class);
	private static final SessionFactory sessionFactory;
	static {
		// This line is needed for right working
		Locale.setDefault(Locale.ENGLISH);
		try {
			File config = new File("config\\hibernate.cfg.xml");
			if (!config.exists()) {
				config = new Resources().getHibernateConfig();
				LOG.info("Внешний hibernate.cfg.xml не был найден - используется внутренний");
			} else {
				LOG.info("Внешний hibernate.cfg.xml был найден");
			}
			sessionFactory = new Configuration().configure(config).buildSessionFactory();
		} catch (Throwable e) {
			LOG.error("Initial SessionFactory creation failed. ", e);
			throw new ExceptionInInitializerError(e);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}
