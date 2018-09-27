package com.romansun;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.romansun.utils.Resources;
import com.romansun.gui.WindowBuilder;

public class App {
	private final static Logger LOG = Logger.getLogger(App.class);
	static {
		PropertyConfigurator.configure(Resources.getInstance().getResource(Resources.LOG4J_CONFIG).getPath());
	}

	public static void main(String[] args) {
		LOG.info("Start Application");
		
		WindowBuilder window = new WindowBuilder();
		window.show();
	}
}
