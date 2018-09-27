package com.romansun.hibernate.factory;

import java.io.File;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.hibernate.cfg.Configuration;
import org.hibernate.stat.Statistics;
import org.hibernate.SessionFactory;

import com.romansun.utils.Resources;

public class Factory {
	private static final Logger LOG = Logger.getLogger(Factory.class);

	private static final SessionFactory sessionFactory;

	static {
		// This line is needed for correct work
		Locale.setDefault(Locale.ENGLISH);
		try {
			File config =Resources.getInstance().getResource(Resources.HIBERNATE_CONFIG);
			if (!config.exists()) {
                LOG.error("File hibernate.cfg.xml is not found");
			} else {
				LOG.info("File hibernate.cfg.xml is found");
			}
			sessionFactory = new Configuration().configure(config).buildSessionFactory();
			Statistics stats = sessionFactory.getStatistics();
			stats.setStatisticsEnabled(true);
			stats.logSummary();
		} catch (Throwable e) {
			LOG.error("Initial SessionFactory creation failed: ", e);
			throw new ExceptionInInitializerError(e);
		}
	}

	static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}
