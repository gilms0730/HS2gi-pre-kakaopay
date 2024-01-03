package com.example.gms.product.service;

import com.example.gms.product.model.PaymentProducts;
import com.example.gms.product.model.Product;
import com.google.gson.Gson;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {
    private final IamportClient iamportClient;

    private final ProductService productService;

    public OrderService(IamportClient iamportClient, ProductService productService) {
        this.iamportClient = iamportClient;
        this.productService = productService;
    }

    public Boolean paymentValidation(String impUid) throws IamportResponseException, IOException {
        IamportResponse<Payment> response = getPaymentInfo(impUid);
        Integer amount = response.getResponse().getAmount().intValue();

        String customDataString = response.getResponse().getCustomData();
        Gson gson = new Gson();
        PaymentProducts paymentProducts = gson.fromJson(customDataString, PaymentProducts.class);

        Integer totalPrice = productService.getTotalPrice(paymentProducts);

        if(amount.equals(totalPrice) ) {
            return true;
        }

        return false;

    }
    public IamportResponse getPaymentInfo(String impUid) throws IamportResponseException, IOException {
        IamportResponse<Payment> response = iamportClient.paymentByImpUid(impUid);
        return response;
    }
}
