package com.rrybalkin.gui.controller;

import com.rrybalkin.hibernate.dao.VisitorDAO;
import com.rrybalkin.hibernate.factory.DAOFactory;
import javafx.scene.control.TabPane;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

public abstract class AbstractController {
	private static final Logger LOG = Logger.getLogger(AbstractController.class);

	protected final static String PATH_TO_REPORTS = "reports";
	
	protected static final VisitorDAO visitorsDAO;
	protected static final DAOFactory daoFactory;
	protected static AtomicReference<TabPane> mainTabPaneRef = new AtomicReference<>();
	
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