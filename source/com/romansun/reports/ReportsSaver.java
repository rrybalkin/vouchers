package com.romansun.reports;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;

import com.romansun.reports.jaxb.ObjectFactory;
import com.romansun.reports.jaxb.ReportType;
import com.romansun.reports.jaxb.RootType;
import com.romansun.reports.jaxb.VisitorType;
import com.romansun.reports.jaxb.VisitorsType;
import com.romansun.reports.logic.InfoVisitor;
import com.romansun.reports.logic.Report;


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
		System.out.println(fullPath.toString());
		if (file!=null && file.exists()) {
			return file;
		}else if(file != null && !file.exists()) {
			file.createNewFile();
			return file;
		}else{
			throw new RuntimeException("Ошибка при создании xml-файла!");
		}
	}
}
