package com.romansun.utils;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Resources {
    private static final Logger LOG = Logger.getLogger(Resources.class);

    public static final String APP_CONFIG = "config/application.conf";
    public static final String LOG4J_CONFIG = "config/log4j.properties";
    public static final String HIBERNATE_CONFIG = "config/hibernate.cfg.xml";

    public static final String MAIN_WINDOW_FXML = "fxml/main_window.fxml";
    public static final String FIRST_TAB_FXML = "fxml/first_tab.fxml";
    public static final String SECOND_TAB_FXML = "fxml/second_tab.fxml";
    public static final String THIRD_TAB_FXML = "fxml/third_tab.fxml";
    public static final String FOURTH_TAB_FXML = "fxml/fourth_tab.fxml";
    public static final String SETTINGS_WINDOW_FXML = "fxml/settings_window.fxml";

    public static final String ICON_IMAGE = "images/icon.png";

    public static final String XLS_REPORT_TEMPLATE = "templates/excel_template_full.xls";
    public static final String XLSX_REPORT_TEMPLATE = "templates/excel_template_full.xlsx";

    private ConcurrentMap<String, File> resources = new ConcurrentHashMap<>();

    public File getResource(String name) {
        if (resources.containsKey(name)) {
            return resources.get(name);
        }
        // loading a resource
        try {
            InputStream stream = this.getClass().getClassLoader().getResourceAsStream(name);
            File file = File.createTempFile(name, ".tmp");
            file.deleteOnExit();
            if (stream != null) {
                IOUtils.copy(stream, new FileOutputStream(file));
            } else {
                throw new FileNotFoundException(name);
            }
            resources.putIfAbsent(name, file);
            return file;
        } catch (Exception e) {
            LOG.error("Error while loading resource by name=" + name + ": ", e);
            throw new IllegalStateException("Error while loading resource by name=" + name, e);
        }
    }

    public InputStream getResourceInputStream(String name) {
        try {
            return this.getClass().getClassLoader().getResourceAsStream(name);
        } catch (Exception e) {
            LOG.error("Error while loading resource stream by name=" + name + ": ", e);
            throw new IllegalStateException("Error while loading resource stream by name=" + name, e);
        }
    }

    private Resources() {}

    private static final Resources INSTANCE = new Resources();

    public static Resources getInstance() {
        return INSTANCE;
    }
}
