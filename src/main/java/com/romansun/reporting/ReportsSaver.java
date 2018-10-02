package com.romansun.reporting;

import com.romansun.reporting.jaxb.*;
import com.romansun.reporting.logic.InfoVisitor;
import com.romansun.reporting.logic.Report;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;


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
		List<VisitorType> visitors = new ArrayList<>();
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
		JAXBContext jc = JAXBContext.newInstance(getClass().getPackage().getName() + ".jaxb");
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		File dstFile = getSaveFile();
		FileOutputStream out = new FileOutputStream(dstFile);
		marshaller.marshal(root, out);
	}
	
	private File getSaveFile() {
		StringBuilder fullPath = new StringBuilder(1000);
		fullPath.append(pathToReports);
		if (pathToReports.lastIndexOf("\\") != pathToReports.length()) 
			fullPath.append("\\");
		fullPath.append(report.getName().replace(" ", "_"));
		return new File(fullPath.toString());
	}
}
