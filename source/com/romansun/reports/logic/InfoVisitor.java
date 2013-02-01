package com.romansun.reports.logic;

public class InfoVisitor {
	private String FIO;
	private Integer lunches;
	private Integer dinners;
	
	public InfoVisitor() {
		
	}

	public String getFIO() {
		return FIO;
	}

	public void setFIO(String fIO) {
		FIO = fIO;
	}

	public Integer getLunches() {
		return lunches;
	}

	public void setLunches(Integer lunches) {
		this.lunches = lunches;
	}

	public Integer getDinners() {
		return dinners;
	}

	public void setDinners(Integer dinners) {
		this.dinners = dinners;
	}
	
	
}
