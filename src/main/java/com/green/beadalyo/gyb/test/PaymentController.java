package com.green.beadalyo.gyb.test;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("payment")
@RequiredArgsConstructor
public class PaymentController
{

    private final PaymentService service ;

    @PostMapping("webhook")
    public void webhook(@RequestBody PaymentRequest request) throws Exception
    {

        if (request.getType().equals("Transaction.Ready")) return ;
        PaymentData data = service.getData(request.getData().getPaymentId()) ;

        System.out.println(data);

    }
}
