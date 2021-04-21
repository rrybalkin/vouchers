package com.rrybalkin.printing.data;

import com.rrybalkin.reporting.logic.InfoVisitor;
import com.rrybalkin.reporting.logic.Report;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class StoredReportData extends ReportData {
	private final static Logger LOG = Logger.getLogger(StoredReportData.class);

	private Report report;
	private boolean includeEmptyVisitors;
	
	public StoredReportData(Report report, boolean includeEmptyVisitors) {
		super();
		LOG.info("StoredReportData constructor ...");
		this.report = report;
		this.includeEmptyVisitors = includeEmptyVisitors;
	}

	@Override
	protected void extractData() {
		LOG.info("Extract data for stored report = " + report.getName() + " ...");
		LOG.info("includeEmptyVisitors = " + includeEmptyVisitors);
		units = new ArrayList<>();
		
		List<InfoVisitor> visitors = report.getVisitors();
		for (InfoVisitor v : visitors) {
			ReportUnit unit = new ReportUnit();

			if (v.validate()) {
				unit.setFirstName(v.getFirstName());
				unit.setLastName(v.getLastName());
				unit.setMiddleName(v.getMiddleName());
				unit.setGroup(v.getGroup());

				int countOfBreakfasts = v.getBreakfasts();
				int countOfLunches = v.getLunches();
				int countOfDinners = v.getDinners();
				if ((countOfBreakfasts == 0 && countOfLunches == 0 && countOfDinners == 0)
						&& !includeEmptyVisitors) {
					LOG.debug("Visitor " + v.getFIO() + " doesn't have breakfasts/lunches/dinners - ignore!");
					continue;
				}

				unit.setBreakfasts(countOfBreakfasts);
				unit.setLunches(countOfLunches);
				unit.setDinners(countOfDinners);
				
				units.add(unit);
			} else {
				LOG.warn("Information about visitor = " + v.getFIO() + " is incorrect!");
			}
		}
		
		LOG.info("Data from stored report = " + report.getName() + " were successfully extracted.");
		extracted = true;
	}
}
