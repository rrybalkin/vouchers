package com.romansun.printing.writer;

import java.io.File;
import java.util.Iterator;

import com.romansun.printing.data.ReportUnit;

public interface ReportWriter {

	/**
	 * Method for generating file report based on provided data.
	 *
	 * @param reportType a report type - XLS, XLSX, etc.
	 * @param data data of generating report
	 * @param date date of generation report (format = "month YYYY")
	 * @return generated report file
	 */
	File generateReport(ReportType reportType, Iterator<ReportUnit> data, String date);
}
