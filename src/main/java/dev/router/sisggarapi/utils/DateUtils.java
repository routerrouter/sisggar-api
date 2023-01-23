package dev.router.sisggarapi.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class DateUtils {

    public static LocalDate stringToDate(String date) {
        LocalDate localDate = LocalDate.parse(date);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setLenient(false);
        return localDate;
    }

    public static void main(String[] args) {
        System.out.println(stringToDate("2022-01-12"));
    }
}
