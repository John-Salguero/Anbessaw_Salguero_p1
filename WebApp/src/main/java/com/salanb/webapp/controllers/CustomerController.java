package com.salanb.webapp.controllers;

import com.google.gson.Gson;
import com.salanb.webapp.models.Customer;
import com.salanb.webapp.models.Product;
import com.salanb.webapp.services.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CustomerController {

    CustomerService cs;
    Gson gson = new Gson();

    public CustomerController(CustomerService cs) {
        this.cs = cs;
    }
    public void getProductById(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String input = request.getAttribute("id").toString();
        int id = 0;
        try {
            id = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID");
        }
        Customer c = cs.getCustomer(id);

        response.getWriter().append((c != null)?gson.toJson(c): "{}");
    }

    public void addCustomer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BufferedReader reader = request.getReader();
        Customer c = gson.fromJson(reader, Customer.class);


        c = addCustomer(c);


        if(c != null) {
            response.setStatus(201);
            response.getWriter().append(gson.toJson(c));
        } else {
            response.getWriter().append("{}");
        }
    }

    public void getCustomer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Customer> customerList = new ArrayList<>();
        String price = request.getParameter("price");

        if(price == null) {
            customerList = cs.getAllCustomers();
        } else {
            try {
                BigDecimal priceNum = new BigDecimal(price);
                customerList = ps.getALLProductsAbovePrice(priceNum);
                customerList = ps.getAllProductsBelowPrice(priceNum);
                productList = ps.getAllProductsBetweenPrice(priceNum);
            } catch (NumberFormatException e) {
                response.sendError(400, "Price is not a number.");
            }
        }

        response.getWriter().append(gson.toJson(productList));

    }
}
