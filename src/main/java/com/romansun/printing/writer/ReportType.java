package com.romansun.printing.writer;

public enum ReportType {
    XLS,
    XLSX;

    public boolean isExcel() {
        return this == XLS || this == XLSX;
    }
}
