package com.salanb.webapp.services;

import com.salanb.orm.session.Session;
import com.salanb.orm.utillities.HashGenerator;
import com.salanb.webapp.models.*;
import com.salanb.webapp.repositories.ToyStoreRepo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ToyStoreServiceImplementation implements ToyStoreService {


    private final ToyStoreRepo tsR;

    public ToyStoreServiceImplementation(ToyStoreRepo tsR) {
        this.tsR = tsR;
    }

    @Override
    public List<Product> getAllToys(Session session) {
        return tsR.getAllProduct(session);
    }

    @Override
    public Product getToyById(Session session, int id) {
        return tsR.getProduct(session, new Product(id));
    }

    @Override
    public Product addToy(Session session, Product product) {
        return tsR.addProduct(session, product);
    }

    @Override
    public Customer login(Session session, Account user) {
        List<Customer> users = tsR.getAllCustomers(session);

        users = users.stream().filter(
                (elm)-> elm.getUsername().equals(user.getUsername())).collect(Collectors.toList());
        if(users.isEmpty())
            return null;
        Customer cust = users.get(0);

        String passHash = HashGenerator.getInstance().getMessageDigestString(user.getPassword());
        if(cust.getPassword().equals(passHash))
        {
            return cust;
        }
        return null;
    }

    @Override
    public Customer signUp(Session session, Account user) {
        List<Customer> users = tsR.getAllCustomers(session);

        users = users.stream().filter(
                (elm)-> elm.getUsername().equals(user.getUsername())).collect(Collectors.toList());
        if(!users.isEmpty())
            return null;

        String passHash = HashGenerator.getInstance().getMessageDigestString(user.getPassword());
        Customer cust = new Customer();
        cust.setPassword(passHash);
        cust.setUsername(user.getUsername());
        cust = tsR.addCustomer(session, cust);

        return cust;
    }

    @Override
    public List<TransactionProduct> getProductTransactions(Session session) {
        return tsR.getAllTransactionProducts(session);
    }

    @Override
    public List<Customer> getCustomers(Session session) {
        return tsR.getAllCustomers(session);
    }

    @Override
    public Customer getCustomerById(Session session, int id) {
        return tsR.getCustomer(session, new Customer(id));
    }

    @Override
    public List<Transaction> getTransactions(Session session) {
        return tsR.getAllTransactions(session);
    }

    @Override
    public Transaction getTransactionById(Session session, int id) {
        return tsR.getTransaction(session, new Transaction(id));
    }

    @Override
    public List<CartItem> addToCart(Session session, int uid, int id) {
        // Add the product to the user's cart, then return all the products in the cart
        Cart cart = new Cart(uid, id);
        Cart prevCart = tsR.getCart(session, cart);
        if(prevCart != null)
            cart.setAmount(prevCart.getAmount() + 1);
        else
            cart.setAmount(1);
        tsR.addCart(session, cart);

        // get all the products in the user's cart
        List<Cart> allCarts = tsR.getAllCarts(session);
        List<Cart> UsersCart = allCarts.stream().filter((ct) -> ct.getCustomerId() == uid).collect(Collectors.toList());

        List<CartItem> retVal = new ArrayList<>();
        for(Cart elem : UsersCart){
            CartItem item = new CartItem();
            item.setItem(tsR.getProduct(session, new Product(elem.getProductId())));
            item.setAmount(elem.getAmount());
            retVal.add(item);
        }

        return retVal;
    }

    @Override
    public TransactionDisplay checkout(int uid, Session session) {
        // get all the products in the user's cart
        List<Cart> allCarts = tsR.getAllCarts(session);
        List<Cart> UsersCart = allCarts.stream().filter((ct) -> ct.getCustomerId() == uid).collect(Collectors.toList());


        BigDecimal subtotal = BigDecimal.ZERO;
        List<CartItem> productList = new ArrayList<>();
        for(Cart elem : UsersCart){
            Product item = tsR.getProduct(session, new Product(elem.getProductId()));
            CartItem newCartItem = new CartItem(item, elem.getAmount());
            productList.add(newCartItem);
            subtotal = subtotal.add(item.getPrice().multiply(BigDecimal.valueOf(elem.getAmount())));
        }

        Transaction newTransaction = new Transaction();
        newTransaction.setCustomerId(uid);
        newTransaction.setSubtotal(subtotal);
        newTransaction.setDate(System.currentTimeMillis());

        newTransaction = tsR.addTransaction(session, newTransaction);
        if(newTransaction == null)
            return null;
        TransactionDisplay retVal = new TransactionDisplay(newTransaction);
        retVal.setToys(productList);
        for(Cart elem : UsersCart){
            TransactionProduct tp = new TransactionProduct();
            tp.setProductId(elem.getProductId());
            tp.setAmount(elem.getAmount());
            tp.setTransactionId(newTransaction.getId());
            tsR.addTransactionProduct(session, tp);
        }

        return retVal;
    }

    @Override
    public Product updateToy(Session session, Product toy) {
        return tsR.updateProduct(session, toy);
    }

    @Override
    public Customer updateCustomer(Session session, Customer customer) {
        return tsR.updateCustomer(session, customer);
    }

    @Override
    public Transaction updateTransaction(Session session, Transaction transaction) {
        return tsR.updateTransaction(session, transaction);
    }

    @Override
    public Cart updateCart(Session session, Cart cart) {
        return tsR.updateCart(session, cart);
    }

    @Override
    public TransactionProduct updateTransactionProduct(Session session, TransactionProduct tp) {
        return tsR.updateTransactionProduct(session, tp);
    }

    @Override
    public Product deleteToy(Session session, int id) {
        return tsR.deleteProduct(session, new Product(id));
    }

    @Override
    public Customer deleteCustomer(Session session, int id) {
        return tsR.deleteCustomer(session, new Customer(id));
    }

    @Override
    public Transaction deleteTransaction(Session session, int id) {
        return tsR.deleteTransaction(session, new Transaction(id));
    }

    @Override
    public boolean clearCart(Session session, int uid) {
        List<Cart> carts = tsR.getAllCarts(session);
        Stream<Cart> userCarts = carts.stream().filter((elem)->elem.getCustomerId() == uid);
        userCarts.forEach((elem)->tsR.deleteCart(session, elem));
        return true;
    }

    @Override
    public TransactionProduct deleteTransactionProduct(Session session, int pid, int tid) {
        return tsR.deleteTransactionProduct(session, new TransactionProduct(tid, pid));
    }

    @Override
    public List<CartItem> removeFromCart(Session session, int uid, int pid, int amount) {
        Cart cart = tsR.getCart(session, new Cart(uid, pid));
        if(cart == null || cart.getAmount() < amount)
            return null;
        cart.setAmount(cart.getAmount() - amount);
        if(cart.getAmount() < 1)
            tsR.deleteCart(session, cart);
        else
            tsR.updateCart(session, cart);

        // get all the products in the user's cart
        List<Cart> allCarts = tsR.getAllCarts(session);
        List<Cart> UsersCart = allCarts.stream().filter((ct) -> ct.getCustomerId() == uid).collect(Collectors.toList());

        List<CartItem> retVal = new ArrayList<>();
        for(Cart elem : UsersCart){
            CartItem item = new CartItem();
            item.setItem(tsR.getProduct(session, new Product(elem.getProductId())));
            item.setAmount(elem.getAmount());
            retVal.add(item);
        }

        return retVal;
    }

    @Override
    public List<CartItem> getUserCart(Session session, int uid) {
        // get all the products in the user's cart
        List<Cart> allCarts = tsR.getAllCarts(session);
        List<Cart> UsersCart = allCarts.stream().filter((ct) -> ct.getCustomerId() == uid).collect(Collectors.toList());

        List<CartItem> retVal = new ArrayList<>();
        for(Cart elem : UsersCart){
            CartItem item = new CartItem();
            item.setItem(tsR.getProduct(session, new Product(elem.getProductId())));
            item.setAmount(elem.getAmount());
            retVal.add(item);
        }

        return retVal;
    }
}
