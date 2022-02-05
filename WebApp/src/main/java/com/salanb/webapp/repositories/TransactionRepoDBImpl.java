package com.salanb.webapp.repositories;

import com.salanb.orm.utillities.JDBCConnection;
import com.salanb.orm.utillities.ResourceNotFoundException;
import com.salanb.webapp.models.Product;
import com.salanb.webapp.models.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepoDBImpl implements TransactionRepo {

    Connection conn = JDBCConnection.getConnection();

    @Override
    public Transaction addTransaction(Transaction t) {
        String sql = "INSERT INTO transaction VALUES";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setLong(1, t.getDate());
            ps.setInt(2, t.getC_id());

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                return buildTransaction(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public Transaction getTransaction(int t_id, int c_id) {
        String sql = "SELECT * FROM transaction WHERE t_id = ?";


        try {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, t_id);
            ps.setInt(2, c_id);

            ResultSet rs = ps.executeQuery();


            if (rs.next()) {
                return buildTransaction(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Transaction> getAllTransactions() {
        String sql = "SELECT * FROM transaction";


        try {
            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            List<Transaction> transactions = new ArrayList<>();
            while (rs.next()) {
                transactions.add(buildTransaction(rs));
            }
            return transactions;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Transaction updateTransaction(Transaction change) {
        String sql = "UPDATE transactions set id=?, date=?, c_id=? WHERE t_id = ? RETURNING *";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setLong(1, change.getDate());
            ps.setInt(2, change.getC_id());
            ps.setInt(3, change.getId());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return buildTransaction(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Transaction deleteTransaction(int id) throws ResourceNotFoundException {
        String sql = "DELETE FROM transaction WHERE t_id = ? RETURNING *";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, t_id);
            ps.setInt(2, c_id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return buildTransaction(rs);
            } else {
                throw new ResourceNotFoundException("Resource with id: " + t_id + c_id + "could not be found in the database.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Transaction buildTransaction(ResultSet rs) throws SQLException {
        Transaction t = new Transaction();
        t.setId(rs.getInt("t_id"));
        t.setDate(rs.getLong("date"));
        t.setId(rs.getInt("c_id"));

        return t;
    }
}
