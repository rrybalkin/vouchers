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
    public String pathToReports;
    public String reportNameTemplate;
    public String macrosDate;
    public int excelTemplateHeaders;
    public Map<String, String> excelTemplateColumns;
    public List<String> excelTemplateStatsColumns;

    @SuppressWarnings("unchecked")
    private Configuration() {
        LOG.info("Reading config file...");
        try {
            final Config config = ConfigFactory.load();

            this.useVisitorsCache = config.getBoolean("cache.enable");

            this.pathToReports = config.getString("report.folder");
            this.reportNameTemplate = config.getString("report.name.template");
            this.macrosDate = config.getString("report.macros.date");

            this.excelTemplateHeaders = config.getInt("report.template.excel.headers");
            this.excelTemplateColumns = (Map<String, String>) config.getList("report.template.excel.columns").unwrapped().get(0);
            this.excelTemplateStatsColumns = config.getStringList("report.template.excel.stats");

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
