package com.romansun.gui.controller;

import java.io.File;

import javafx.scene.control.TabPane;

import com.romansun.config.Configuration;
import com.romansun.hibernate.factory.DAOFactory;
import com.romansun.reports.logic.Report;

public abstract class AbstractController {
	protected final static String PATH_TO_REPORTS = "reports";
	protected final static Configuration config = Configuration.getInstance();
	protected static DAOFactory dao; 
	protected static TabPane mainTabPane;
	
	static {
		File reports = new File(PATH_TO_REPORTS);
		if (!reports.exists()) {
			reports.mkdir();
		}
		dao = DAOFactory.getInstance();
	}
	
	protected static String upFirst(String str) {
		StringBuilder res = new StringBuilder(1000);
		if (str != null && !str.isEmpty()) {
		res.append(str.substring(0, 1).toUpperCase());
		res.append(str.substring(1).toLowerCase());
		
		return res.toString();
		} else {
			return "?";
		}
	}
}