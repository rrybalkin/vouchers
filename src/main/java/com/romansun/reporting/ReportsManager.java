package com.romansun.reporting;

import com.romansun.reporting.jaxb.ReportType;
import com.romansun.reporting.jaxb.RootType;
import com.romansun.reporting.jaxb.VisitorType;
import com.romansun.reporting.jaxb.VisitorsType;
import com.romansun.reporting.logic.InfoVisitor;
import com.romansun.reporting.logic.Report;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReportsManager {
	
	private List<Report> reports;
	private String pathToReports;
	
	public ReportsManager(String pathToReports) {
		this.pathToReports = pathToReports;
		loadReports();
	}
	
	public void loadReports() {
		reports = new ArrayList<>();
		try {
			File dirReports = new File(pathToReports);
			if (dirReports.exists() && dirReports.isDirectory()) {
				File[] xmlFiles = dirReports.listFiles();
				if (xmlFiles == null) {
					throw new IllegalArgumentException(pathToReports + " folder does not exist");
				}
				for (File xmlReport : xmlFiles) {
					Report report = parseReport(xmlReport);
					reports.add(report);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<Report> getReportsByDate(Integer month, Integer year) {
		List<Report> needReports = new ArrayList<>();
		for (Report report : reports) {
			Integer curMonth = report.getMonth();
			Integer curYear = report.getYear();
			if (curMonth.equals(month) && curYear.equals(year)) {
				needReports.add(report);
			}
		}
		
		return needReports;
	}

	private Report parseReport(File xmlReport) throws Exception {
		// Initialize JAXB
		JAXBContext jaxb = JAXBContext.newInstance(getClass().getPackage().getName() + ".jaxb");
		final Unmarshaller unmarshaller = jaxb.createUnmarshaller();
		JAXBElement<RootType> rootElement = (JAXBElement<RootType>) unmarshaller.unmarshal(xmlReport);

		// Get report
		RootType root = rootElement.getValue();
		ReportType reportElement = root.getReport();
		VisitorsType visitorsElement = reportElement.getVisitors();
		List<VisitorType> visitors = visitorsElement.getVisitor();
		
		List<InfoVisitor> infoVisitors = new ArrayList<>();
		for (VisitorType v : visitors) {
			InfoVisitor infoVisitor = new InfoVisitor(v.getFIO(), v.getLunches(), v.getDinners());
			
			infoVisitors.add(infoVisitor);
		}
		Integer month = reportElement.getMonth();
		Integer year = reportElement.getYear();
		
		Report report = new Report();
		report.setMonth(month);
		report.setYear(year);
		report.setVisitors(infoVisitors);
		report.setName(xmlReport.getName());
		report.setFile(xmlReport);
		
		return report;
	}
}
