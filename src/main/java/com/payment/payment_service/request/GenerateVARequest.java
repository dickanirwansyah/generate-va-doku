package com.payment.payment_service.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenerateVARequest {

    @JsonProperty("order")
    private Order order;
    @JsonProperty("virtual_account_info")
    private VirtualAccountInfo virtualAccountInfo;
    @JsonProperty("customer")
    private Customer customer;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Order {
        @JsonProperty("invoice_number")
        private String invoiceNumber;
        @JsonProperty("amount")
        private BigDecimal amount;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class VirtualAccountInfo {
        @JsonProperty("billing_type")
        private String billingType;
        @JsonProperty("expired_time")
        private String expiredTime;
        @JsonProperty("reusable_status")
        private boolean reusable;
        private String info1;
        private String info2;
        private String info3;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Customer {
        @JsonProperty("name")
        private String name;
        @JsonProperty("email")
        private String email;
    }
}
