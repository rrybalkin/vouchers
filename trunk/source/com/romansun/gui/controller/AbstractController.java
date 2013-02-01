package com.romansun.gui.controller;

import com.romansun.hibernate.factory.DAOFactory;
import com.romansun.reports.ReportsManager;

public abstract class AbstractController {
	
	protected static DAOFactory dao; 
	
	protected static ReportsManager reportsManager;
	
	protected final static String PATH_TO_REPORTS = "reports";
	
	static {
		dao = DAOFactory.getInstance();
		reportsManager = new ReportsManager(PATH_TO_REPORTS);
		reportsManager.loadReports();
	}
}