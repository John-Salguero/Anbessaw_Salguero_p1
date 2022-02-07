package com.salanb.webapp.repositories;

import com.salanb.orm.session.Session;
import com.salanb.orm.utillities.ResourceNotFoundException;
import com.salanb.webapp.models.*;

import java.util.List;

public class ToyStoreRepoImplementation implements ToyStoreRepo {

    public ToyStoreRepoImplementation() {
    }

    @Override
    public Cart addCart(Session session, Cart ct) {
        return session.getTransaction().save(ct);
    }

    @Override
    public Cart deleteCart(Session session, Cart ct) {
        return session.getTransaction().delete(ct);
    }

    @Override
    public Cart getCart(Session session, Cart ct) {
        return session.getTransaction().get(ct);
    }

    @Override
    public List<Cart> getAllCarts(Session session) {
        return session.getTransaction().getTable(Cart.class);
    }

    @Override
    public Cart updateCart(Session session, Cart change) {
        try{
        return session.getTransaction().update(change);
        }catch (ResourceNotFoundException e) {
            return null;
        }
    }

    @Override
    public Customer addCustomer(Session session, Customer c) {
        return session.getTransaction().save(c);
    }

    @Override
    public Customer deleteCustomer(Session session, Customer c) {
        return session.getTransaction().delete(c);
    }

    @Override
    public Customer getCustomer(Session session, Customer c) {
        return session.getTransaction().get(c);
    }

    @Override
    public List<Customer> getAllCustomers(Session session) {
        return session.getTransaction().getTable(Customer.class);
    }

    @Override
    public Customer updateCustomer(Session session, Customer change) {
        try {
            return session.getTransaction().update(change);
        } catch (ResourceNotFoundException e) {
            return null;
        }
    }

    @Override
    public Product addProduct(Session session, Product p) {
        return session.getTransaction().save(p);
    }

    @Override
    public Product deleteProduct(Session session, Product p) {
        return session.getTransaction().delete(p);
    }

    @Override
    public Product getProduct(Session session, Product p) {
        return session.getTransaction().get(p);
    }

    @Override
    public List<Product> getAllProduct(Session session) {
        return session.getTransaction().getTable(Product.class);
    }

    @Override
    public Product updateProduct(Session session, Product change) {
        try {
            return session.getTransaction().update(change);
        } catch (ResourceNotFoundException e) {
            return null;
        }
    }

    @Override
    public Transaction addTransaction(Session session, Transaction t) {
        return session.getTransaction().save(t);
    }

    @Override
    public Transaction deleteTransaction(Session session, Transaction t) {
        return session.getTransaction().delete(t);
    }

    @Override
    public Transaction getTransaction(Session session, Transaction t) {
        return session.getTransaction().get(t);
    }

    @Override
    public List<Transaction> getAllTransactions(Session session) {
        return session.getTransaction().getTable(Transaction.class);
    }

    @Override
    public Transaction updateTransaction(Session session, Transaction change) {
        try {
            return session.getTransaction().update(change);
        } catch (ResourceNotFoundException e) {
            return null;
        }
    }

    @Override
    public TransactionProduct addTransactionProduct(Session session, TransactionProduct tp) {
        return session.getTransaction().save(tp);
    }

    @Override
    public TransactionProduct deleteTransactionProduct(Session session, TransactionProduct tp) {
        return session.getTransaction().delete(tp);
    }

    @Override
    public TransactionProduct getTransactionProduct(Session session, TransactionProduct tp) {
        return session.getTransaction().get(tp);
    }

    @Override
    public List<TransactionProduct> getAllTransactionProducts(Session session) {
        return session.getTransaction().getTable(TransactionProduct.class);
    }

    @Override
    public TransactionProduct updateTransactionProduct(Session session, TransactionProduct change) {
        try {
            return session.getTransaction().update(change);
        } catch (ResourceNotFoundException e) {
            return null;
        }
    }
}
