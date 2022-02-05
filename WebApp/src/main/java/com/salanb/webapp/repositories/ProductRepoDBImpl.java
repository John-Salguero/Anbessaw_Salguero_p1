package com.salanb.webapp.repositories;

import com.salanb.orm.utillities.JDBCConnection;
import com.salanb.orm.utillities.ResourceNotFoundException;
import com.salanb.webapp.models.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductRepoDBImpl implements ProductRepo {

    Connection conn = JDBCConnection.getConnection();

    @Override
    public Product addProduct(Product p) {
        String sql = "INSERT INTO product VALUES";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, p.getName());
            ps.setBigDecimal(2, p.getPrice());
            ps.setBoolean(3, p.getAvailability());

            ResultSet rs = ps.executeQuery();
            
            if(rs.next()) {
                return buildProduct(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }



    @Override
    public Product getProduct(int id) {

        String sql = "SELECT * FROM product WHERE p_id = ?";


        try {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();


            if (rs.next()) {
                return buildProduct(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Product> getAllProducts() {
        String sql = "SELECT * FROM product";


        try {
            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            List<Product> products = new ArrayList<>();
            while (rs.next()) {
                products.add(buildProduct(rs));
            }
            return products;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Product updateProduct(Product change) {
        String sql = "UPDATE products set name=?, price=?, available=? WHERE p_id = ? RETURNING *";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, change.getName());
            ps.setBigDecimal(2, change.getPrice());
            ps.setBoolean(3, change.getAvailability());
            ps.setInt(4, change.getId());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return buildProduct(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Product deleteProduct(int id) throws ResourceNotFoundException {

        String sql = "DELETE FROM products WHERE p_id = ? RETURNING *";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return buildProduct(rs);
            } else {
                throw new ResourceNotFoundException("Resource with id: " + id + "could not be found in the database.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    private Product buildProduct(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setId(rs.getInt("p_id"));
        p.setName(rs.getString("name"));
        p.setPrice(rs.getBigDecimal("price"));
        p.setAvailability(rs.getBoolean("available"));
        return p;

    }


}
