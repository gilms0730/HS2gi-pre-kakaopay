package com.example.gms.product.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("*")
public class OrderController {
    @RequestMapping("/get/token")
    public String getToken() throws IOException {
        HttpsURLConnection conn= null;

        URL url = new URL("https://api.iamport.kr/users/getToken");
        conn = (HttpsURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-type","application/json");
        conn.setRequestProperty("Accept","apploication/json");
        conn.setDoOutput(true);

        JsonObject json = new JsonObject();
        json.addProperty("imp_key","3717632236770265");
        json.addProperty("imp_secret","NUWRQk8v8Rnms9OPxe7wDba1F3qNCieuLkJEI1jwRsyh8vtqUwgzybvn5Y54dF1Ceu229Hm9VfLsdSzB");

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
        bw.write(json.toString());
        bw.flush();
        bw.close();

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
        Gson gson = new Gson();
        String response = gson.fromJson(br.readLine(), Map.class).get("response").toString();

        String token = gson.fromJson(response, Map.class).get("access_token").toString();
        br.close();
        conn.disconnect();

        return token;
    }
    @RequestMapping(method= RequestMethod.GET, value = "/get/info")
    public Map <String,String> getPaymentInfo(String impUid) throws IOException{
        String token = getToken();
        HttpsURLConnection conn= null;

        URL url = new URL("https://api.iamport.kr/payments/"+impUid);
        conn = (HttpsURLConnection)url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization",token);
        conn.setDoOutput(true);

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
        Gson gson = new Gson();
        String response = gson.fromJson(br.readLine(), Map.class).get("response").toString();

        br.close();
        conn.disconnect();
        String amount = response.split("amount")[1].split(",")[0].replace("=","");
        String name =  response.split("name")[1].split(",")[0].replace("=","");

        Map<String, String> result = new HashMap<>();
        result.put("name", name);
        result.put("amount",amount);
        return result;
    }

    @RequestMapping("/validation")
    public ResponseEntity paymentValidation(String impUid) throws IOException{
        String dbPrice = "50000.0";//db에서 값을 조회하는 것
        Map<String,String>paymentResult = getPaymentInfo(impUid);
        if(paymentResult.get("amount").equals(dbPrice)){
            return ResponseEntity.ok().body("ok");
        }
        else {
            String token = getToken();

            payMentCancel(token, impUid, paymentResult.get("amount"), "결제 금액 에러");
            return ResponseEntity.ok().body("error");
        }
    }

    private void payMentCancel(String token, String impUid, String amount, String reason) {

    }
}
