package com.salanb.webapp.services;

import com.salanb.orm.session.Session;
import com.salanb.webapp.models.*;

import java.util.List;

public interface ToyStoreService {
    List<Product> getAllToys(Session session);
    Product getToyById(Session session, int Id);
    Product addToy(Session session, Product product);

    Customer login(Session session, Account user);

    Customer signUp(Session session, Account user);

    List<TransactionProduct> getProductTransactions(Session session);

    List<Customer> getCustomers(Session session);

    Customer getCustomerById(Session session, int id);

    List<Transaction> getTransactions(Session session);

    Transaction getTransactionById(Session session, int id);

    List<CartItem> addToCart(Session session, int uid, int id);

    TransactionDisplay checkout(int uid, Session session);

    Product updateToy(Session session, Product toy);

    Customer updateCustomer(Session session, Customer customer);

    Transaction updateTransaction(Session session, Transaction transaction);

    Cart updateCart(Session session, Cart cart);

    TransactionProduct updateTransactionProduct(Session session, TransactionProduct tp);

    Product deleteToy(Session session, int id);

    Customer deleteCustomer(Session session, int id);

    Transaction deleteTransaction(Session session, int id);

    boolean clearCart(Session session, int uid);

    TransactionProduct deleteTransactionProduct(Session session, int pid, int tid);

    List<CartItem> removeFromCart(Session session, int uid, int id, int amount);

    List<CartItem> getUserCart(Session session, int uid);
}
