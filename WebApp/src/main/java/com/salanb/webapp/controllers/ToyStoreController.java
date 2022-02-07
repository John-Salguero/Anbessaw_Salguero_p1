package com.salanb.webapp.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.salanb.orm.session.Session;
import com.salanb.webapp.models.*;
import com.salanb.webapp.services.ToyStoreService;
import com.salanb.webapp.utilities.GsonSingleton;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class ToyStoreController {

    private final ToyStoreService tsS;
    private final Gson gson;

    public ToyStoreController(ToyStoreService tsS){
        this.tsS = tsS;
        gson = GsonSingleton.getInstance().getGson();
    }

    public void getToys(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Session session = (Session)request.getSession().getAttribute("session");
        String uri = request.getRequestURI();
        String[] splitURI = uri.split("/");
        if(splitURI.length == 3) {
            List<Product> toys = tsS.getAllToys(session);
            response.getWriter().append(gson.toJson(toys));
        }
        else if(splitURI.length == 4){
            int id;
            try {
                id = Integer.parseInt(splitURI[3]);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            Product toy  = tsS.getToyById(session, id);
            response.getWriter().append(gson.toJson(toy));
        }
        else
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    public void addToy(HttpServletRequest request, HttpServletResponse response) throws IOException {

        if(!(Boolean) request.getSession().getAttribute("admin")){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Session session = (Session)request.getSession().getAttribute("session");
        String uri = request.getRequestURI();
        String[] splitURI = uri.split("/");
        if(splitURI.length == 4) {
            // Extract Data from the Request
            Product product;
            try {
                BufferedReader reader = request.getReader();
                product = gson.fromJson(reader, Product.class);
            } catch (JsonSyntaxException | JsonIOException e){
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            product = tsS.addToy(session, product);
            if(product != null){
                response.getWriter().append(gson.toJson(product));
            }
            else{
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
        else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }

    }

    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {

        if((Boolean) request.getSession().getAttribute("loggedIn")){
            response.sendError(HttpServletResponse.SC_CONFLICT, "you are already logged in");
            return;
        }

        String uri = request.getRequestURI();
        String[] splitURI = uri.split("/");

        if(splitURI.length != 4) // if the url is malformed
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        else {                  // if the url is good
            Session session = (Session)request.getSession().getAttribute("session");
            Account user;
            try {
                BufferedReader reader = request.getReader();
                user = gson.fromJson(reader, Account.class);
            } catch (JsonSyntaxException | JsonIOException e){
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            Customer customer = tsS.login(session, user);
            if(customer != null) {
                request.getSession().setAttribute("uid", customer.getId());
                request.getSession().setAttribute("loggedIn", Boolean.valueOf("true"));
                if("admin".equals(customer.getUsername()))
                    request.getSession().setAttribute("admin", Boolean.valueOf("true"));
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().append("Successfully logged in.");
            }
            else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    public void getWelcome(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.getWriter().append("Welcome to Thomas Anbessa and John Salguero's ToyStore\n");
        response.getWriter().append("To Display our product make a get request to \"/SalAnbToyStore/toys\"\n");
        response.getWriter().append("To see an individual product make a get request to  \"/SalAnbToyStore/toys/{id}\"\n");
    }

    public void signUp(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uri = request.getRequestURI();
        String[] splitURI = uri.split("/");

        if(splitURI.length != 4) // if the url is malformed
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        else {                  // if the url is good
            Session session = (Session)request.getSession().getAttribute("session");
            Account user;
            try {
                BufferedReader reader = request.getReader();
                user = gson.fromJson(reader, Account.class);
            } catch (JsonSyntaxException | JsonIOException e){
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            Customer cust = tsS.signUp(session, user);
            if(cust != null) {
                request.getSession().setAttribute("uid", cust.getId());
                request.getSession().setAttribute("loggedIn", Boolean.valueOf("true"));
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().append("Successfully registered and signed in.\n");
            }
            else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    public void getProductTransaction(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(!(Boolean) request.getSession().getAttribute("admin")){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Session session = (Session)request.getSession().getAttribute("session");
        String uri = request.getRequestURI();
        String[] splitURI = uri.split("/");
        if(splitURI.length == 3) {
            List<TransactionProduct> tps = tsS.getProductTransactions(session);
            response.getWriter().append(gson.toJson(tps));
        }
        else if (splitURI.length == 4){
            int id;
            try {
                id = Integer.parseInt(splitURI[2]);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            Product toy  = tsS.getToyById(session, id);
            response.getWriter().append(gson.toJson(toy));
        } else
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    public void getCustomers(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(!(Boolean) request.getSession().getAttribute("admin")){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Session session = (Session)request.getSession().getAttribute("session");
        String uri = request.getRequestURI();
        String[] splitURI = uri.split("/");
        if(splitURI.length == 3) {
            List<Customer> customers = tsS.getCustomers(session);
            response.getWriter().append(gson.toJson(customers));
        }
        else if(splitURI.length == 4){
            int id;
            try {
                id = Integer.parseInt(splitURI[3]);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            Customer customer  = tsS.getCustomerById(session, id);
            response.getWriter().append(gson.toJson(customer));
        } else
            response.sendError(HttpServletResponse.SC_BAD_GATEWAY);
    }

    public void getTransactions(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(!(Boolean) request.getSession().getAttribute("admin")){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Session session = (Session)request.getSession().getAttribute("session");
        String uri = request.getRequestURI();
        String[] splitURI = uri.split("/");
        if(splitURI.length == 3) {
            List<Transaction> transactions = tsS.getTransactions(session);
            response.getWriter().append(gson.toJson(transactions));
        }
        else if(splitURI.length == 4){
            int id;
            try {
                id = Integer.parseInt(splitURI[3]);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            Transaction transaction  = tsS.getTransactionById(session, id);
            response.getWriter().append(gson.toJson(transaction));
        }
        else
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    public void getCarts(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(!(Boolean) request.getSession().getAttribute("admin")){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Session session = (Session)request.getSession().getAttribute("session");
        String uri = request.getRequestURI();
        String[] splitURI = uri.split("/");
        if(splitURI.length == 3) {
            List<Customer> customers = tsS.getCustomers(session);
            response.getWriter().append(gson.toJson(customers));
        }
        else if(splitURI.length == 4){
            int id;
            try {
                id = Integer.parseInt(splitURI[3]);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            Customer customer  = tsS.getCustomerById(session, id);
            response.getWriter().append(gson.toJson(customer));
        } else
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    public void addToCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(!(Boolean) request.getSession().getAttribute("loggedIn")){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Session session = (Session)request.getSession().getAttribute("session");
        String uri = request.getRequestURI();
        String[] splitURI = uri.split("/");
        if(splitURI.length != 4) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int id;
        int uid =(Integer) request.getSession().getAttribute("uid");
        try {
            id = Integer.parseInt(splitURI[3]);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        List<CartItem> cart = tsS.addToCart(session, uid, id);
        response.getWriter().append(gson.toJson(cart));
    }

    public void checkout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(!(Boolean) request.getSession().getAttribute("loggedIn")){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Session session = (Session)request.getSession().getAttribute("session");
        String uri = request.getRequestURI();
        String[] splitURI = uri.split("/");
        if(splitURI.length != 3) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int uid =(Integer) request.getSession().getAttribute("uid");
        TransactionDisplay transaction = tsS.checkout(uid, session);
        if(transaction != null)
            response.getWriter().append(gson.toJson(transaction));
        else
            response.sendError(HttpServletResponse.SC_CONFLICT);
    }

    public void updateToys(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(!(Boolean) request.getSession().getAttribute("admin")){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String uri = request.getRequestURI();
        String[] splitURI = uri.split("/");

        if(splitURI.length != 3) // if the url is malformed
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        else {                  // if the url is good
            Session session = (Session) request.getSession().getAttribute("session");
            Product toy;
            try {
                BufferedReader reader = request.getReader();
                toy = gson.fromJson(reader, Product.class);
            } catch (JsonSyntaxException | JsonIOException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            toy = tsS.updateToy(session, toy);
            if(toy != null)
                response.getWriter().append(gson.toJson(toy));
            else
                response.sendError(HttpServletResponse.SC_CONFLICT);
        }
    }

    public void updateCustomers(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(!(Boolean) request.getSession().getAttribute("admin")){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String uri = request.getRequestURI();
        String[] splitURI = uri.split("/");

        if(splitURI.length != 3) // if the url is malformed
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        else {                  // if the url is good
            Session session = (Session) request.getSession().getAttribute("session");
            Customer customer;
            try {
                BufferedReader reader = request.getReader();
                customer = gson.fromJson(reader, Customer.class);
            } catch (JsonSyntaxException | JsonIOException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            customer = tsS.updateCustomer(session, customer);
            if(customer != null)
                response.getWriter().append(gson.toJson(customer));
            else
                response.sendError(HttpServletResponse.SC_CONFLICT);
        }
    }

    public void updateTransactions(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(!(Boolean) request.getSession().getAttribute("admin")){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String uri = request.getRequestURI();
        String[] splitURI = uri.split("/");

        if(splitURI.length != 3) // if the url is malformed
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        else {                  // if the url is good
            Session session = (Session) request.getSession().getAttribute("session");
            Transaction transaction;
            try {
                BufferedReader reader = request.getReader();
                transaction = gson.fromJson(reader, Transaction.class);
            } catch (JsonSyntaxException | JsonIOException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            transaction = tsS.updateTransaction(session, transaction);
            if(transaction != null)
                response.getWriter().append(gson.toJson(transaction));
            else
                response.sendError(HttpServletResponse.SC_CONFLICT);
        }
    }

    public void updateCarts(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(!(Boolean) request.getSession().getAttribute("admin")){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String uri = request.getRequestURI();
        String[] splitURI = uri.split("/");

        if(splitURI.length != 3) // if the url is malformed
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        else {                  // if the url is good
            Session session = (Session) request.getSession().getAttribute("session");
            Cart cart;
            try {
                BufferedReader reader = request.getReader();
                cart = gson.fromJson(reader, Cart.class);
            } catch (JsonSyntaxException | JsonIOException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            cart = tsS.updateCart(session, cart);
            if(cart != null)
                response.getWriter().append(gson.toJson(cart));
            else
                response.sendError(HttpServletResponse.SC_CONFLICT);
        }
    }

    public void updateProductTransaction(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(!(Boolean) request.getSession().getAttribute("admin")){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String uri = request.getRequestURI();
        String[] splitURI = uri.split("/");

        if(splitURI.length != 3) // if the url is malformed
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        else {                  // if the url is good
            Session session = (Session) request.getSession().getAttribute("session");
            TransactionProduct tp;
            try {
                BufferedReader reader = request.getReader();
                tp = gson.fromJson(reader, TransactionProduct.class);
            } catch (JsonSyntaxException | JsonIOException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            tp = tsS.updateTransactionProduct(session, tp);
            if(tp != null)
                response.getWriter().append(gson.toJson(tp));
            else
                response.sendError(HttpServletResponse.SC_CONFLICT);
        }
    }

    public void deleteToys(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(!(Boolean) request.getSession().getAttribute("admin")){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Session session = (Session)request.getSession().getAttribute("session");
        String uri = request.getRequestURI();
        String[] splitURI = uri.split("/");
        if(splitURI.length != 4) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int id;
        try {
            id = Integer.parseInt(splitURI[3]);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Product deletedToy = tsS.deleteToy(session, id);
        if(deletedToy != null)
            response.getWriter().append(gson.toJson(deletedToy));
        else
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    public void deleteCustomers(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(!(Boolean) request.getSession().getAttribute("admin")){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Session session = (Session)request.getSession().getAttribute("session");
        String uri = request.getRequestURI();
        String[] splitURI = uri.split("/");
        if(splitURI.length != 4) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int id;
        try {
            id = Integer.parseInt(splitURI[3]);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Customer deletedCustomer = tsS.deleteCustomer(session, id);
        if(deletedCustomer != null)
            response.getWriter().append(gson.toJson(deletedCustomer));
        else
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    public void deleteTransactions(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(!(Boolean) request.getSession().getAttribute("admin")){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Session session = (Session)request.getSession().getAttribute("session");
        String uri = request.getRequestURI();
        String[] splitURI = uri.split("/");
        if(splitURI.length != 4) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int id;
        try {
            id = Integer.parseInt(splitURI[3]);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Transaction deletedTransaction = tsS.deleteTransaction(session, id);
        if(deletedTransaction != null)
            response.getWriter().append(gson.toJson(deletedTransaction));
        else
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    public void clearCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(!(Boolean) request.getSession().getAttribute("loggedIn")){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Session session = (Session)request.getSession().getAttribute("session");
        String uri = request.getRequestURI();
        String[] splitURI = uri.split("/");
        if(splitURI.length != 3) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if( tsS.clearCart(session, (Integer )request.getAttribute("uid")))
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        else
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    public void deleteProductTransaction(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(!(Boolean) request.getSession().getAttribute("admin")){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Session session = (Session)request.getSession().getAttribute("session");
        String uri = request.getRequestURI();
        String[] splitURI = uri.split("/");
        if(splitURI.length != 3) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String transaction = request.getParameter("transaction");
        String product = request.getParameter("product");
        int tid;
        int pid;
        try {
            tid = Integer.parseInt(transaction);
            pid = Integer.parseInt(product);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        TransactionProduct tp = tsS.deleteTransactionProduct(session, pid, tid);
        if(tp != null)
            response.getWriter().append(gson.toJson(tp));
        else
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    public void removeFromCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(!(Boolean) request.getSession().getAttribute("loggedIn")){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Session session = (Session)request.getSession().getAttribute("session");
        String uri = request.getRequestURI();
        String[] splitURI = uri.split("/");
        if(splitURI.length != 4) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String user = (String)request.getSession().getAttribute("uid");
        String amount = (String)request.getAttribute("amount");
        String product = splitURI[3];
        int uid;
        int pid;
        int amnt;
        try {
            uid = Integer.parseInt(user);
            pid = Integer.parseInt(product);
            if(amount != null)
                amnt = Integer.parseInt(amount);
            else
                amnt = 1;
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        List<CartItem> cart = tsS.removeFromCart(session, uid, pid, amnt);
        if(cart != null) {
            response.getWriter().append("Successfully removied ").
                    append(String.valueOf(amnt)).append(" of item ").
                    append(String.valueOf(pid)).append("from your cart\n");
            response.getWriter().append("This is what remains\n");
            response.getWriter().append(gson.toJson(cart));
        }else
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    public void showMyCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(!(Boolean) request.getSession().getAttribute("loggedIn")){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Session session = (Session)request.getSession().getAttribute("session");
        String uri = request.getRequestURI();
        String[] splitURI = uri.split("/");
        if(splitURI.length != 3) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String user = (String)request.getSession().getAttribute("uid");
        int uid;
        try {
            uid = Integer.parseInt(user);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        List<CartItem> cart = tsS.getUserCart(session, uid);
        if(cart != null) {
            response.getWriter().append(gson.toJson(cart));
        }else
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }
}
