package com.rrybalkin.printing.writer;

import com.rrybalkin.printing.data.ReportUnit;

import java.io.File;
import java.util.Iterator;

public interface ReportWriter {

	/**
	 * Method for generating file report based on provided data.
	 *
	 * @param data data of generating report
	 * @param reportDate date of generation report (format = "month YYYY")
	 * @param reportGroup report group of visitors, used as a part of name
	 * @return generated report file
	 */
	File generateReport(Iterator<ReportUnit> data, String reportDate, String reportGroup);
}
