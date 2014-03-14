package com.romansun.printing.writer;

import com.romansun.printing.writer.impl.XLSReportWriter;

public class WriterFactory {
	
	public static IReportWriter getWriter(String reportType, String pathToReportFolder) {
		IReportWriter writer = null;
		if ("XLS".equalsIgnoreCase(reportType)) {
			writer = new XLSReportWriter(pathToReportFolder);
		}
		
		return writer;
	}	
}
