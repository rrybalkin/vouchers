package com.romansun.gui.controller;

import com.romansun.hibernate.dao.VisitorDAO;
import com.romansun.hibernate.factory.DAOFactory;
import com.romansun.utils.Configuration;
import javafx.scene.control.TabPane;
import org.apache.log4j.Logger;

import java.io.File;

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
		visitorsDAO = daoFactory.getVisitorDAO();
	}


}