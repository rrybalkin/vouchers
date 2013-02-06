package com.romansun.gui.utils;

import java.io.File;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.romansun.base.StartWindow;

public class Resources {
	private static final Logger LOG = Logger.getLogger(Resources.class);
	
	private File log4jProperties;
	private File hibernateConfig;
	private File mainWindowFXML;
	private File firstTabFXML;
	private File secondTabFXML;
	private File thirdTabFXML;
	private File icon;
	
	{
		log4jProperties = getFileByName("log4j.properties");
		hibernateConfig = getFileByName("hibernate.cfg.xml");
		mainWindowFXML = getFileByName("main_window.fxml");
		firstTabFXML = getFileByName("first_tab.fxml");
		secondTabFXML = getFileByName("second_tab.fxml");
		thirdTabFXML = getFileByName("third_tab.fxml");
		icon = getFileByName("icon.png");
	}
	
	public Resources() {
		
	}
	
	public File getIcon() {
		return icon;
	}

	public File getHibernateConfig() {
		return hibernateConfig;
	}

	public File getFirstTabFXML() {
		return firstTabFXML;
	}

	public File getMainWindowFXML() {
		return mainWindowFXML;
	}

	public File getSecondTabFXML() {
		return secondTabFXML;
	}

	public File getThirdTabFXML() {
		return thirdTabFXML;
	}

	public File getLog4jProperties() {
		return log4jProperties;
	}
	
	private File getFileByName(String name) {
		try {
		InputStream stream = StartWindow.class.getClassLoader()
	            .getResourceAsStream(name);
		File file = File.createTempFile(name, ".tmp");
		file.deleteOnExit();
		FileUtils.copyInputStreamToFile(stream, file);
		
		return file;
		} catch (Exception e) {
			LOG.error("Произошла обибка при загрузке ресурса " + name + ": ", e);
			return null;
		}
	}
}
