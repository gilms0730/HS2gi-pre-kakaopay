package com.example.gms.product.config;

import com.siot.IamportRestClient.IamportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PortOneConfig {
    @Value("{iam.iamkey}")
    private  String apiKey;
    @Value("{iam.iamsecret}")
    private  String apiSecret;
    @Bean
    public IamportClient iamportClient(){
        return  new IamportClient(apiKey,apiSecret);
    }
}
