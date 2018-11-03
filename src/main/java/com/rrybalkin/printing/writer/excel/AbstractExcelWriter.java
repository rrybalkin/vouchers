package com.rrybalkin.printing.writer.excel;

import com.rrybalkin.printing.data.ReportUnit;
import com.rrybalkin.printing.writer.ReportType;
import com.rrybalkin.printing.writer.ReportWriter;
import com.rrybalkin.utils.Configuration;
import com.rrybalkin.utils.Resources;
import com.rrybalkin.utils.Utils;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public abstract class AbstractExcelWriter implements ReportWriter {
    private static final Logger LOG = Logger.getLogger(AbstractExcelWriter.class);

    final Configuration config = Configuration.getInstance();

    private File reportsFolder;

    // columns in template and their indexes
    Map<String, Integer> columnIndexes;

    // cell formulas
    static Map<String, String> cellFormulas;

    static {
        cellFormulas = new HashMap<>();
        cellFormulas.put("#", "ROW()-2");
    }

    AbstractExcelWriter() {
        File reportsFolder = new File(config.pathToReports);
        if (!reportsFolder.exists() && !reportsFolder.mkdirs()) {
            throw new IllegalArgumentException("Folder by path=" + config.pathToReports + " does not exist");
        } else {
            this.reportsFolder = reportsFolder;
        }
    }

    @Override
    public File generateReport(ReportType reportType, Iterator<ReportUnit> data, String reportDate) {
        LOG.info("Start generating report from data at " + reportDate + " ...");
        try {
            Workbook wb = createNewWorkbook(reportType);
            final Sheet sheet = wb.getSheetAt(0);
            updateReportTitle(sheet, reportDate);
            findColumnIndexes(sheet);
            populateWorkbook(wb, data, reportDate);

            // Write the output to a file
            final String reportName = generateReportName(reportDate);
            File report = Utils.createUniqueFile(reportsFolder, reportName, reportType.name().toLowerCase(Locale.getDefault()));
            try (FileOutputStream fileOut = new FileOutputStream(report)) {
                wb.write(fileOut);
            }
            LOG.info("Report with name = " + reportName + " was successfully created and stored in folder = " + reportsFolder.getAbsolutePath());
            return report;
        } catch (Exception e) {
            LOG.error("Error while generate report: ", e);
            throw new IllegalStateException(e);
        }
    }

    protected abstract void populateWorkbook(Workbook workbook, Iterator<ReportUnit> data, String reportDate) throws Exception;

    private String generateReportName(String reportDate) {
        return config.reportNameTemplate.replace(config.macrosDate, reportDate);
    }

    private Workbook createNewWorkbook(ReportType reportType) throws IOException, InvalidFormatException {
        File templateFile;
        if (reportType == ReportType.XLS) {
            templateFile = Resources.getResource(Resources.XLS_REPORT_TEMPLATE);
        } else if (reportType == ReportType.XLSX) {
            templateFile = Resources.getResource(Resources.XLSX_REPORT_TEMPLATE);
        } else {
            throw new IllegalArgumentException("Report type = " + reportType + " is not supported");
        }

        try (InputStream inp = new FileInputStream(templateFile)) {
            return WorkbookFactory.create(inp);
        }
    }

    /**
     * Updates a spreadsheet title with current report date.
     * @param sheet a spreadsheet to update
     * @param reportDate current report date
     */
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

            for (Map.Entry<String, String> columnMapping : config.excelTemplateColumns.entrySet()) {
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
     * Populates a cell with a data.
     * @param cell a spreadsheet cell
     * @param cellData a cell data
     */
    void populateCell(Cell cell, Object cellData) {
        if (cellData instanceof Integer) {
            cell.setCellValue((Integer) cellData);
        } else if (cellData instanceof Double) {
            cell.setCellValue((Double) cellData);
        } else {
            cell.setCellValue((String) cellData);
        }
    }

    /**
     * Creates a row with SUM stats according to configured columns.
     *
     * @param sheet       a spreadsheet page
     * @param firstRowNum row number which is the first in report report
     * @param lastRowNum  row number which is the last in the report
     */
    void createStatsRow(Sheet sheet, int firstRowNum, int lastRowNum) {
        LOG.debug("Creating a stats row in spreadsheet ...");
        final Row statsRow = sheet.createRow(++lastRowNum);

        for (String statsColumnAlias : config.excelTemplateStatsColumns) {
            int columnIndex = columnIndexes.get(statsColumnAlias);
            String columnLetter = CellReference.convertNumToColString(columnIndex);

            final Cell cell = statsRow.createCell(columnIndex);
            String sumFormula = "SUM(" + columnLetter + firstRowNum + ":" + columnLetter + lastRowNum + ")";
            LOG.debug("SUM formula for column='" + statsColumnAlias + "' is " + sumFormula);
            cell.setCellFormula(sumFormula);
        }
    }
}
