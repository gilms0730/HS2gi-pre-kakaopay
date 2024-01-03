package com.example.gms.product.service;
import com.example.gms.product.model.PaymentProducts;
import com.example.gms.product.model.Product;
import com.example.gms.product.repository.ProductRepository;
import com.siot.IamportRestClient.response.Payment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void uploadProduct(String name, Integer price){
        productRepository.save(Product.builder()
                .name(name)
                .price(price)
                .build());
    }

    public List<Product> list() {
        List<Product> result = productRepository.findAll();
        return result;
    }

    public Product read(Integer id) {
        Optional<Product> result = productRepository.findById(id);
        if (result.isPresent()) {
            Product product = result.get();

            return Product.builder()
                    .id(product.getId())
                    .price(product.getPrice())
                    .name(product.getName())
                    .build();
        } else {
            return null;
        }
    }
    public void update(Product product) {
        productRepository.save(Product.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .build());
    }

    public void delete(Integer id) {
        productRepository.delete(Product.builder().id(id).build());
    }

    public Integer getTotalPrice(PaymentProducts datas){

        List<Integer> productIds = new ArrayList<>();
        for (Product product: datas.getProducts()) {
            productIds.add(product.getId());
        }

        List<Product> products = productRepository.findAllById(productIds);

        Integer totalPrice = 0;
        for (Product product: products) {
            totalPrice += product.getPrice();
        }

        return  totalPrice;
    }

}
