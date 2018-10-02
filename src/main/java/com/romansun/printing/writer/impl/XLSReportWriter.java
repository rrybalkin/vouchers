package com.romansun.printing.writer.impl;

import com.romansun.printing.data.ReportUnit;
import com.romansun.printing.writer.IReportWriter;
import com.romansun.utils.Configuration;
import com.romansun.utils.Resources;
import com.romansun.utils.Utils;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

	// template [column alias => column name} mappings
	private Map<String, String> columnsMapping;

	// columns in template and their indexes
	private Map<String, Integer> columnIndexes;
	
	// cell formulas
	private Map<String, String> cellFormulas;
	{
		cellFormulas = new HashMap<>();
		cellFormulas.put("#", "ROW()-2");
	}
	
	public XLSReportWriter(String pathToReportFolder, String format)
	{
		File folder = new File(pathToReportFolder);
		if (!folder.exists() && !folder.mkdirs()) {
			throw new IllegalArgumentException("Folder by path=" + pathToReportFolder + " does not exist");
		} else {
			this.reportFolder = folder;
		}
		this.version = format;
		columnsMapping = config.xlsTemplateColumns;
	}

	@Override
	public File generateReport(Iterator<ReportUnit> reportData, String reportDate) {
		LOG.info("Start generating report from data at " + reportDate + " ...");

		String reportName = generateReportName(reportDate);
		File report = null;
		try {
			final Workbook wb = createNewWorkbook();
			final Sheet sheet = wb.getSheetAt(0);
            updateReportTitle(sheet, reportDate);

			findColumnIndexes( sheet );

			LOG.debug("Start filling report from data ...");
			int rowNum = config.xlsTemplateHeaders; // skip headers lines
			while(reportData.hasNext()) {
				Row currentRow = sheet.createRow(rowNum);
				ReportUnit unit = reportData.next();

				for (String columnAlias : columnIndexes.keySet()) {
                    final Cell cell = currentRow.createCell(columnIndexes.get(columnAlias));

                    // check if cell should contain formula value
                    if (cellFormulas.containsKey(columnAlias)) {
                        cell.setCellFormula(cellFormulas.get(columnAlias));
                        continue;
                    }

                    // put a cell value from unit data
				    Object pValue = unit.getPropertyByName(columnAlias);
					if (pValue != null) {
                        if (pValue instanceof Integer) {
                            cell.setCellValue((Integer) pValue);
                        } else if (pValue instanceof Double) {
                            cell.setCellValue((Double) pValue);
                        } else {
                            cell.setCellValue((String) pValue);
                        }
                    } else {
                        LOG.warn("Unit data by columnAlias='" + columnAlias + "' not found!");
                    }
				}

				rowNum++;
			}

			// create stats row
			createStatsRow(sheet, config.xlsTemplateHeaders, rowNum);

			LOG.debug(rowNum - config.xlsTemplateHeaders + " rows were filled");
			
			// Write the output to a file
			report = Utils.createUniqueFile(reportFolder, reportName, version);
		    FileOutputStream fileOut = new FileOutputStream(report);
		    wb.write(fileOut);
		    fileOut.close();
		    
		    LOG.info("Report with name = " + reportName + " was successfully created and stored in folder = " + reportFolder.getAbsolutePath());
		} 
		catch (Exception e) {
			LOG.error("Error while generate report: ", e);
		}
		return report;
	}

	private Workbook createNewWorkbook() throws IOException, InvalidFormatException {
        File templateFile;
        if ("XLS".equalsIgnoreCase(version)) {
            templateFile = Resources.getInstance().getResource(Resources.XLS_REPORT_TEMPLATE);
        } else if ("XLSX".equalsIgnoreCase(version)) {
            templateFile = Resources.getInstance().getResource(Resources.XLSX_REPORT_TEMPLATE);
        } else {
            throw new IllegalArgumentException("Version = " + version + " is unknown");
        }

        try (InputStream inp = new FileInputStream(templateFile)){
            return WorkbookFactory.create(inp);
        }
    }

	private String generateReportName(String reportDate) {
		return config.reportNameTemplate.replace(config.macrosDate, reportDate);
	}

	private void updateReportTitle(Sheet sheet, String reportDate) {
        String title = sheet.getRow(0).getCell(0).getStringCellValue();
        title = title.replace(config.macrosDate, reportDate);
        sheet.getRow(0).getCell(0).setCellValue(title);
        LOG.debug("Report title = " + title);
    }
	
	/**
	 * Looking for columns and their indexes in the template sheet.
	 * @param sheet template sheet which contains columns
	 */
	private void findColumnIndexes(Sheet sheet) {
		LOG.debug("Searching the column indexes by their names in the current template ...");
		columnIndexes = new HashMap<>();
		
		Row titleRow = sheet.getRow(1);
		for (Cell cell : titleRow) {
			String cellValue = cell.getStringCellValue();

			for (Map.Entry<String, String> columnMapping : columnsMapping.entrySet()) {
				String columnAlias = columnMapping.getKey();
				String columnName = columnMapping.getValue();
				if (columnName.equalsIgnoreCase(cellValue)) {
					LOG.debug("  Found: columnName='" + columnName + "' with alias='" + columnAlias + "', column index=" + cell.getColumnIndex());
					columnIndexes.put(columnAlias, cell.getColumnIndex());
				}
			}
		}
		
		LOG.debug("End of finding columns!");
	}

	/**
	 * Creates a row with SUM stats according to configured columns.
	 * @param sheet a spreadsheet page
	 * @param firstRowNum row number which is the first in report report
	 * @param lastRowNum row number which is the last in the report
	 */
	private void createStatsRow(Sheet sheet, int firstRowNum, int lastRowNum) {
		LOG.debug("Creating a stats row in the spreadsheet ...");
		final Row statsRow = sheet.createRow(++lastRowNum);

		for (String statsColumnAlias : config.xlsTemplateStatsColumns) {
			int columnIndex = columnIndexes.get(statsColumnAlias);
			String columnLetter = CellReference.convertNumToColString(columnIndex);

			final Cell cell = statsRow.createCell(columnIndex);
			String sumFormula = "SUM(" + columnLetter + firstRowNum + ":" + columnLetter + lastRowNum + ")";
			LOG.debug("SUM formula for column='" + statsColumnAlias + "' is " + sumFormula);
			cell.setCellFormula(sumFormula);
		}
	}
}
