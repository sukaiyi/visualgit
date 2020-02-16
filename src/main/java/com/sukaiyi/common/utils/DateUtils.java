package com.sukaiyi.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private DateUtils() {

    }

    public static String format(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    public static Date parse(String date, String format) {
        try {
            return new SimpleDateFormat(format).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


}
