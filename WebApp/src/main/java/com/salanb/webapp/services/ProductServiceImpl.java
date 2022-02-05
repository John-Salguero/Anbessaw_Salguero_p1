package com.salanb.webapp.services;

import com.salanb.orm.utillities.ResourceNotFoundException;
import com.salanb.webapp.models.Product;
import com.salanb.webapp.repositories.ProductRepo;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductServiceImpl implements ProductService {

    private final ProductRepo pr;


    public ProductServiceImpl(ProductRepo pr) {

        this.pr = pr;
    }

    @Override
    public Product addProduct(Product p) {
        try {
            return pr.addProduct(p);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return p;
    }

    @Override
    public Product getProduct(int id) {
        return pr.getProduct(id);
    }

    @Override
    public List<Product> getAllProducts() {
        return pr.getAllProducts();
    }

    @Override
    public Product updateProduct(Product change) {
        return pr.updateProduct(change);
    }

    @Override
    public Product deleteProduct(int id) throws ResourceNotFoundException {
        return pr.deleteProduct(id);
    }

    @Override
    public List<Product> getALLProductsAbovePrice(BigDecimal price) {

        List<Product> allProducts = pr.getAllProducts();


        List<Product> filteredProducts = new ArrayList<>();
        for(int i = 0; i < allProducts.size(); i++) {
            Product p = allProducts.get(i);
            if(p.getPrice() > price) {
                filteredProducts.add(p);
            }
        }
        return filteredProducts;
    }

    @Override
    public List<Product> getAllProductsBelowPrice(BigDecimal price) {
        List<Product> allProducts = pr.getAllProducts();


        List<Product> filteredProducts = new ArrayList<>();
        for(int i = 0; i < allProducts.size(); i++) {
            Product p = allProducts.get(i);
            if(p.getPrice() < price) {
                filteredProducts.add(p);
            }
        }
        return filteredProducts;
    }

    @Override
    public List<Product> getAllProductsBetweenPrice(BigDecimal price) {
        List<Product> allProducts = pr.getAllProducts();


        List<Product> filteredProducts = new ArrayList<>();
        for(int i = 0; i < allProducts.size(); i++) {
            Product p = allProducts.get(i);
            if(price <= p.getPrice() <= price) {
                filteredProducts.add(p);
            }
        }
        return filteredProducts;
    }

    @Override
    public List<Product> getAllAvailableProducts() {
        return null;
    }

    @Override
    public List<Product> getAllUnavailableProducts() {
        return null;
    }
}
