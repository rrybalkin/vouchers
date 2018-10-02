package com.romansun.utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * Class of configuration properties which are stored in configuration file "application.conf".
 *
 * @author Roman Rybalkin
 */
public class Configuration {
    private final static Logger LOG = Logger.getLogger(Configuration.class);

    public boolean useVisitorsCache;
    public List<String> printReportFormats;
    public String pathToReports;
    public String reportNameTemplate;
    public String macrosDate;
    public int xlsTemplateHeaders;
    public Map<String, String> xlsTemplateColumns;
    public List<String> xlsTemplateStatsColumns;

    @SuppressWarnings("unchecked")
    private Configuration() {
        LOG.info("Reading config file...");
        try {
            final Config config = ConfigFactory.load();

            this.useVisitorsCache = config.getBoolean("cache.enable");

            this.printReportFormats = config.getStringList("report.formats");
            this.pathToReports = config.getString("report.folder");
            this.reportNameTemplate = config.getString("report.name.template");
            this.macrosDate = config.getString("report.macros.date");

            this.xlsTemplateHeaders = config.getInt("report.template.xls.headers");
            this.xlsTemplateColumns = (Map<String, String>) config.getList("report.template.xls.columns").unwrapped().get(0);
            this.xlsTemplateStatsColumns = config.getStringList("report.template.xls.stats");

            LOG.info("Reading config file... Done");
        } catch (Exception e) {
            LOG.error("Reading config file... Failed", e);
            throw new IllegalStateException("Application config failed to load");
        }
    }

    private static final Configuration INSTANCE = new Configuration();

    public static Configuration getInstance() {
        return INSTANCE;
    }
}
