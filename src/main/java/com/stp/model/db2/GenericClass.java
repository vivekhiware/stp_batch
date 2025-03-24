package com.stp.model.db2;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GenericClass {

    // Method to convert string to Date
    public static Date convertStringToDateMysql(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            Date parsedDate = sdf.parse(dateString);
            return removeTimeFromDate(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null; // Return null if parsing fails
    }

    // Method to remove time from Date (set to 00:00:00)
    private static Date removeTimeFromDate(Date date) {
        if (date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // Reset time to 00:00:00
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    // Main method
    public static void main(String[] args) {
        GenericClass test = new GenericClass();
        Date date = GenericClass.convertStringToDateMysql("20-Jun-2023");

        if (date != null) {
            System.out.println("Formatted Date (Date object): " + date);
        } else {
            System.out.println("Invalid date format.");
        }
    }
}
