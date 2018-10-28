package com.romansun.gui.controller;

import java.io.File;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javafx.scene.control.TabPane;

import com.romansun.cache.impl.VisitorsCacheImpl;
import com.romansun.utils.Configuration;
import com.romansun.hibernate.dao.VisitorDAO;
import com.romansun.hibernate.factory.DAOFactory;
import javafx.util.StringConverter;
import org.apache.log4j.Logger;

public abstract class AbstractController {
	private static final Logger LOG = Logger.getLogger(AbstractController.class);

	protected final static String PATH_TO_REPORTS = "reports";
	protected final static Configuration config = Configuration.getInstance();
	
	protected static final VisitorDAO visitorsDAO;
	protected static final DAOFactory daoFactory;
	protected TabPane mainTabPane;
	
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