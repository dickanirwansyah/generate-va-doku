package com.payment.payment_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.payment.payment_service.doku.BankBRIVAService;
import com.payment.payment_service.doku.BankMandiriVAService;
import com.payment.payment_service.request.GenerateVARequest;
import com.payment.payment_service.util.GenerateInvoiceNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

@RestController
@RequestMapping(value = "/generate/va")
@RequiredArgsConstructor
public class GenerateVAController {

    private final BankBRIVAService bankBRIVAService;
    private final BankMandiriVAService bankMandiriVAService;

    @PostMapping(value = "/bank-bri")
    public ResponseEntity<HashMap<String,Object>> generateVaBankBRI(@RequestBody GenerateVARequest request) throws NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        request.getOrder().setInvoiceNumber(GenerateInvoiceNumber.generateInvoicNumber("BRI"));
        return ResponseEntity.ok()
                .body(bankBRIVAService.execute(request));
    }

    @PostMapping(value = "/bank-mandiri")
    public ResponseEntity<HashMap<String,Object>> generateVABankMandiri(@RequestBody GenerateVARequest request) throws NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        request.getOrder().setInvoiceNumber(GenerateInvoiceNumber.generateInvoicNumber("MDR"));
        return ResponseEntity.ok()
                .body(bankMandiriVAService.execute(request));
    }
}
