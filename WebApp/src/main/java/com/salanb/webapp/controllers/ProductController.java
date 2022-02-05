package com.salanb.webapp.controllers;

import com.google.gson.Gson;
import com.salanb.orm.utillities.ResourceNotFoundException;
import com.salanb.webapp.models.Product;
import com.salanb.webapp.services.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductController {

    ProductService ps;
    Gson gson = new Gson();

    public ProductController(ProductService ps) {
        this.ps = ps;

    }

    public void getProductById(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String input = request.getAttribute("id").toString();
        int id = 0;
        try {
            id = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID");
        }
        Product p = ps.getProduct(id);

        response.getWriter().append((p != null)?gson.toJson(p): "{}");
    }

    public void getProduct(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Product> productList = new ArrayList<>();
        String price = request.getParameter("price");

        if(price == null) {
            productList = ps.getAllProducts();
        } else {
            try {
              BigDecimal priceNum = new BigDecimal(price);
              productList = ps.getALLProductsAbovePrice(priceNum);
              productList = ps.getAllProductsBelowPrice(priceNum);
              productList = ps.getAllProductsBetweenPrice(priceNum);
            } catch (NumberFormatException e) {
                    response.sendError(400, "Price is not a number.");
            }
        }

        response.getWriter().append(gson.toJson(productList));

    }
    public void addProduct(HttpServletRequest request, HttpServletResponse response) throws IOException {

        BufferedReader reader = request.getReader();
        Product p = gson.fromJson(reader, Product.class);


        p = ps.addProduct(p);


        if(p != null) {
            response.setStatus(201);
            response.getWriter().append(gson.toJson(p));
        } else {
            response.getWriter().append("{}");
        }

    }
    public void updateProduct(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Product p = gson.fromJson(request.getReader(), Product.class);
        p.setId((int) request.getAttribute("id"));

        p = ps.updateProduct(p);

        response.getWriter().append((p != null) ? gson.toJson(p) : "{}");
    }

    public void deleteProduct(HttpServletRequest request, HttpServletResponse response) throws ResourceNotFoundException {

        int id = (int) request.getAttribute("id");

        Product p = ps.deleteProduct(id);

        response.setStatus(204);
    }
}
