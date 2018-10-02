package com.romansun.printing.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.romansun.reporting.logic.InfoVisitor;
import com.romansun.reporting.logic.Report;

public class StoredReportData extends ReportData {
	private final static Logger LOG = Logger.getLogger(StoredReportData.class);
	private Report report;
	private double priceOfLunch, priceOfDinner;
	private boolean ignoreEmptyVisitors = false;
	
	public StoredReportData(Report report, double priceOfLunch, double priceOfDinner, boolean ignoreEmptyVisitors) {
		super();
		LOG.info("'StoredReportData' constructor ...");
		this.report = report;
		this.priceOfLunch = priceOfLunch;
		this.priceOfDinner = priceOfDinner;
		this.ignoreEmptyVisitors = ignoreEmptyVisitors;
	}

	@Override
	protected void extractData() {
		LOG.debug("Extract data for stored report = " + report.getName() + " ...");
		LOG.debug("Ignore Empty Visitors = " + ignoreEmptyVisitors);
		super.units = new ArrayList<>();
		
		List<InfoVisitor> visitors = report.getVisitors();
		for (InfoVisitor visitor : visitors) {
			ReportUnit unit = new ReportUnit();

			if (visitor.validate()) {
				unit.setVisitorFirstname(visitor.getFirstName());
				unit.setVisitorLastname(visitor.getLastName());
				unit.setVisitorMiddlename(visitor.getMiddleName());
				unit.setVisitorGroupName(visitor.getGroup());

				int countOfLunches = visitor.getLunches();
				int countOfDinners = visitor.getDinners();
				if (ignoreEmptyVisitors && (countOfLunches == 0 && countOfDinners == 0)) {
					LOG.debug("Visitor = " + visitor.getFIO() + " doesn't have any lunches or dinners.");
					continue;
				}
				
				double costOfLunches = countOfLunches * priceOfLunch;
				double costOfDinners = countOfDinners * priceOfDinner;
				double generalCost = costOfLunches + costOfDinners;
				unit.setCountOfLunches(countOfLunches);
				unit.setCountOfDinners(countOfDinners);
				unit.setCostOfLunches(costOfLunches);
				unit.setCostOfDinners(costOfDinners);
				unit.setGeneralCost(generalCost);
				
				units.add(unit);
			} else {
				LOG.warn("Information about visitor = " + visitor.getFIO() + " is incorrect!");
			}
		}
		
		LOG.debug("Data from stored report = " + report.getName() + " were successfully extracted.");
		super.extracted = true;
	}
}
