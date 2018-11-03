package com.rrybalkin.hibernate.factory;

import com.rrybalkin.utils.Resources;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.stat.Statistics;

import java.io.File;
import java.util.Locale;

public class Factory {
	private static final Logger LOG = Logger.getLogger(Factory.class);

	private static final SessionFactory sessionFactory;

	static {
		// This line is needed for correct work
		Locale.setDefault(Locale.ENGLISH);
		try {
			File config = getHibernateConfig();
			if (!config.exists()) {
                LOG.error("Hibernate config is not found");
			} else {
				LOG.info("Hibernate config is found");
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

	private static File getHibernateConfig() {
		File config;
		final String appDb = com.rrybalkin.utils.Configuration.getInstance().appDatabase;
		if ("mysql".equalsIgnoreCase(appDb)) {
			config = Resources.getResource(Resources.HIBERNATE_MYSQL_CONFIG);
		} else if ("hibernate".equalsIgnoreCase(appDb)) {
			config = Resources.getResource(Resources.HIBERNATE_ORACLE_CONFIG);
		} else {
			throw new IllegalArgumentException("App DB " + appDb + " not supported");
		}
		return config;
	}

	static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}
