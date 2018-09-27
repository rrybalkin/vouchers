package com.romansun.utils;

import jfork.nproperty.Cfg;
import jfork.nproperty.ConfigParser;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Class of configuration properties which are stored in configuration file "config/application.conf".
 * For parsing system uses "nProperty" library.
 * @link https://code.google.com/p/jfork/
 * @author Roman Rybalkin
 */
public class Configuration {
	private final static Logger LOG = Logger.getLogger(Configuration.class);

	@Cfg(value = "PRINT_REPORT_FORMATS", splitter = ",")
	public String[] printReportFormats;
	
	@Cfg(value = "PATH_TO_REPORTS")
	public String pathToReports;
	
	@Cfg(value = "DATE_MACROS")
	public String macrosDate;
	
	@Cfg(value = "FIELDS_OF_EXEL_TEMPLATE", splitter = ",")
	public String[] fieldsOfExelTemplate;
	
	@Cfg("USE_VISITORS_CACHE")
	public boolean useVisitorsCache;
	
	{
		LOG.debug("Reading of configuration file...");
		try (Reader reader = new InputStreamReader(Resources.getInstance().getResourceInputStream(Resources.APP_CONFIG), "UTF-8")) {
			ConfigParser.parse(this, reader, "application.conf");
            LOG.debug("Reading of configuration file... Done");
		} catch (Exception e) {
			LOG.error("Reading of configuration file... Failed", e);
		}
	}

    private Configuration() {}

    private static final Configuration INSTANCE = new Configuration();

    public static Configuration getInstance() {
        return INSTANCE;
    }
}
