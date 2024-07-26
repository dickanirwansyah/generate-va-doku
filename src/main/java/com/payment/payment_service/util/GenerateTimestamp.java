package com.payment.payment_service.util;


import java.time.*;
import java.time.format.DateTimeFormatter;

public class GenerateTimestamp {

    public static String generateTimestamp(){
        LocalDate currentDate = LocalDate.now();
        LocalTime specificTime = LocalTime.of(8, 51, 0);
        LocalDateTime localDateTime = LocalDateTime.of(currentDate, specificTime);
        ZoneId wibZoneId = ZoneId.of("Asia/Jakarta");
        ZonedDateTime wibZonedDateTime = localDateTime.atZone(wibZoneId);
        ZonedDateTime utcZonedDateTime = wibZonedDateTime.withZoneSameInstant(ZoneOffset.UTC);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        String formattedDateTime = utcZonedDateTime.format(formatter);
        System.out.println(formattedDateTime);
        return formattedDateTime;
    }

    public static String timestampConverter(){
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime utcDateTime = convertToUTC(localDateTime, 7);
        String iso8601UTC = formatToISO8601UTC(utcDateTime);

        System.out.println("Local DateTime (WIB): " + localDateTime);
        System.out.println("Converted to UTC DateTime: " + utcDateTime);
        System.out.println("ISO 8601 UTC Time: " + iso8601UTC);
        return iso8601UTC;
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
