package com.salanb.webapp.repositories;

import com.salanb.orm.utillities.JDBCConnection;
import com.salanb.webapp.models.Cart;
import com.salanb.webapp.models.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CartRepoDBImpl implements CartRepo{

    Connection conn = JDBCConnection.getConnection();
    @Override
    public Cart addCart(Cart ct) {
        String sql = "INSERT INTO cart VALUES";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, ct.getAmount());
            ps.setInt(2, ct.getP_id());

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                return buildCart(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public Cart getCart(int c_id, int p_id) {
        String sql = "SELECT * FROM cart WHERE c_id = ? AND p_id = ?";


        try {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, c_id);
            ps.setInt(2, p_id);

            ResultSet rs = ps.executeQuery();


            if (rs.next()) {
                return buildCart(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Cart> getAllCarts() {
        String sql = "SELECT * FROM transaction";


        try {
            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            List<Cart> carts = new ArrayList<>();
            while (rs.next()) {
                carts.add(buildCart(rs));
            }
            return carts;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Cart updateCart(Cart change) {
        String sql = "UPDATE transactions set id=?, date=?, c_id=? WHERE t_id = ? RETURNING *";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, change.getAmount());
            ps.setInt(2, change.getC_id());
            ps.setInt(3, change.getP_id());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return buildCart(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Cart buildCart(ResultSet rs) throws SQLException {
        Cart ct = new Cart();
        ct.setAmount(rs.getInt("amount"));
        ct.setC_id(rs.getInt("c_id"));
        ct.setP_id(rs.getInt("p_id"));
        return ct;
    }
}
