package com.payment.payment_service.doku;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.payment_service.request.GenerateVARequest;
import com.payment.payment_service.util.GenerateSignature;
import com.payment.payment_service.util.GenerateTimestamp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class BankBRIVAService {

    @Value("${doku.client-id}")
    private String dokuClientId;

    @Value("${doku.bri.url-generate-va-bri}")
    private String dokuUrlBRIGenerateVa;

    @Value("${doku.bri.url-generate-va-sandbox}")
    private String urlSandboxGenerateVaBRI;

    @Value("${doku.secret-key}")
    private String secretKey;

    private final RestTemplate restTemplate;
    private final GenerateSignature generateSignature;
    private final ObjectMapper objectMapper;

    public HashMap<String,Object> execute(GenerateVARequest request) throws NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        log.info("execute generate va account bank bri..");
        var requestId = UUID.randomUUID().toString();
        String requestTimestamp = GenerateTimestamp.timestampConverter();
        //String jsonBody = "{\"order\":{\"invoice_number\":\"INV-20210124-0005\",\"amount\":150000},\"virtual_account_info\":{\"billing_type\":\"FIX_BILL\",\"expired_time\":60,\"reusable_status\":false,\"info1\":\"Merchant Demo Store\",\"info2\":\"Thank you for shopping\",\"info3\":\"on our store\"},\"customer\":{\"name\":\"Dicka Nirwansyah\",\"email\":\"dickanirwansyah@gmail.com\"}}";
        String jsonBody = objectMapper.writeValueAsString(request);

        String digitalSignature = generateSignature.generateSignature(
                dokuClientId,
                requestId,
                requestTimestamp,
                dokuUrlBRIGenerateVa,
                generateDigest(jsonBody),
                secretKey);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Client-Id", dokuClientId);
        headers.set("Request-Id", requestId);
        headers.set("Request-Timestamp", requestTimestamp);
        headers.set("Signature", digitalSignature);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                urlSandboxGenerateVaBRI,
                HttpMethod.POST,
                requestEntity,
                String.class);
        log.info("response from doku = [{}]",response.getBody());
        HashMap<String,Object> responseMap = objectMapper.readValue(response.getBody(), HashMap.class);
        return responseMap;
    }

    private String generateDigest(String requestBody) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(requestBody.getBytes(StandardCharsets.UTF_8));
        byte[] digest = md.digest();
        return Base64.getEncoder().encodeToString(digest);
    }
}
