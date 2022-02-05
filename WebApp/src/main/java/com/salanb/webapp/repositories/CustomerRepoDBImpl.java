package com.salanb.webapp.repositories;

import com.salanb.orm.utillities.JDBCConnection;
import com.salanb.orm.utillities.ResourceNotFoundException;
import com.salanb.webapp.models.Customer;
import com.salanb.webapp.models.Product;
import com.salanb.webapp.models.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepoDBImpl implements CustomerRepo{

    Connection conn = JDBCConnection.getConnection();

    @Override
    public Customer addCustomer(Customer c) {
        String sql = "INSERT INTO customer VALUES";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, c.getUserName());
            ps.setString(2, c.getPassWord());

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                return buildCustomer(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public Customer getCustomer(int id) {
        String sql = "SELECT * FROM customer WHERE p_id = ?";


        try {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();


            if (rs.next()) {
                return buildCustomer(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Customer> getAllCustomers() {
        String sql = "SELECT * FROM transaction";


        try {
            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            List<Customer> customers = new ArrayList<>();
            while (rs.next()) {
                customers.add(buildCustomer(rs));
            }
            return customers;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Customer updateCustomer(Customer change) {
        String sql = "UPDATE customer set id=?, username=?, password=? WHERE c_id = ? RETURNING *";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, change.getUserName());
            ps.setString(2, change.getPassWord());
            ps.setInt(3, change.getId());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return buildCustomer(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Customer deleteCustomer(int id) throws ResourceNotFoundException {
        String sql = "DELETE FROM transaction WHERE c_id = ? RETURNING *";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return buildCustomer(rs);
            } else {
                throw new ResourceNotFoundException("Resource with id: " + id + "could not be found in the database.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Customer buildCustomer(ResultSet rs) throws SQLException {
        Customer c = new Customer();
        c.setId(rs.getInt("c_id"));
        c.setUserName(rs.getString("username"));
        c.setPassWord(rs.getString("password"));
        return c;
    }
}
