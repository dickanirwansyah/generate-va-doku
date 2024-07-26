package com.payment.payment_service.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class TimestampConverter {

    public static void main(String[] args){
        // Example local time in WIB (UTC+7)
        //LocalDateTime localDateTime = LocalDateTime.of(2020, 9, 22, 8, 51, 0);
        LocalDateTime localDateTime = LocalDateTime.now();

        // Convert to UTC (UTC+0)
        LocalDateTime utcDateTime = convertToUTC(localDateTime, 7);

        // Format as ISO 8601 UTC time
        String iso8601UTC = formatToISO8601UTC(utcDateTime);

        System.out.println("Local DateTime (WIB): " + localDateTime);
        System.out.println("Converted to UTC DateTime: " + utcDateTime);
        System.out.println("ISO 8601 UTC Time: " + iso8601UTC);
    }

    // Convert local time to UTC time with specified offset from UTC
    private static LocalDateTime convertToUTC(LocalDateTime localDateTime, int offsetHours) {
        return localDateTime.minusHours(offsetHours);
    }

    // Format LocalDateTime to ISO 8601 UTC format (yyyy-MM-dd'T'HH:mm:ss'Z')
    private static String formatToISO8601UTC(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        return dateTime.atOffset(ZoneOffset.UTC).format(formatter);
    }
}
