package com.sukaiyi.visualgit.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static String format(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

}
