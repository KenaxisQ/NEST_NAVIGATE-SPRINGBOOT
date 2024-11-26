package com.kenaxisq.nestnavigate.utils;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateFormatter {
    public static void main(String[] args) {
        String expiryTime = "2024-11-26T11:47:41.275816700";
        
        // Parse the original expiryTime string
        LocalDateTime dateTime = LocalDateTime.parse(expiryTime);

        // Format the date to the desired format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d['st']['nd']['rd']['th'] MMM yyyy hh:mm a", Locale.ENGLISH);
        
        // Determine the suffix for the day of the month
        int dayOfMonth = dateTime.getDayOfMonth();
        String daySuffix = getDayOfMonthSuffix(dayOfMonth);

        // Concatenate the correct day suffix
        String formattedDate = dateTime.format(DateTimeFormatter.ofPattern("d", Locale.ENGLISH)) + daySuffix + 
                               dateTime.format(DateTimeFormatter.ofPattern(" MMM yyyy hh:mm a", Locale.ENGLISH));

        System.out.println(formattedDate); // Output: 26th Nov 2024 11:47 AM
    }

    private static String getDayOfMonthSuffix(int n) {
        if (n >= 11 && n <= 13) {
            return "th";
        }
        switch (n % 10) {
            case 1:  return "st";
            case 2:  return "nd";
            case 3:  return "rd";
            default: return "th";
        }
    }
}