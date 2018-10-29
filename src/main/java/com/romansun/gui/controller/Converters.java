package com.romansun.gui.controller;

import com.romansun.printing.writer.ReportType;
import com.romansun.utils.Configuration;
import com.romansun.utils.Messages;
import javafx.util.StringConverter;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Converters {

    public static final StringConverter<Boolean> YES_NO_TO_BOOLEAN = new StringConverter<Boolean>() {
        private final String yes = Messages.get("common.yes");
        private final String no = Messages.get("common.no");

        @Override
        public String toString(Boolean o) {
            return o ? yes : no;
        }

        @Override
        public Boolean fromString(String string) {
            return yes.equalsIgnoreCase(string) ? Boolean.TRUE : Boolean.FALSE;
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
            final Locale locale = Configuration.getInstance().appLocale;
            MONTHS.put(Month.JANUARY.getDisplayName(TextStyle.FULL, locale), 1);
            MONTHS.put(Month.FEBRUARY.getDisplayName(TextStyle.FULL, locale), 2);
            MONTHS.put(Month.MARCH.getDisplayName(TextStyle.FULL, locale), 3);
            MONTHS.put(Month.APRIL.getDisplayName(TextStyle.FULL, locale), 4);
            MONTHS.put(Month.MAY.getDisplayName(TextStyle.FULL, locale), 5);
            MONTHS.put(Month.JUNE.getDisplayName(TextStyle.FULL, locale), 6);
            MONTHS.put(Month.JULY.getDisplayName(TextStyle.FULL, locale), 7);
            MONTHS.put(Month.AUGUST.getDisplayName(TextStyle.FULL, locale), 8);
            MONTHS.put(Month.SEPTEMBER.getDisplayName(TextStyle.FULL, locale), 9);
            MONTHS.put(Month.OCTOBER.getDisplayName(TextStyle.FULL, locale), 10);
            MONTHS.put(Month.NOVEMBER.getDisplayName(TextStyle.FULL, locale), 11);
            MONTHS.put(Month.DECEMBER.getDisplayName(TextStyle.FULL, locale), 12);
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
