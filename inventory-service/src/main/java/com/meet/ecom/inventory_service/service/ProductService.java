package com.meet.ecom.inventory_service.service;

import com.meet.ecom.inventory_service.clients.OrdersFeignClient;
import com.meet.ecom.inventory_service.dto.OrderRequestDto;
import com.meet.ecom.inventory_service.dto.OrderRequestItemDto;
import com.meet.ecom.inventory_service.dto.ProductDto;
import com.meet.ecom.inventory_service.entity.Product;
import com.meet.ecom.inventory_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final OrdersFeignClient ordersFeignClient;


    public List<ProductDto> getAllInventory() {
        List<Product> inventories = productRepository.findAll();
        return inventories.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .toList();
    }

    // Get product by ID
    public ProductDto getProductById(Long id) {
        Optional<Product> inventory = productRepository.findById(id);
        return inventory.map(product -> modelMapper.map(product, ProductDto.class))
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Transactional
    public Double reduceStock(OrderRequestDto orderRequestDto) {
        log.info("Reducing stock for order: {}", orderRequestDto);
        Double totalPrice = 0.0;
        for(OrderRequestItemDto orderRequestItemDto: orderRequestDto.getItems()) {
            Long productId = orderRequestItemDto.getProductId();
            Integer quantity = orderRequestItemDto.getQuantity();

             Product product = productRepository.findById(productId).orElseThrow(
                    () -> new RuntimeException("Product not found with id: " + productId));

            if(product.getStock() < quantity) {
                throw new RuntimeException("Not enough stock for product: " + product.getTitle() + " with stock: " + product.getStock());
            }

             product.setStock(product.getStock() - quantity);
            productRepository.save(product);
            totalPrice += product.getPrice() * quantity;

        }
        return totalPrice;
    }

    @Transactional
    public void revertInventory(Long orderId) {
        log.info("Reverting stock for order Id: {}", orderId);

        OrderRequestDto orderRequestDto = ordersFeignClient.getOrderById(orderId);

        for(OrderRequestItemDto orderRequestItemDto: orderRequestDto.getItems()) {
            Long productId = orderRequestItemDto.getProductId();
            Integer quantity = orderRequestItemDto.getQuantity();

            Product product = productRepository.findById(productId).orElseThrow(
                    () -> new RuntimeException("Product not found with id: " + productId));

            product.setStock(product.getStock() + quantity);
            productRepository.save(product);
        }
    }
}
