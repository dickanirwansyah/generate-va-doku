package com.payment.payment_service.util;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class ExampleTimestamp {

    public static void main(String[] args) {

        // Step 1: Get the current date
        LocalDate currentDate = LocalDate.now();

        // Step 2: Define the specific time (08:51:00)
        LocalTime specificTime = LocalTime.of(8, 51, 0);

        // Step 3: Combine the current date with the specific time to create a LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.of(currentDate, specificTime);

        // Step 4: Define the ZoneId for WIB (UTC+7)
        ZoneId wibZoneId = ZoneId.of("Asia/Jakarta");

        // Step 5: Create a ZonedDateTime in WIB
        ZonedDateTime wibZonedDateTime = localDateTime.atZone(wibZoneId);

        // Step 6: Convert the ZonedDateTime to UTC
        ZonedDateTime utcZonedDateTime = wibZonedDateTime.withZoneSameInstant(ZoneOffset.UTC);

        // Step 7: Define the formatter to output the date-time in the desired format
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

        // Step 8: Format the ZonedDateTime to a string
        String formattedDateTime = utcZonedDateTime.format(formatter);

        // Step 9: Print the formatted date-time string
        System.out.println(formattedDateTime);

    }
}
