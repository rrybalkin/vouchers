package com.romansun.utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Class of configuration properties which are stored in configuration file "application.conf".
 *
 * @author Roman Rybalkin
 */
public class Configuration {
    private final static Logger LOG = Logger.getLogger(Configuration.class);

    public List<String> printReportFormats;
    public String pathToReports;
    public String macrosDate;
    public List<String> fieldsOfExcelTemplate;
    public boolean useVisitorsCache;

    private Configuration() {
        LOG.debug("Reading config file...");
        try {
            final Config config = ConfigFactory.load();
            this.printReportFormats = config.getStringList("report.formats");
            this.pathToReports = config.getString("report.folder");
            this.macrosDate = config.getString("report.macros.data");
            this.fieldsOfExcelTemplate = config.getStringList("report.template.xls.fields");
            this.useVisitorsCache = config.getBoolean("cache.enable");
            LOG.debug("Reading config file... Done");
        } catch (Exception e) {
            LOG.error("Reading config file... Failed", e);
        }
    }

    private static final Configuration INSTANCE = new Configuration();

    public static Configuration getInstance() {
        return INSTANCE;
    }
}
