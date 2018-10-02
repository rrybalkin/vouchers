package com.romansun.printing.writer.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.romansun.utils.Resources;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.romansun.utils.Configuration;
import com.romansun.printing.data.ReportUnit;
import com.romansun.printing.writer.IReportWriter;
import com.romansun.utils.Utils;

/**
 * Class for creating and generating report file in type EXCEL
 * @author Roman Rybalkin
 * 
 */
public class XLSReportWriter implements IReportWriter {
	private final static Logger LOG = Logger.getLogger(XLSReportWriter.class);
	
	private final Configuration config = Configuration.getInstance();
	private File reportFolder;
	private String version;
	/* Columns of template */
	private List<String> columns;
	/* Associations for columns in template and their indexes */
	private Map<String, Integer> columnIndexes;
	
	/* Cell formulas */
	private Map<String, CellFormula> cellFormulas;
	{
		cellFormulas = new HashMap<String, CellFormula>();
		cellFormulas.put("�", new CellFormula() {

			@Override
			public String getFormula() {
				return "ROW()-2";
			}
			
		});
	}
	
	public XLSReportWriter(String pathToReportFolder, String version) 
	{
		File folder = new File(pathToReportFolder);
		if (!folder.exists() && !folder.mkdirs()) {
			throw new IllegalArgumentException("Folder by path=" + pathToReportFolder + " does not exist");
		} else {
			this.reportFolder = folder;
		}
		this.version = version;
		columns = config.fieldsOfExcelTemplate;
	}

	@Override
	public File generateReport(Iterator<ReportUnit> reportData, String reportDate) {
		LOG.debug("Start generating report for date = " + reportDate + " ...");
		
		File report = null;
		try {
			File templateFile;
			if ("XLS".equalsIgnoreCase(version)) {
				templateFile = Resources.getInstance().getResource(Resources.XLS_REPORT_TEMPLATE);
			} else if ("XLSX".equalsIgnoreCase(version)) {
				templateFile = Resources.getInstance().getResource(Resources.XLSX_REPORT_TEMPLATE);
			} else {
				throw new IllegalStateException("Version = " + version + " is unknown");
			}
			
			InputStream inp = new FileInputStream(templateFile);
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
					
					Cell cell = currentRow.createCell(columnIndex);
					if (cellFormulas.containsKey(columnName)) {
						cell.setCellFormula(cellFormulas.get(columnName).getFormula());
						continue;
					}
					
					if (pValue instanceof Integer) {
						cell.setCellValue((Integer) pValue);
					} else if (pValue instanceof Double) {
						cell.setCellValue((Double) pValue);
					} else {
						cell.setCellValue((String) pValue);
					}
				}

				rowNum++;
			}
			LOG.debug(rowNum-2 + " rows were filled");
			
			// Write the output to a file
			String reportName = "����� �� " + reportDate;
			report = Utils.createUniqueFile(reportFolder, reportName, version);
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
	 * Method for finding columns and their indexes in template sheet
	 * @param sheet template sheet which contains columns
	 */
	private void findColumnIndexes(Sheet sheet) {
		LOG.debug("Searching the column indexes by their names in the current template ...");
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
	
	/**
	 * Interface for creating cell formula
	 * 
	 * @author Roman Rybalkin
	 *
	 */
	interface CellFormula {
		String getFormula();
	}
}
