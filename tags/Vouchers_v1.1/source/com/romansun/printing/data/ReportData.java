package com.romansun.printing.data;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public abstract class ReportData implements Iterator<ReportUnit> {
	protected List<ReportUnit> units;
	protected boolean extracted = false;
	private int currentIndex = 0;
	
	public ReportData() {}
	
	@Override
	public boolean hasNext() {
		if (!extracted) {
			extractData();
		}
		return currentIndex < units.size();
	}

	@Override
	public ReportUnit next() {
		if (!extracted) {
			extractData();
		}
		return units.get(currentIndex++);
	}

	@Override
	public void remove() {
		if (!extracted) {
			extractData();
		}
		units.remove(currentIndex);
	}
	
	public void reset() {
		currentIndex = -1;
	}
	
	public void sort(Comparator<? super ReportUnit> comparator) {
		if (!extracted) {
			extractData();
		}
		Collections.sort(units, comparator);
	}
	
	protected abstract void extractData();
}
