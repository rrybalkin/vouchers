package com.romansun.gui.controller;

import java.io.File;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javafx.scene.control.TabPane;

import com.romansun.cache.impl.VisitorsCacheImpl;
import com.romansun.utils.Configuration;
import com.romansun.hibernate.dao.VisitorDAO;
import com.romansun.hibernate.factory.DAOFactory;
import javafx.util.StringConverter;
import org.apache.log4j.Logger;

public abstract class AbstractController {
	private static final Logger LOG = Logger.getLogger(AbstractController.class);

	protected final static String PATH_TO_REPORTS = "reports";
	protected final static Configuration config = Configuration.getInstance();
	
	protected static VisitorDAO visitorsDAO;
	protected static DAOFactory daoFactory;
	protected static TabPane mainTabPane;
	
	static {
		File reports = new File(PATH_TO_REPORTS);
		if (!reports.exists()) {
			boolean created = reports.mkdir();
			LOG.info("Reports folder created: " + created);
		}
		daoFactory = DAOFactory.getInstance();
		visitorsDAO = config.useVisitorsCache ? new VisitorsCacheImpl() : daoFactory.getVisitorDAO();
	}

	/*
	 * A month converter, name -> digit.
	 */
	public static class MonthConverter extends StringConverter<Integer> {

		private static final Map<String, Integer> MONTHS = new HashMap<>();
		static {
			MONTHS.put(Month.JANUARY.getDisplayName(TextStyle.FULL, Locale.getDefault()), 1);
			MONTHS.put(Month.FEBRUARY.getDisplayName(TextStyle.FULL, Locale.getDefault()), 2);
			MONTHS.put(Month.MARCH.getDisplayName(TextStyle.FULL, Locale.getDefault()), 3);
			MONTHS.put(Month.APRIL.getDisplayName(TextStyle.FULL, Locale.getDefault()), 4);
			MONTHS.put(Month.MAY.getDisplayName(TextStyle.FULL, Locale.getDefault()), 5);
			MONTHS.put(Month.JUNE.getDisplayName(TextStyle.FULL, Locale.getDefault()), 6);
			MONTHS.put(Month.JULY.getDisplayName(TextStyle.FULL, Locale.getDefault()), 7);
			MONTHS.put(Month.AUGUST.getDisplayName(TextStyle.FULL, Locale.getDefault()), 8);
			MONTHS.put(Month.SEPTEMBER.getDisplayName(TextStyle.FULL, Locale.getDefault()), 9);
			MONTHS.put(Month.OCTOBER.getDisplayName(TextStyle.FULL, Locale.getDefault()), 10);
			MONTHS.put(Month.NOVEMBER.getDisplayName(TextStyle.FULL, Locale.getDefault()), 11);
			MONTHS.put(Month.DECEMBER.getDisplayName(TextStyle.FULL, Locale.getDefault()), 12);
		}

		@Override
		public Integer fromString(String month) {
			return MONTHS.get(month);
		}

		@Override
		public String toString(Integer month) {
			return getMonthNameByIndex(month);
		}

		public static String getMonthNameByIndex(Integer monthIndex) {
			for (String key : MONTHS.keySet()) {
				if (monthIndex.equals(MONTHS.get(key)))
					return key;
			}
			throw new IllegalArgumentException("Month by index=" + monthIndex + " not found");
		}
	}

	/*
	 * A year converter, string -> int.
	 */
	public static class YearConverter extends StringConverter<Integer> {

		@Override
		public Integer fromString(String month) {
			return Integer.parseInt(month);
		}

		@Override
		public String toString(Integer month) {
			return month.toString();
		}
	}
}