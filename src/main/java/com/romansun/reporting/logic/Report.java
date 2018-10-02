package com.romansun.reporting.logic;

import java.io.File;
import java.util.List;

public class Report {
	private Integer month;
	private Integer year;
	private String name;
	private File file;
	private List<InfoVisitor> visitors;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	@Override
	public String toString() {
		return name;
	}
	
}
