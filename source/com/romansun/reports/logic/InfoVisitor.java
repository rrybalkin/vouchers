package com.romansun.reports.logic;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class InfoVisitor {
	private final SimpleStringProperty FIO;
	private final SimpleIntegerProperty lunches;
	private final SimpleIntegerProperty dinners;
	
	public InfoVisitor(String fio, Integer lunches, Integer dinners) {
		this.FIO = new SimpleStringProperty(fio);
		this.lunches = new SimpleIntegerProperty(lunches);
		this.dinners = new SimpleIntegerProperty(dinners);
	}

	public String getFIO() {
		return FIO.get();
	}

	public void setFIO(String fio) {
		FIO.set(fio);
	}

	public Integer getLunches() {
		return lunches.getValue();
	}

	public void setLunches(Integer lunches) {
		this.lunches.set(lunches);
	}

	public Integer getDinners() {
		return dinners.getValue();
	}

	public void setDinners(Integer dinners) {
		this.dinners.set(dinners);
	}	
}
