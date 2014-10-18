package com.romansun.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import jfork.nproperty.Cfg;
import jfork.nproperty.ConfigParser;

import org.apache.log4j.Logger;

/**
 * Class of configuration properties which are stored in configuration file "config/config.txt".
 * For parsing system uses "nProperty" library.
 * @link https://code.google.com/p/jfork/
 * @author Roman Rybalkin
 *
 */
public class Configuration {
	public static final String PATH_TO_CONFIG_FILE = "config/config.txt";
	private final static Logger LOG = Logger.getLogger(Configuration.class);
	
	/* Configuration properties */
	@Cfg(splitter = ",")
	public Integer[] YEARS_FOR_REPORTS;
	
	@Cfg(splitter = ",")
	public String[] PRINT_REPORT_FORMATS;
	
	@Cfg
	public String PATH_TO_REPORTS;
	
	@Cfg("PATH_TO_XLS_TEMPLATE")
	public String pathToXLSTemplate;
	@Cfg("PATH_TO_XLSX_TEMPLATE")
	public String pathToXLSXTemplate;
	@Cfg("PATH_TO_DOC_TEMPLATE")
	public String pathToDOCTemplate;
	@Cfg("PATH_TO_DOCX_TEMPLATE")
	public String pathToDOCSTemplate;
	
	@Cfg("DATE_MACROS")
	public String macrosDate;
	
	@Cfg(splitter = ",")
	public String[] FIELDS_OF_EXEL_TEMPLATE;
	
	@Cfg("USE_VISITORS_CACHE")
	public boolean useVisitorsCache;
	
	
	/* Realization of Singleton */
	private Configuration() {
		LOG.debug("Reading of configuration file 'config.txt' ...");
		try {
			Reader reader = new InputStreamReader(new FileInputStream(new File(PATH_TO_CONFIG_FILE)), "UTF-8");
			ConfigParser.parse(this, reader, "config.txt");
		} catch (Exception e) {
			LOG.error("Error while reading configuration file: ", e);
		}
	}
	
	private static Configuration instance = new Configuration();
	public static Configuration getInstance() {
		return instance;
	}
}
