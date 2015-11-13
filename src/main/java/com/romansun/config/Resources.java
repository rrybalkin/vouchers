package com.romansun.config;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

public class Resources {
    private static final Logger LOG = Logger.getLogger(Resources.class);

    private File log4jProperties;
    private File hibernateConfig;
    private File mainWindowFXML;
    private File firstTabFXML;
    private File secondTabFXML;
    private File thirdTabFXML;
    private File fourthTabFXML;
    private File settingsWindowFXML;
    private File icon;

    {
        log4jProperties = getFileByName("log4j.properties");
        hibernateConfig = getFileByName("hibernate.cfg.xml");
        mainWindowFXML = getFileByName("main_window.fxml");
        firstTabFXML = getFileByName("first_tab.fxml");
        secondTabFXML = getFileByName("second_tab.fxml");
        thirdTabFXML = getFileByName("third_tab.fxml");
        fourthTabFXML = getFileByName("fourth_tab.fxml");
        settingsWindowFXML = getFileByName("settings_window.fxml");
        icon = getFileByName("icon.png");
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

    public File getFourthTabFXML() {
        return fourthTabFXML;
    }

    public File getSettingsWindowFXML() {
        return settingsWindowFXML;
    }

    public File getLog4jProperties() {
        return log4jProperties;
    }

    private File getFileByName(String name) {
        try {
            InputStream stream = this.getClass().getClassLoader().getResourceAsStream(name);
            File file = File.createTempFile(name, ".tmp");
            file.deleteOnExit();
            if (stream != null) {
                IOUtils.copy(stream, new FileOutputStream(file));
            } else {
                throw new FileNotFoundException(name);
            }
            return file;
        } catch (Exception e) {
            LOG.error("Error while loading resource by name=" + name + ": ", e);
            return null;
        }
    }

    private Resources() {
    }

    private static Resources instance = new Resources();

    public static Resources getInstance() {
        return instance;
    }
}
