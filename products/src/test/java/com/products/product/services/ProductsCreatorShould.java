package com.products.product.services;

import com.products.product.domain.Product;
import com.products.product.domain.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductsCreatorShould {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductsCreator productsCreator;

    @Test
    void save_shouldCallRepositoryWithCorrectProduct() {
        UUID id = UUID.randomUUID();
        String name = "Test Product";
        String description = "Test Description";
        double price = 99.99;
        UUID brandId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        Product expectedProduct = Product.create(id, name, description, price, brandId, categoryId);

        when(productRepository.save(any(Product.class)))
                .thenReturn(Mono.empty());

        Mono<Void> result = productsCreator.save(id, name, description, price, brandId, categoryId);

        StepVerifier.create(result)
                .verifyComplete();

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository, times(1)).save(captor.capture());
        Product savedProduct = captor.getValue();

        assertEquals(expectedProduct.getId(), savedProduct.getId());
        assertEquals(expectedProduct.getName(), savedProduct.getName());
        assertEquals(expectedProduct.getDescription(), savedProduct.getDescription());
        assertEquals(expectedProduct.getPrice(), savedProduct.getPrice());
        assertEquals(expectedProduct.getBrandId(), savedProduct.getBrandId());
        assertEquals(expectedProduct.getCategoryId(), savedProduct.getCategoryId());
    }
}
