package com.rrybalkin.utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Class of configuration properties which are stored in configuration file "application.conf".
 *
 * @author Roman Rybalkin
 */
public class Configuration {
    private final static Logger LOG = Logger.getLogger(Configuration.class);

    public String pathToReports;
    public String reportNameTemplate;
    public String reportFolderTemplate;
    public String macrosDate, macrosGroup, macrosTime;
    public int excelTemplateHeaders;
    public Map<String, String> excelTemplateColumns;
    public List<String> excelTemplateStatsColumns;
    public Locale appLocale;
    public String appDatabase;

    @SuppressWarnings("unchecked")
    private Configuration() {
        LOG.info("Reading config file...");
        try {
            final Config config = ConfigFactory.load();

            this.pathToReports = config.getString("report.folder");
            this.reportNameTemplate = config.getString("report.name.template");
            this.reportFolderTemplate = config.getString("report.folderName.template");
            this.macrosDate = config.getString("report.macros.date");
            this.macrosGroup = config.getString("report.macros.group");
            this.macrosTime = config.getString("report.macros.time");

            this.excelTemplateHeaders = config.getInt("report.template.excel.headers");
            this.excelTemplateColumns = (Map<String, String>) config.getList("report.template.excel.columns").unwrapped().get(0);
            this.excelTemplateStatsColumns = config.getStringList("report.template.excel.stats");

            this.appLocale = config.hasPath("app.locale") ? new Locale(config.getString("app.locale")) : Locale.getDefault();

            this.appDatabase = config.getString("app.database");

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
