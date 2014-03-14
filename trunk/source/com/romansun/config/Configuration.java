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
 * @author rybalkin
 *
 */
public class Configuration {
	private static final String PATH_TO_CONFIG_FILE = "config/config.txt";
	private final static Logger LOG = Logger.getLogger(Configuration.class);
	
	/* Configuration properties */
	@Cfg(splitter = ",")
	public Integer[] YEARS_FOR_REPORTS;
	
	@Cfg(splitter = ",")
	public String[] PRINT_REPORT_FORMATS;
	
	@Cfg
	public String PATH_TO_REPORTS;
	
	@Cfg("EXCEL_TEMPLATE")
	public String pathToExcelTemplate;
	
	@Cfg("DATE_MACROS")
	public String macrosDate;
	
	@Cfg(splitter = ",")
	public String[] FULL_SET_OF_FIELDS_IN_TEMPLATE;
	
	
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
