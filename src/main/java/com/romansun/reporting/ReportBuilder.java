package com.romansun.reporting;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.joda.time.DateTime;

import com.romansun.hibernate.entity.Visitor;
import com.romansun.reporting.logic.InfoVisitor;
import com.romansun.reporting.logic.Report;

public class ReportBuilder {
	
	public Report buildReport(List<Visitor> visitors) {
		Report report = new Report();
		// Getting attributes for report
		Integer curMonth = today().getMonthOfYear();
		Integer curYear = today().getYear();
		report.setMonth(curMonth);
		report.setYear(curYear);

		// Getting info about visitors
		List<InfoVisitor> infoVisitors = new ArrayList<>();
		for (Visitor v : visitors) {
			InfoVisitor info = new InfoVisitor(
					v.getFirstName(),
					v.getLastName(),
					v.getMiddleName(),
					v.getAssociation().getName(),
					v.getTalon().getLunches(),
					v.getTalon().getDinners()
			);
			infoVisitors.add(info);
		}
		report.setVisitors(infoVisitors);
		report.setName(getName());

		return report;
	}

	private String getName() {
		final DateTime dt = today();
		return String.valueOf(dt.getDayOfMonth()) +
				"_" +
				dt.monthOfYear().getAsText(new Locale("ru")) +
				"_" +
				dt.getYear() +
				"_" +
				dt.getHourOfDay() +
				"-" +
				dt.getMinuteOfHour() +
				"-" +
				dt.getSecondOfMinute() +
				".xml";
	}

	private static DateTime today() {
		return new DateTime();
	}
}
