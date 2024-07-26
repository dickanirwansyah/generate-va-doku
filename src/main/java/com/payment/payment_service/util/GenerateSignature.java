package com.payment.payment_service.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class GenerateSignature {

    public static final String CLIENT_ID = "Client-Id";
    public static final String REQUEST_ID = "Request-Id";
    public static final String REQUEST_TIMESTAMP = "Request-Timestamp";
    public static final String REQUEST_TARGET = "Request-Target";
    public static final String DIGEST = "Digest";
    public static final String COLON_SYMBOL = ":";
    public static final String NEW_LINE = "\n";

    //generate signature
    public String generateSignature(String clientId, String requestId, String requestTimestamp, String requestTarget, String digest, String secret) throws InvalidKeyException, NoSuchAlgorithmException {
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
        System.out.println("HMACSHA256="+signature);
        return "HMACSHA256="+signature;
    }


}


