package com.romansun.hibernate.factory;

import java.io.File;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.hibernate.cfg.Configuration;
import org.hibernate.stat.Statistics;
import org.hibernate.SessionFactory;

import com.romansun.config.Resources;

public class Factory {
	private static final Logger LOG = Logger.getLogger(Factory.class);
	private static final SessionFactory sessionFactory;
	static {
		// This line is needed for correct work
		Locale.setDefault(Locale.ENGLISH);
		try {
			File config = new File("config" + File.separator + "hibernate.cfg.xml");
            if (!config.exists()) {
				config = Resources.getInstance().getHibernateConfig();
				LOG.info("External hibernate.cfg.xml is not found - using inner...");
			} else {
                LOG.info("External hibernate.cfg.xml is found");
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

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}
