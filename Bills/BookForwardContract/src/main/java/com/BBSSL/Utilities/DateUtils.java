package com.BBSSL.Utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    private static final String OUTPUT_PATTERN = "dd-MM-yyyy";

    // Add all possible date-input patterns you may receive
    private static final String[] INPUT_PATTERNS = {
        "EEE MMM dd HH:mm:ss z yyyy",  // e.g. "Sun Mar 15 00:00:00 IST 2029"
        "yyyy-MM-dd",
        "dd/MM/yyyy",
        "MM/dd/yyyy",
        "dd-MM-yyyy",
        "dd-MMM-yyyy"
    };

    /**
     * Attempts to parse the given dateStr using known patterns,
     * then formats it into "dd-MM-yyyy".
     *
     * @param dateStr Input date string in unknown format
     * @return Formatted date string
     * @throws ParseException if none of the patterns match
     */
    public static String toDDMMYYYY(String dateStr) throws ParseException {
        Date parsedDate = null;
        for (String pattern : INPUT_PATTERNS) {
            try {
                SimpleDateFormat parser = new SimpleDateFormat(pattern, Locale.ENGLISH);
                parser.setLenient(false);
                parsedDate = parser.parse(dateStr);
                break;
            } catch (ParseException ignored) { }
        }
        if (parsedDate == null) {
            throw new ParseException("Unrecognized date format: " + dateStr, 0);
        }
        return new SimpleDateFormat(OUTPUT_PATTERN).format(parsedDate);
    }
}
