package com.romansun.base;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.romansun.config.Resources;
import com.romansun.gui.viewbuilder.WindowBuilder;

public class StartWindow {
	private final static Logger LOG = Logger.getLogger(StartWindow.class);
	static {
		File config = new File("config/log4j.properties");
		if (!config.exists()) {
			config = Resources.getInstance().getLog4jProperties();
			LOG.info("External config of log4j.properties doesn't found - using internal");
		}
		PropertyConfigurator.configure(config.getPath());
	}

	public static void main(String[] args) {
		LOG.info("Start program ...");
		
		WindowBuilder window = new WindowBuilder();
		window.show();
	}
}
