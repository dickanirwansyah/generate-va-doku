package com.payment.payment_service.util;


import org.json.JSONObject;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

public class ExampleSignature {

    public static final String CLIENT_ID = "Client-Id";
    public static final String REQUEST_ID = "Request-Id";
    public static final String REQUEST_TIMESTAMP = "Request-Timestamp";
    public static final String REQUEST_TARGET = "Request-Target";
    public static final String DIGEST = "Digest";
    public static final String COLON_SYMBOL = ":";
    public static final String NEW_LINE = "\n";

    //generate digest
    public static String generateDigest(String requestBody) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(requestBody.getBytes(StandardCharsets.UTF_8));
        byte[] digest = md.digest();
        return Base64.getEncoder().encodeToString(digest);
    }

    //generate signature
    private static String generateSignature(String clientId, String requestId, String requestTimestamp, String requestTarget, String digest, String secret) throws InvalidKeyException, NoSuchAlgorithmException {
        // Prepare Signature Component
        System.out.println("----- Component Signature -----");
        StringBuilder component = new StringBuilder();
        component.append(CLIENT_ID).append(COLON_SYMBOL).append(clientId);
        component.append(NEW_LINE);
        component.append(REQUEST_ID).append(COLON_SYMBOL).append(requestId);
        component.append(NEW_LINE);
        component.append(REQUEST_TIMESTAMP).append(COLON_SYMBOL).append(requestTimestamp);
        component.append(NEW_LINE);
        component.append(REQUEST_TARGET).append(COLON_SYMBOL).append(requestTarget);
        // If body not send when access API with HTTP method GET/DELETE
        if(digest != null && !digest.isEmpty()) {
            component.append(NEW_LINE);
            component.append(DIGEST).append(COLON_SYMBOL).append(digest);
        }

        System.out.println(component.toString());
        System.out.println();

        // Calculate HMAC-SHA256 base64 from all the components above
        byte[] decodedKey = secret.getBytes();
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");
        Mac hmacSha256 = Mac.getInstance("HmacSHA256");
        hmacSha256.init(originalKey);
        hmacSha256.update(component.toString().getBytes());
        byte[] HmacSha256DigestBytes = hmacSha256.doFinal();
        String signature = Base64.getEncoder().encodeToString(HmacSha256DigestBytes);
        // Prepend encoded result with algorithm info HMACSHA256=
        return "HMACSHA256="+signature;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException  {
        String jsonBody = new JSONObject()
                .put("order", new JSONObject()
                        .put("invoice_number", "INV-20210124-0001")
                        .put("amount", 15000)
                )
                .put("virtual_account_info", new JSONObject()
                        .put("expired_time", 60)
                        .put("amount", 15000)
                )
                .toString();

        // Generate Digest from JSON Body, For HTTP Method GET/DELETE don't need generate Digest
        System.out.println("----- Digest -----");
        String digest = generateDigest(jsonBody);
        System.out.println(digest);
        System.out.println();

        // Generate Signature
        String headerSignature = generateSignature(
                "BRN-0258-1654228632223",
                UUID.randomUUID().toString(),
                GenerateTimestamp.generateTimestamp(),
                "/mandiri-virtual-account/v2/payment-code", // For merchant request to DOKU, use DOKU path here. For HTTP Notification, use merchant path here
                digest, // Set empty string for this argumentes if HTTP Method is GET/DELETE
                "SK-3pXfYMvlr5KYxVC9Gd4f");

        System.out.println("----- Header Signature -----");
        System.out.println(headerSignature);
    }

}
