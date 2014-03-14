package com.romansun.printing.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.romansun.reports.logic.InfoVisitor;
import com.romansun.reports.logic.Report;

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
		super.units = new ArrayList<ReportUnit>();
		
		List<InfoVisitor> visitors = report.getVisitors();
		int num = 1;
		for (InfoVisitor visitor : visitors) {
			ReportUnit unit = new ReportUnit();
			String[] fio = visitor.getFIO().replace("Без группы", "Без_группы").split(" ");

			if (fio != null && fio.length == 4) {
				String lastname = fio[0];
				String firstname = fio[1];
				String middlename = fio[2];
				String groupName = fio[3];
				
				int countOfLunches = visitor.getLunches();
				int countOfDinners = visitor.getDinners();
				if (ignoreEmptyVisitors && (countOfLunches == 0 && countOfDinners == 0)) {
					LOG.debug("Visitor = " + visitor.getFIO() + " doesn't have any lunches or dinners.");
					continue;
				}
				
				double costOfLunches = countOfLunches * priceOfLunch;
				double costOfDinners = countOfDinners * priceOfDinner;
				double generalCost = costOfLunches + costOfDinners;
				
				unit.setNumber(num);
				unit.setVisitorFirstname(firstname);
				unit.setVisitorLastname(lastname);
				if (middlename != null && !"?".equals(middlename)) {
					unit.setVisitorMiddlename(middlename);
				}
				if (groupName != null && !"(Без_группы)".equals(groupName)) {
					unit.setVisitorGroupName(groupName.substring(1, groupName.length()-1));
				}
				
				unit.setCountOfLunches(countOfLunches);
				unit.setCountOfDinners(countOfDinners);
				unit.setCostOfLunches(costOfLunches);
				unit.setCostOfDinners(costOfDinners);
				unit.setGeneralCost(generalCost);
				
				units.add(unit);
				num++;
			} else {
				LOG.debug("Information about visitor = " + visitor.getFIO() + " is null or incorrect!");
			}
		}
		
		LOG.debug("Data from stored report = " + report.getName() + " were successfully extracted.");
		super.extracted = true;
	}
}
