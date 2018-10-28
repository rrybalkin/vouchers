package com.romansun.printing.data;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public abstract class ReportData implements Iterator<ReportUnit> {
	List<ReportUnit> units;
	boolean extracted = false;
	private int currentIndex = 0;
	
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

		if (hasNext()) {
			return units.get(currentIndex++);
		} else {
			throw new NoSuchElementException("No more elements");
		}
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
		units.sort(comparator);
	}
	
	protected abstract void extractData();
}
