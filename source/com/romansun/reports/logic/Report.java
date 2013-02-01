package com.romansun.reports.logic;

import java.util.List;

public class Report {
	
	private Integer month;
	private Integer year;
	private List<InfoVisitor> visitors;
	
	public Report() { 
		
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public List<InfoVisitor> getVisitors() {
		return visitors;
	}

	public void setVisitors(List<InfoVisitor> visitors) {
		this.visitors = visitors;
	}
	
	
}
