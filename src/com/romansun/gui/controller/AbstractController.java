package com.romansun.gui.controller;

import java.util.Map;

import com.romansun.hibernate.factory.DAOFactory;

public abstract class AbstractController {
	protected static DAOFactory dao = DAOFactory.getInstance();
	
	public abstract Map<String, String> getData();
	
	public abstract void setData(Map<String, String> data);
}