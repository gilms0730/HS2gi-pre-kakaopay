package com.example.gms.product.controller;

import com.example.gms.product.service.OrderService;
import com.example.gms.product.service.ProductService;
import com.siot.IamportRestClient.response.IamportResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class OrderController {
    private final ProductService productService;
    private final OrderService orderService;

    public OrderController(ProductService productService, OrderService orderService) {
        this.productService = productService;
        this.orderService = orderService;
    }
    @RequestMapping("/validation")
    public ResponseEntity validation (String impUid){
        try {
            if (orderService.paymentValidation(impUid))
            {
                return ResponseEntity.ok().body("오키");
            }
            else {
                return  ResponseEntity.ok().body("에러");
            }
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.ok().body("error");
        }
    }
}
