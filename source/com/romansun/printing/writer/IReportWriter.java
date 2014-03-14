package com.romansun.printing.writer;

import java.io.File;
import java.util.Iterator;

import com.romansun.printing.data.ReportUnit;

public interface IReportWriter {
	/**
	 * Method for generating file report for data
	 * @param data data of generating report
	 * @param date date of generation report (format = "month YYYY")
	 * @return file of generated report
	 */
	public File generateReport(Iterator<ReportUnit> data, String date);
}
