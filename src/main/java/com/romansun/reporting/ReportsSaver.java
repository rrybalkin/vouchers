package com.romansun.reporting;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;

import com.romansun.reporting.jaxb.ObjectFactory;
import com.romansun.reporting.jaxb.ReportType;
import com.romansun.reporting.jaxb.RootType;
import com.romansun.reporting.jaxb.VisitorType;
import com.romansun.reporting.jaxb.VisitorsType;
import com.romansun.reporting.logic.InfoVisitor;
import com.romansun.reporting.logic.Report;


public class ReportsSaver {
	private String pathToReports;
	private Report report;
	
	public ReportsSaver(String pathToReports, Report report) {
		this.pathToReports = pathToReports;
		this.report = report;
	}
	
	public void saveReport() throws Exception {
		ObjectFactory of = new ObjectFactory();
		RootType rootElement = new RootType();
		ReportType reportElement = new ReportType();
		VisitorsType visitorsElement = new VisitorsType();
		// Getting info about all visitors from report
		List<VisitorType> visitors = new ArrayList<VisitorType>();
		List<InfoVisitor> infoVisitors = report.getVisitors();
		for (InfoVisitor info : infoVisitors) {
			VisitorType visitor = new VisitorType();
			visitor.setFIO(info.getFIO());
			visitor.setLunches(info.getLunches());
			visitor.setDinners(info.getDinners());
			
			visitors.add(visitor);
		}
		// Adding all visitors with info
		visitorsElement.getVisitor().addAll(visitors);
		// Adding report tag with attributes
		reportElement.setVisitors(visitorsElement);
		reportElement.setMonth(report.getMonth());
		reportElement.setYear(report.getYear());
		rootElement.setReport(reportElement);
		
		// Start JAXB
		JAXBElement<RootType> root = of.createRoot(rootElement);
		JAXBContext jc = JAXBContext.newInstance("com.romansun.reports.jaxb");
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		File dstFile = getSaveFile();
		FileOutputStream out = new FileOutputStream(dstFile);
		marshaller.marshal(root, out);
	}
	
	private File getSaveFile() throws IOException {
		StringBuilder fullPath = new StringBuilder(1000);
		fullPath.append(pathToReports);
		if (pathToReports.lastIndexOf("\\") != pathToReports.length()) 
			fullPath.append("\\");
		fullPath.append(report.getName().replace(" ", "_"));
		File file = new File(fullPath.toString());
		
		return file;
	}
}
