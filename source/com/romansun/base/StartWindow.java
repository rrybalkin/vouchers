package com.romansun.base;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.romansun.gui.utils.Resources;
import com.romansun.gui.viewbuilder.WindowBuilder;

public class StartWindow {

	private final static Logger LOG = Logger.getLogger(StartWindow.class);
	static {
		PropertyConfigurator.configure(new Resources().getLog4jProperties()
				.getPath());
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LOG.info("Старт программы");

		WindowBuilder window = new WindowBuilder();
		window.show();
	}
}
