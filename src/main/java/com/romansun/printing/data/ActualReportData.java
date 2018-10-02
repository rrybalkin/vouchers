package com.romansun.printing.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.romansun.hibernate.entity.Visitor;

public class ActualReportData extends ReportData {
	private final static Logger LOG = Logger.getLogger(StoredReportData.class);
	private List<Visitor> visitors;
	private double priceOfLunch, priceOfDinner;
	private boolean ignoreEmptyVisitors;
	
	public ActualReportData(List<Visitor> visitors, double priceOfLunch, double priceOfDinner, boolean ignoreEmptyVisitors) {
		super();
		LOG.info("'ActualReportData' constructor ...");
		this.visitors = visitors;
		this.priceOfLunch = priceOfLunch;
		this.priceOfDinner = priceOfDinner;
		this.ignoreEmptyVisitors = ignoreEmptyVisitors;
	}
	
	@Override
	protected void extractData() {
		LOG.debug("Extract data for actual info about visitors ...");
		LOG.debug("Ignore Empty Visitors = " + ignoreEmptyVisitors);
		super.units = new ArrayList<>();
		
		for (Visitor visitor : visitors) {
			int countOfLunches = visitor.getTalon().getLunches();
			int countOfDinners = visitor.getTalon().getDinners();
			
			if (ignoreEmptyVisitors && (countOfLunches == 0 && countOfDinners == 0)) {
				LOG.debug("Visitor = " + visitor.getLastName() + " " + visitor.getFirstName() + " doesn't have any lunches or dinners.");
				continue;
			}
			
			ReportUnit unit = new ReportUnit();
			unit.setVisitorFirstname(visitor.getFirstName());
			unit.setVisitorLastname(visitor.getLastName());
			
			String middlename = visitor.getMiddleName();
			if (!"?".equals(middlename)) {
				unit.setVisitorMiddlename(middlename);
			}
			
			String groupName = visitor.getAssociation().getName();
			if (!"Without group".equals(groupName)) {
				unit.setVisitorGroupName(groupName);
			}
			
			unit.setCountOfLunches(countOfLunches);
			unit.setCountOfDinners(countOfDinners);
			
			double costOfLunches = countOfLunches * priceOfLunch; 
			unit.setCostOfLunches(costOfLunches);
			double costOfDinners = countOfDinners * priceOfDinner; 
			unit.setCostOfDinners(costOfDinners);
			double generalCost = costOfLunches + costOfDinners;
			unit.setGeneralCost(generalCost);
			
			units.add(unit);
		}
		
		LOG.debug("Actual Data were successfully extracted.");
		super.extracted = true;
	}

}
