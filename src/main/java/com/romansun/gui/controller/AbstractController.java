package com.romansun.gui.controller;

import java.io.File;

import javafx.scene.control.TabPane;

import com.romansun.cache.impl.VisitorsCacheImpl;
import com.romansun.utils.Configuration;
import com.romansun.hibernate.dao.VisitorDAO;
import com.romansun.hibernate.factory.DAOFactory;
import org.apache.log4j.Logger;

public abstract class AbstractController {
	private static final Logger LOG = Logger.getLogger(AbstractController.class);

	protected final static String PATH_TO_REPORTS = "reports";
	protected final static Configuration config = Configuration.getInstance();
	
	protected static VisitorDAO visitorsDAO;
	protected static DAOFactory daoFactory;
	protected static TabPane mainTabPane;
	
	static {
		File reports = new File(PATH_TO_REPORTS);
		if (!reports.exists()) {
			boolean created = reports.mkdir();
			LOG.info("Reports folder created: " + created);
		}
		daoFactory = DAOFactory.getInstance();
		visitorsDAO = config.useVisitorsCache ? new VisitorsCacheImpl() : daoFactory.getVisitorDAO();
	}
}