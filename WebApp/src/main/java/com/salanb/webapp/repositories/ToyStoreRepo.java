package com.salanb.webapp.repositories;

import com.salanb.orm.session.Session;
import com.salanb.webapp.models.*;

import java.util.List;

public interface ToyStoreRepo {
    public Cart addCart(Session session, Cart ct);
    public Cart deleteCart(Session session, Cart ct);
    public Cart getCart(Session session, Cart ct);
    public List<Cart> getAllCarts(Session session);
    public Cart updateCart(Session session, Cart change);

    public Customer addCustomer(Session session, Customer c);
    public Customer deleteCustomer(Session session, Customer c);
    public Customer getCustomer(Session session, Customer c);
    public List<Customer> getAllCustomers(Session session);
    public Customer updateCustomer(Session session,Customer change);

    public Product addProduct(Session session, Product p);
    public Product deleteProduct(Session session, Product p);
    public Product getProduct(Session session, Product p);
    public List<Product> getAllProduct(Session session);
    public Product updateProduct(Session session, Product change);

    public Transaction addTransaction(Session session, Transaction t);
    public Transaction deleteTransaction(Session session, Transaction t);
    public Transaction getTransaction(Session session, Transaction t);
    public List<Transaction> getAllTransactions(Session session);
    public Transaction updateTransaction(Session session, Transaction change);

    public TransactionProduct addTransactionProduct(Session session, TransactionProduct tp);
    public TransactionProduct deleteTransactionProduct(Session session, TransactionProduct tp);
    public TransactionProduct getTransactionProduct(Session session, TransactionProduct tp);
    public List<TransactionProduct> getAllTransactionProducts(Session session);
    public TransactionProduct updateTransactionProduct(Session session, TransactionProduct change);
}
