package com.romansun.gui.controller;

import com.romansun.printing.writer.ReportType;
import javafx.util.StringConverter;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Converters {

    public static final StringConverter<Boolean> YES_NO_TO_BOOLEAN = new StringConverter<Boolean>() {
        @Override
        public String toString(Boolean o) {
            return o ? "Да" : "Нет";
        }

        @Override
        public Boolean fromString(String string) {
            return "ДА".equalsIgnoreCase(string) ? Boolean.TRUE : Boolean.FALSE;
        }
    };

    public static final StringConverter<ReportType> REPORT_TYPE_CONVERTER = new StringConverter<ReportType>() {
        @Override
        public String toString(ReportType object) {
            return object.name();
        }

        @Override
        public ReportType fromString(String string) {
            return ReportType.valueOf(string);
        }
    };

    /*
	 * A month converter, name -> digit.
	 */
    public static final StringConverter<Integer> MONTHS_CONVERTER = new StringConverter<Integer>() {

        private final Map<String, Integer> MONTHS = new HashMap<>();
        {
            MONTHS.put(Month.JANUARY.getDisplayName(TextStyle.FULL, Locale.getDefault()), 1);
            MONTHS.put(Month.FEBRUARY.getDisplayName(TextStyle.FULL, Locale.getDefault()), 2);
            MONTHS.put(Month.MARCH.getDisplayName(TextStyle.FULL, Locale.getDefault()), 3);
            MONTHS.put(Month.APRIL.getDisplayName(TextStyle.FULL, Locale.getDefault()), 4);
            MONTHS.put(Month.MAY.getDisplayName(TextStyle.FULL, Locale.getDefault()), 5);
            MONTHS.put(Month.JUNE.getDisplayName(TextStyle.FULL, Locale.getDefault()), 6);
            MONTHS.put(Month.JULY.getDisplayName(TextStyle.FULL, Locale.getDefault()), 7);
            MONTHS.put(Month.AUGUST.getDisplayName(TextStyle.FULL, Locale.getDefault()), 8);
            MONTHS.put(Month.SEPTEMBER.getDisplayName(TextStyle.FULL, Locale.getDefault()), 9);
            MONTHS.put(Month.OCTOBER.getDisplayName(TextStyle.FULL, Locale.getDefault()), 10);
            MONTHS.put(Month.NOVEMBER.getDisplayName(TextStyle.FULL, Locale.getDefault()), 11);
            MONTHS.put(Month.DECEMBER.getDisplayName(TextStyle.FULL, Locale.getDefault()), 12);
        }

        @Override
        public Integer fromString(String month) {
            return MONTHS.get(month);
        }

        @Override
        public String toString(Integer monthIndex) {
            for (Map.Entry<String, Integer> monthEntry : MONTHS.entrySet()) {
                if (monthIndex.equals(monthEntry.getValue()))
                    return monthEntry.getKey();
            }
            throw new IllegalArgumentException("Month by index=" + monthIndex + " not found");
        }
    };

    /*
     * A year converter, string -> int.
     */
    public static final StringConverter<Integer> YEARS_CONVERTER = new StringConverter<Integer>() {

        @Override
        public Integer fromString(String month) {
            return Integer.parseInt(month);
        }

        @Override
        public String toString(Integer month) {
            return month.toString();
        }
    };
}
