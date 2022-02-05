package com.salanb.webapp.services;

import com.salanb.orm.utillities.ResourceNotFoundException;
import com.salanb.webapp.models.Product;
import com.salanb.webapp.models.Transaction;

import java.util.List;

public interface TransactionService {
    public Transaction addTransaction(Transaction t);
    public Transaction getTransaction(int id);
    public List<Transaction> getAllTransactions();
    public Transaction updateTransaction(Transaction change);
    public Transaction deleteTransaction(int id) throws ResourceNotFoundException;

}
