package com.romansun.reports;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.romansun.hibernate.logic.Visitor;
import com.romansun.reports.logic.InfoVisitor;
import com.romansun.reports.logic.Report;

public class ReportBuilder {
	
	public ReportBuilder() {
		
	}
	
	public Report buildReport(List<Visitor> visitors) {
		Report report = new Report();
		// Getting attributes for report
		Integer curMonth = Calendar.MONTH;
		Integer curYear = Calendar.YEAR;
		report.setMonth(curMonth);
		report.setYear(curYear);
		
		// Getting info about visitors
		List<InfoVisitor> infoVisitors = new ArrayList<InfoVisitor>();
		for (Visitor v : visitors) {
			InfoVisitor info = new InfoVisitor();
			String FIO = v.getLastname() + " " + v.getFirstname() + " " + v.getMiddlename();
			info.setFIO(FIO);
			info.setLunches(v.getTalon().getCount_lunch());
			info.setDinners(v.getTalon().getCount_dinner());
			
			infoVisitors.add(info);
		}
		report.setVisitors(infoVisitors);
		report.setName(getName());
		
		return report;
	}
	
	private String getName() {
		StringBuilder name = new StringBuilder(100);
		name.append(Calendar.DAY_OF_MONTH);
		name.append("_");
		name.append(Calendar.MONTH);
		name.append("_");
		name.append(Calendar.YEAR);
		name.append("_");
		name.append(Calendar.HOUR);
		name.append(":");
		name.append(Calendar.MINUTE);
		name.append(".xml");
		
		return name.toString();
	}
}
