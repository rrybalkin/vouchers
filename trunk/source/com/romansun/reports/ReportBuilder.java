package com.romansun.reports;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.joda.time.DateTime;

import com.romansun.hibernate.logic.Visitor;
import com.romansun.reports.logic.InfoVisitor;
import com.romansun.reports.logic.Report;

public class ReportBuilder {
	
	public Report buildReport(List<Visitor> visitors) {
		Report report = new Report();
		// Getting attributes for report
		Integer curMonth = new DateTime().getMonthOfYear();
		Integer curYear = new DateTime().getYear();
		report.setMonth(curMonth);
		report.setYear(curYear);

		// Getting info about visitors
		List<InfoVisitor> infoVisitors = new ArrayList<InfoVisitor>();
		for (Visitor v : visitors) {
			String FIO = v.getLastname() + " " + v.getFirstname() + " "
					+ v.getMiddlename() + " (" + v.getAssociation().getName()
					+ ")";
			Integer lunches = v.getTalon().getCount_lunch();
			Integer dinners = v.getTalon().getCount_dinner();
			InfoVisitor info = new InfoVisitor(FIO, lunches, dinners);

			infoVisitors.add(info);
		}
		report.setVisitors(infoVisitors);
		report.setName(getName());

		return report;
	}

	private String getName() {
		StringBuilder name = new StringBuilder(100);
		DateTime dt = new DateTime();
		name.append(dt.getDayOfMonth());
		name.append("_");
		name.append(dt.monthOfYear().getAsText(new Locale("ru")));
		name.append("_");
		name.append(dt.getYear());
		name.append("_");
		name.append(dt.getHourOfDay());
		name.append("-");
		name.append(dt.getMinuteOfHour());
		name.append("-");
		name.append(dt.getSecondOfMinute());
		name.append(".xml");

		return name.toString();
	}
}
