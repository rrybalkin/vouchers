package com.romansun.printing.writer.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.romansun.config.Configuration;
import com.romansun.printing.data.ReportUnit;
import com.romansun.printing.writer.IReportWriter;

/**
 * Class for creating and generating report file in type EXCEL
 * @author Roman Rybalkin
 * 
 */
public class XLSReportWriter implements IReportWriter {
	private final static Logger LOG = Logger.getLogger(XLSReportWriter.class);
	
	private final Configuration config = Configuration.getInstance();
	private File reportFolder;
	/* Columns of template */
	private List<String> columns;
	/* Associations for columns in template and their indexes */
	private Map<String, Integer> columnIndexes;
	
	public XLSReportWriter(String pathToReportFolder) 
	{
		File folder = new File(pathToReportFolder);
		if (folder == null || !folder.exists()) {
			throw new IllegalArgumentException("Папка для сохранения отчетов = '" + pathToReportFolder + "' не найдена!");
		} else {
			this.reportFolder = folder;
		}
		
		columns = Arrays.asList(config.FULL_SET_OF_FIELDS_IN_TEMPLATE);
	}

	@Override
	public File generateReport(Iterator<ReportUnit> reportData, String reportDate) {
		LOG.debug("Start generating report for date = " + reportDate + " ...");
		
		File report = null;
		try {
			InputStream inp = new FileInputStream(config.pathToExcelTemplate);
			Workbook wb = WorkbookFactory.create(inp);
			inp.close();
			Sheet sheet = wb.getSheetAt(0);
			
			String title = sheet.getRow(0).getCell(0).getStringCellValue();
			title = title.replace(config.macrosDate, reportDate);
			sheet.getRow(0).getCell(0).setCellValue(title);
			LOG.debug("Title of report = " + title);
			
			findColumnIndexes( sheet );
			LOG.debug("Start filling report from data ...");
			int rowNum = 2;
			while(reportData.hasNext()) {
				Row currentRow = sheet.createRow(rowNum);
				ReportUnit unit = reportData.next();
				for (String columnName : columnIndexes.keySet()) {
					int columnIndex = columnIndexes.get(columnName);
					Object pValue = unit.getPropertyByName(columnName);
					
					if (pValue instanceof Integer) {
						currentRow.createCell(columnIndex).setCellValue((Integer) pValue);
					} else if (pValue instanceof Double) {
						currentRow.createCell(columnIndex).setCellValue((Double) pValue);
					} else {
						currentRow.createCell(columnIndex).setCellValue((String) pValue);
					}
				}

				rowNum++;
			}
			LOG.debug(rowNum-2 + " rows were filled!");
			
			// Write the output to a file
			String reportName = "Отчет за " + reportDate;
			report = createReportFile(reportName, "xlsx");
		    FileOutputStream fileOut = new FileOutputStream(report);
		    wb.write(fileOut);
		    fileOut.close();
		    
		    LOG.debug("Report with name = " + reportName + " was successfully created and stored in folder = " + reportFolder.getAbsolutePath());
		} 
		catch (Exception e) 
		{
			LOG.error("Error while generate report: ", e);
		}
		
		return report;
	}
	
	/**
	 * Method for creating file of report; 
	 * If file with initial name already exists - name will be extended by suffix = " (i)"
	 * @param name name of report
	 * @param extension extension of file
	 * @return file with unique name within report folder
	 */
	private File createReportFile(String name, String extension) {
		File file = new File(reportFolder.getAbsolutePath() + "//" + name + "." + extension);
		int index = 1;
		while (file.exists()) {
			file = new File(reportFolder.getAbsolutePath() + "//" + name + " (" + index++ + ")" + "." + extension);
		}
		
		return file;
	}
	
	/**
	 * Method for finding columns and their indexes in template sheet
	 * @param sheet template sheet which contains columns
	 */
	private void findColumnIndexes(Sheet sheet) {
		LOG.debug("Find columns indexes by their names in current template ...");
		columnIndexes = new HashMap<String, Integer>();
		
		Row titleRow = sheet.getRow(1);
		for (Cell cell : titleRow) {
			String cellValue = cell.getStringCellValue();

			for (String column : columns) {
				if (column.equalsIgnoreCase(cellValue)) {
					LOG.debug("  Found: columnName = " + column + ", index = " + cell.getColumnIndex());
					columnIndexes.put(column, cell.getColumnIndex());
				}
			}
		}
		
		LOG.debug("End of finding columns!");
	}

}
