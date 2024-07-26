package com.payment.payment_service.util;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Slf4j
public class GenerateInvoiceNumber {

    public static final String generateInvoicNumber(String patternInvoice){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String dateFormat = simpleDateFormat.format(new Date());

        //generate a rancom 4 digit number
        Random random = new Random();
        int randomNumber = random.nextInt(10000);

        //format the random number to ensure it has 4 digits
        String formatRandomNumber = String.format("04d", randomNumber);

        //combine with pattern for example INV-
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(patternInvoice);
        stringBuilder.append("-");
        stringBuilder.append(dateFormat);
        stringBuilder.append("-");
        stringBuilder.append(formatRandomNumber);
        log.info("final result generate random invoice number = [{}]",stringBuilder.toString());
        return stringBuilder.toString();
    }
}
