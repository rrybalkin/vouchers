package com.romansun.gui.controller;

import java.io.File;
import java.util.HashMap;
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
 * Class Converter for ComboBox "Month"
 */
	protected static class MonthConverter extends StringConverter<Integer> {

		private static final Map<String, Integer> months = new HashMap<String, Integer>();
		static {
			months.put("Январь", 1);
			months.put("Февраль", 2);
			months.put("Март", 3);
			months.put("Апрель", 4);
			months.put("Май", 5);
			months.put("Июнь", 6);
			months.put("Июль", 7);
			months.put("Август", 8);
			months.put("Сентябрь", 9);
			months.put("Октябрь", 10);
			months.put("Ноябрь", 11);
			months.put("Декабрь", 12);
		}

		public MonthConverter() {}

		@Override
		public Integer fromString(String month) {
			return months.get(month);
		}

		@Override
		public String toString(Integer month) {
			return getMonthNameByIndex(month);
		}

		public static String getMonthNameByIndex(Integer monthIndex) {
			for (String key : months.keySet()) {
				if (monthIndex.equals(months.get(key)))
					return key;
			}
			return null;
		}
	}

	/*
	 * Class Converter for ComboBox "Year"
	 */
	protected static class YearConverter extends StringConverter<Integer> {

		public YearConverter() {}

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