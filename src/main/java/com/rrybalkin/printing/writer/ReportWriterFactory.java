package com.rrybalkin.printing.writer;

import com.rrybalkin.printing.writer.excel.SinglePageExcelWriter;

import java.util.HashMap;
import java.util.Map;

/**
 * A factory class to get a report writer by report type.
 *
 * @author Roman Rybalkin
 */
public class ReportWriterFactory {

	private static Map<ReportType, ReportWriter> reportWriters;
	static {
		reportWriters = new HashMap<>();

		final ReportWriter excelReportWriter = new SinglePageExcelWriter();
		reportWriters.put(ReportType.XLS, excelReportWriter);
		reportWriters.put(ReportType.XLSX, excelReportWriter);
	}

	/**
	 * Method for getting a report writer by type.
	 *
	 * @param reportType type of report
	 * @return the report writer implementation.
	 */
	public static ReportWriter getWriter(ReportType reportType) {
		return reportWriters.get(reportType);
	}	
}
