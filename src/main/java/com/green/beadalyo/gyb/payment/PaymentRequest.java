package com.green.beadalyo.gyb.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PaymentRequest {

    private String type;
    private String timestamp;
    private IdData data; // 중첩된 JSON 객체를 위한 필드


    @Data
    public static class IdData {
        @JsonProperty("transactionId")
        private String transactionId;

        @JsonProperty("paymentId")
        private String paymentId;

    }
}

