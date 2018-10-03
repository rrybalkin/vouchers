package com.romansun.printing.writer.excel;

import com.romansun.printing.data.ReportUnit;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Iterator;

/**
 * Class for creating and generating report file in type EXCEL
 * @author Roman Rybalkin
 * 
 */
public class MultiPageExcelWriter extends AbstractExcelWriter {
	private final static Logger LOG = Logger.getLogger(MultiPageExcelWriter.class);

	@Override
	protected void populateWorkbook(Workbook workbook, Iterator<ReportUnit> data, String reportDate) throws Exception {
		Sheet firstSheet = workbook.getSheetAt(0);
		// TODO
	}
}
