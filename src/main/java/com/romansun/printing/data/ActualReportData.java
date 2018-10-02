package com.romansun.printing.data;

import com.romansun.hibernate.entity.Association;
import com.romansun.hibernate.entity.Visitor;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ActualReportData extends ReportData {
	private final static Logger LOG = Logger.getLogger(StoredReportData.class);
	private List<Visitor> visitors;
	private double priceOfLunch, priceOfDinner;
	private boolean includeEmptyVisitors;
	
	public ActualReportData(List<Visitor> visitors, double priceOfLunch, double priceOfDinner, boolean includeEmptyVisitors) {
		super();
		LOG.info("ActualReportData constructor ...");
		this.visitors = visitors;
		this.priceOfLunch = priceOfLunch;
		this.priceOfDinner = priceOfDinner;
		this.includeEmptyVisitors = includeEmptyVisitors;
	}
	
	@Override
	protected void extractData() {
		LOG.info("Extract data for actual info about visitors ...");
		LOG.info("includeEmptyVisitors = " + includeEmptyVisitors);
		units = new ArrayList<>();
		
		for (Visitor visitor : visitors) {
			int countOfLunches = visitor.getTalon().getLunches();
			int countOfDinners = visitor.getTalon().getDinners();
			
			if ((countOfLunches == 0 && countOfDinners == 0) && !includeEmptyVisitors) {
				LOG.debug("Visitor = " + visitor.getFIO() + " doesn't have lunches or dinners - ignore!");
				continue;
			}
			
			ReportUnit unit = new ReportUnit();
			unit.setFirstName(visitor.getFirstName());
			unit.setLastName(visitor.getLastName());
			
			String middleName = visitor.getMiddleName();
			if (StringUtils.isNotEmpty(middleName) && !"?".equals(middleName)) {
				unit.setMiddleName(middleName);
			}
			
			String groupName = visitor.getAssociation().getName();
			if (!Association.NO_GROUP.equals(groupName)) {
				unit.setGroup(groupName);
			}
			
			unit.setLunches(countOfLunches);
			unit.setDinners(countOfDinners);
			
			double costOfLunches = countOfLunches * priceOfLunch; 
			unit.setLunchPrice(costOfLunches);
			double costOfDinners = countOfDinners * priceOfDinner; 
			unit.setDinnerPrice(costOfDinners);
			double generalCost = costOfLunches + costOfDinners;
			unit.setTotalCost(generalCost);
			
			units.add(unit);
		}
		
		LOG.info("Actual Data were successfully extracted.");
		extracted = true;
	}

}
