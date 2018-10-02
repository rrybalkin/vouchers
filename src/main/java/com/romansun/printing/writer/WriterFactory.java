package com.romansun.printing.writer;

import com.romansun.printing.writer.impl.XLSReportWriter;
/**
 * Factory for getting writer of report by file type
 * @author Roman Rybalkin
 *
 */
public class WriterFactory {

	/**
	 * Method for getting report writer by type
	 * @param reportType type of report - XLS, XLSX, DOC, DOCX, etc.
	 * @param pathToReportFolder path to folder of reports
	 * @return realization of writer
	 */
	public static IReportWriter getWriter(String reportType, String pathToReportFolder) {
		IReportWriter writer = null;
		
		if ("XLS".equalsIgnoreCase(reportType) || "XLSX".equalsIgnoreCase(reportType)) {
			writer = new XLSReportWriter(pathToReportFolder, reportType.toLowerCase());
		}

		return writer;
	}	
}
