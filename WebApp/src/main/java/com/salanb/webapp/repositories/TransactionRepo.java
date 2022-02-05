package com.salanb.webapp.repositories;

import com.salanb.orm.utillities.ResourceNotFoundException;
import com.salanb.webapp.models.Transaction;

import java.util.List;

public interface TransactionRepo {
    public Transaction addTransaction(Transaction t);
    public Transaction getTransaction(int t_id, int c_id);
    public List<Transaction> getAllTransactions();
    public Transaction updateTransaction(Transaction change);
    public Transaction deleteTransaction(int id) throws ResourceNotFoundException;
}
