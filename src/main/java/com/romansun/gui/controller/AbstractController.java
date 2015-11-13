package com.romansun.gui.controller;

import java.io.File;

import javafx.scene.control.TabPane;

import com.romansun.cache.impl.VisitorsCacheImpl;
import com.romansun.config.Configuration;
import com.romansun.hibernate.dao.VisitorDAO;
import com.romansun.hibernate.factory.DAOFactory;

public abstract class AbstractController {
	protected final static String PATH_TO_REPORTS = "reports";
	protected final static Configuration config = Configuration.getInstance();
	
	protected static VisitorDAO visitorsDAO;
	protected static DAOFactory dao;
	protected static TabPane mainTabPane;
	
	static {
		File reports = new File(PATH_TO_REPORTS);
		if (!reports.exists()) {
			reports.mkdir();
		}
		dao = DAOFactory.getInstance();
		visitorsDAO = (config.useVisitorsCache) ? new VisitorsCacheImpl() : dao.getVisitorDAO();
	}
}