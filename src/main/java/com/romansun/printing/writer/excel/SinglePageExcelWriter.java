package com.romansun.printing.writer.excel;

import com.romansun.printing.data.ReportUnit;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Iterator;

/**
 * Class for creating and generating report file in type EXCEL
 *
 * @author Roman Rybalkin
 */
public class SinglePageExcelWriter extends AbstractExcelWriter {
    private final static Logger LOG = Logger.getLogger(SinglePageExcelWriter.class);

    @Override
    protected void populateWorkbook(Workbook workbook, Iterator<ReportUnit> data, String reportDate) throws Exception {
        LOG.debug("Start populating workbook spreadsheet by data ...");
        final Sheet sheet = workbook.getSheetAt(0);

        final int headers = config.excelTemplateHeaders;
        int rowNum = headers; // skip headers lines
        while (data.hasNext()) {
            Row currentRow = sheet.createRow(rowNum);
            ReportUnit unit = data.next();

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
                    populateCell(cell, pValue);
                } else {
                    LOG.warn("Unit data by column alias='" + columnAlias + "' not found!");
                }
            }

            rowNum++;
        }

        // create a stats row
        createStatsRow(sheet, headers, rowNum);

        LOG.debug(rowNum - headers + " rows were filled");
    }
}
