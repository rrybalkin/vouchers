package com.romansun.printing.data;

import com.romansun.reporting.logic.InfoVisitor;
import com.romansun.reporting.logic.Report;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class StoredReportData extends ReportData {
	private final static Logger LOG = Logger.getLogger(StoredReportData.class);

	private Report report;
	private double priceOfLunch, priceOfDinner;
	private boolean includeEmptyVisitors;
	
	public StoredReportData(Report report, double priceOfLunch, double priceOfDinner, boolean includeEmptyVisitors) {
		super();
		LOG.info("StoredReportData constructor ...");
		this.report = report;
		this.priceOfLunch = priceOfLunch;
		this.priceOfDinner = priceOfDinner;
		this.includeEmptyVisitors = includeEmptyVisitors;
	}

	@Override
	protected void extractData() {
		LOG.info("Extract data for stored report = " + report.getName() + " ...");
		LOG.info("includeEmptyVisitors = " + includeEmptyVisitors);
		units = new ArrayList<>();
		
		List<InfoVisitor> visitors = report.getVisitors();
		for (InfoVisitor visitor : visitors) {
			ReportUnit unit = new ReportUnit();

			if (visitor.validate()) {
				unit.setFirstName(visitor.getFirstName());
				unit.setLastName(visitor.getLastName());
				unit.setMiddleName(visitor.getMiddleName());
				unit.setGroup(visitor.getGroup());

				int countOfLunches = visitor.getLunches();
				int countOfDinners = visitor.getDinners();
				if ((countOfLunches == 0 && countOfDinners == 0) && !includeEmptyVisitors) {
					LOG.debug("Visitor = " + visitor.getFIO() + " doesn't have lunches or dinners - ignore!");
					continue;
				}
				
				double costOfLunches = countOfLunches * priceOfLunch;
				double costOfDinners = countOfDinners * priceOfDinner;
				double generalCost = costOfLunches + costOfDinners;
				unit.setLunches(countOfLunches);
				unit.setDinners(countOfDinners);
				unit.setLunchPrice(costOfLunches);
				unit.setDinnerPrice(costOfDinners);
				unit.setTotalCost(generalCost);
				
				units.add(unit);
			} else {
				LOG.warn("Information about visitor = " + visitor.getFIO() + " is incorrect!");
			}
		}
		
		LOG.info("Data from stored report = " + report.getName() + " were successfully extracted.");
		extracted = true;
	}
}
