package com.rrybalkin.utils;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Resources {
    private static final Logger LOG = Logger.getLogger(Resources.class);

    public static final String LOG4J_CONFIG = "config/log4j.properties";
    public static final String HIBERNATE_ORACLE_CONFIG = "config/hibernate.oracle.xml";
    public static final String HIBERNATE_MYSQL_CONFIG = "config/hibernate.mysql.xml";

    public static final String MAIN_WINDOW_FXML = "fxml/main_window.fxml";
    public static final String FIRST_TAB_FXML = "fxml/first_tab.fxml";
    public static final String SECOND_TAB_FXML = "fxml/second_tab.fxml";
    public static final String THIRD_TAB_FXML = "fxml/third_tab.fxml";
    public static final String FOURTH_TAB_FXML = "fxml/fourth_tab.fxml";

    public static final String ICON_IMAGE = "images/icon.png";

    public static final String XLS_REPORT_TEMPLATE = "templates/excel_template_full.xls";
    public static final String XLSX_REPORT_TEMPLATE = "templates/excel_template_full.xlsx";

    private static final ConcurrentMap<String, File> RESOURCES = new ConcurrentHashMap<>();

    public static File getResource(String name) {
        if (RESOURCES.containsKey(name)) {
            return RESOURCES.get(name);
        }
        // loading a resource
        try {
            InputStream stream = Resources.class.getClassLoader().getResourceAsStream(name);
            File file = File.createTempFile(name, ".tmp");
            file.deleteOnExit();
            if (stream != null) {
                IOUtils.copy(stream, new FileOutputStream(file));
            } else {
                throw new FileNotFoundException(name);
            }
            RESOURCES.put(name, file);
            return file;
        } catch (IOException e) {
            LOG.error("Error while loading resource by name=" + name + ": ", e);
            throw new IllegalStateException("Error while loading resource by name=" + name, e);
        }
    }

    private Resources() {}
}
