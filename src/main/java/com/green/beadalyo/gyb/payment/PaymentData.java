package com.green.beadalyo.gyb.payment;

import lombok.Data;

@Data
public class PaymentData
{
    private String status;
    private String id;
    private String transactionId;
    private String merchantId;
    private String storeId;
    private paymentMethod method;
    private Channel channel;

    //결제 요청 시점
    private String requestedAt;
    //주문명
    private String orderName;
    //사용자 정의 값
    private String customData ;
    //결제 정보
    private Amount amount;
    //통화단위
    private String currency;
    //고객 정보
    private Customer customer;
    //결제 완료 시점
    private String paidAt;
    //PG사 거래 아이디
    private String pgTxId;

    @Override
    public String toString()
    {
        return new StringBuilder()
                .append("status").append(" : ").append(status).append("\n")
                .append("id").append(" : ").append(id).append("\n")
                .append("transactionId").append(" : ").append(transactionId).append("\n")
                .append("merchantId").append(" : ").append(merchantId).append("\n")
                .append("storeId").append(" : ").append(storeId).append("\n")
                .append("method").append(" : ").append(method).append("\n")
                .append("channel").append(" : ").append(channel).append("\n")
                .append("requestedAt").append(" : ").append(requestedAt).append("\n")
                .append("orderName").append(" : ").append(orderName).append("\n")
                .append("customData").append(" : ").append(customData).append("\n")
                .append("amount").append(" : ").append(amount).append("\n")
                .append("currency").append(" : ").append(currency).append("\n")
                .append("customer").append(" : ").append(customer).append("\n")
                .append("paidAt").append(" : ").append(paidAt).append("\n")
                .append("pgTxId").append(" : ").append(pgTxId).append("\n").toString();


    }

}

@Data
class paymentMethod {

    private String type;
    private String provider;
    private EasyPayMethod easyPayMethod;

}

@Data
class EasyPayMethod {

    private String type;
    private Card card;
    private String approvalNumber;
    private Installment installment;
    private boolean pointUsed;

}

@Data
class Card
{
    private String publisher;
    private String issuer;
    private String brand;
    private String type;
    private String ownerType;
    private String bin;
    private String name;
    private String number;
}

@Data
class Installment
{

    private int month;
    private boolean isInterestFree;
}

@Data
class Channel
{

    private String type;
    private String id;
    private String key;
    private String name;
    private String pgProvider;
    private String pgMerchantId;
}


@Data
class WebhookRequest
{
    private String header;
    private String body;
    private String requestedAt;
}

@Data
class WebhookResponse
{

    private String code;
    private String header;
    private String body;
    private String respondedAt;
}

@Data
class Amount
{

    private int total;
    private int taxFree;
    private int vat;
    private int supply;
    private int discount;
    private int paid;
    private int cancelled;
    private int cancelledTaxFree;
}

@Data
class Customer
{
    private String id;
    private String name ;
    private String gender ;
    private String email ;
    private String phoneNumber;
    private String address ;
    private String zipcode;
}
