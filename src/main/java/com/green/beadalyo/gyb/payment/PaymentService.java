package com.green.beadalyo.gyb.payment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class PaymentService
{

    WebClient webClient = WebClient.builder().baseUrl("https://api.portone.io").build();

    private final String SECRET_KEY ;

    public PaymentService(@Value("${payment.secret}") String secret)
    {
        this.SECRET_KEY = secret;
    }

    public PaymentData getData(String paymentId)
    {

        Mono<PaymentData> data = webClient.get()
                .uri("payments/"+paymentId )
                .header("Content-Type", "application/json")
                .header("Authorization", "PortOne " + SECRET_KEY)
                .retrieve()
                .bodyToMono(PaymentData.class)
                .doOnSuccess(response -> System.out.println("성공했습니다." + response))
                .doOnError(error -> System.out.println("실패했습니다." + error.getMessage())) ;

        return data.block() ;
    }



}
