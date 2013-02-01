package com.romansun.reports;

import java.util.ArrayList;
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
		Integer curMonth = new Date().getMonth();
		Integer curYear = new Date().getYear();
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
		
		return report;
	}
}
