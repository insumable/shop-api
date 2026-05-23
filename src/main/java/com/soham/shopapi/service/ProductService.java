package com.soham.shopapi.service;

import com.soham.shopapi.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {  // also remove @Component
    List<Product> getAllProducts();
    Product getProductById(int id);
    Product addProduct(Product product, MultipartFile imageFile) throws IOException;  // ← add throws
    Product updateProduct(int id, Product product, MultipartFile imageFile) throws IOException;  // ← add throws
    void deleteProduct(int id);
    List<Product> searchProducts(String keyword);
}