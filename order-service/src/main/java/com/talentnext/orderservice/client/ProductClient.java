package com.talentnext.orderservice.client;

import com.talentnext.orderservice.dto.ProductDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class ProductClient {

    private final RestTemplate restTemplate;
    private final String productServiceUrl;

    public ProductClient(RestTemplate restTemplate,
                          @Value("${product.service.url}") String productServiceUrl) {
        this.restTemplate = restTemplate;
        this.productServiceUrl = productServiceUrl;
    }

    public ProductDto getProductById(Long productId) {
        try {
            return restTemplate.getForObject(productServiceUrl + "/" + productId, ProductDto.class);
        } catch (HttpClientErrorException.NotFound ex) {
            return null;
        }
    }
}
