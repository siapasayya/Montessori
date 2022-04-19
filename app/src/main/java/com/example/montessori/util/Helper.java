package com.example.montessori.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Helper {
    public static String convertToFormattedDate(String pattern, Date date) {
        return new SimpleDateFormat(pattern, getDefaultLocale()).format(date);
    }

    public static Locale getDefaultLocale() {
        return new Locale("id", "ID");
    }

}
