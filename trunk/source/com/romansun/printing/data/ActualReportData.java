package com.romansun.printing.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.romansun.hibernate.logic.Visitor;

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
		super.units = new ArrayList<ReportUnit>();
		
		for (Visitor visitor : visitors) {
			int countOfLunches = visitor.getTalon().getCount_lunch();
			int countOfDinners = visitor.getTalon().getCount_dinner();
			
			if (ignoreEmptyVisitors && (countOfLunches == 0 && countOfDinners == 0)) {
				LOG.debug("Visitor = " + visitor.getLastname() + " " + visitor.getFirstname() + " doesn't have any lunches or dinners.");
				continue;
			}
			
			ReportUnit unit = new ReportUnit();
			unit.setVisitorFirstname(visitor.getFirstname());
			unit.setVisitorLastname(visitor.getLastname());
			
			String middlename = visitor.getMiddlename();
			if (!"?".equals(middlename)) {
				unit.setVisitorMiddlename(middlename);
			}
			
			String groupName = visitor.getAssociation().getName();
			if (!"Без группы".equals(groupName)) {
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
