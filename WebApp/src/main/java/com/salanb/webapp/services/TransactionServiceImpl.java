package com.salanb.webapp.services;

import com.salanb.orm.utillities.ResourceNotFoundException;
import com.salanb.webapp.models.Product;
import com.salanb.webapp.models.Transaction;
import com.salanb.webapp.repositories.TransactionRepo;

import java.util.List;

public class TransactionServiceImpl implements TransactionService{

    private TransactionRepo tr;

    public TransactionServiceImpl(TransactionRepo tr) {
        this.tr = tr;
    }

    @Override
    public Transaction addTransaction(Transaction t) {
        return tr.addTransaction(t) ;
    }

    @Override
    public Transaction getTransaction(int t_id, int c_id) {
        return tr.getTransaction(t_id, c_id);
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return tr.getAllTransactions();
    }

    @Override
    public Transaction updateTransaction(Transaction change) {
        return tr.updateTransaction(change);
    }

    @Override
    public Transaction deleteTransaction(int id) throws ResourceNotFoundException {
        return tr.deleteTransaction(id);
    }
}
