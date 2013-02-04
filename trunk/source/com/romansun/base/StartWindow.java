package com.romansun.base;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.romansun.gui.viewbuilder.WindowBuilder;

public class StartWindow {
	
	private final static Logger LOG = Logger.getLogger(StartWindow.class);
	static {
		// Set path to file-property for log4j
		PropertyConfigurator.configure("config/log4j.properties");
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LOG.info("����� ���������");
		
		WindowBuilder window = new WindowBuilder();
		window.show();
	}
}