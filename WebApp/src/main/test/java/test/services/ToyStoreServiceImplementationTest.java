package test.services;

import com.salanb.orm.App;
import com.salanb.orm.session.Session;
import com.salanb.webapp.models.*;
import com.salanb.webapp.repositories.ToyStoreRepo;
import com.salanb.webapp.repositories.ToyStoreRepoImplementation;
import com.salanb.webapp.services.ToyStoreService;
import com.salanb.webapp.services.ToyStoreServiceImplementation;
import org.junit.Test;

import javax.xml.parsers.ParserConfigurationException;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

public class ToyStoreServiceImplementationTest {

    ToyStoreRepo tsR = new ToyStoreRepoImplementation();
    ToyStoreService tsS = new ToyStoreServiceImplementation(tsR);

    @Test
    public void getAllToys() throws ParserConfigurationException {
        List<Product> productList = tsS.getAllToys(App.getInstance().getNewSession());
        assertNotNull(productList);
    }

    @Test
    public void getToyById() throws ParserConfigurationException {
        Session session = App.getInstance().getNewSession();
        Product toy = new Product(123, "Big Bird", new BigDecimal("2.50"), true, "");

        toy = tsS.addToy(session, toy);
        List<Product> productList = tsS.getAllToys(session);
        assertNotNull(productList);

        tsS.deleteToy(session, toy.getId());
    }

    @Test
    public void login() throws ParserConfigurationException {
        Session session = App.getInstance().getNewSession();
        Account user = new Account();
        user.setUsername("Test_User");
        user.setPassword("Test_User");

        Customer newUser = tsS.signUp(session, user);
        assertNotNull(newUser);
        assertNotNull(tsS.login(session, user));

        tsS.deleteCustomer(session, newUser.getId());
    }

    @Test
    public void getProductTransactions() {

    }

    @Test
    public void getCustomers() {

    }

    @Test
    public void getCustomerById() {
    }

    @Test
    public void getTransactions() {
    }

    @Test
    public void getTransactionById() {
    }

    @Test
    public void addToCart() {
    }

    @Test
    public void checkout() throws ParserConfigurationException {
        Session session = App.getInstance().getNewSession();
        Account user = new Account();
        user.setUsername("Test_User");
        user.setPassword("Test_User");
        Product toy = new Product(123, "Big Bird", new BigDecimal("2.50"), true, "");

        toy = tsS.addToy(session, toy);
        assertNotNull(toy);
        Customer newUser = tsS.signUp(session, user);
        assertNotNull(newUser);
        List<CartItem> cartItems = tsS.addToCart(session, newUser.getId(), toy.getId());
        assertNotNull(cartItems);
        TransactionDisplay td = tsS.checkout(newUser.getId(), session);
        assertNotNull(td);

        assertNotNull(tsS.deleteTransactionProduct(session, toy.getId(), td.getId()));
        assertNotNull(tsS.deleteTransaction(session, td.getId()));
        assertNotNull(tsS.deleteCustomer(session, newUser.getId()));
        assertNotNull(tsS.deleteToy(session, toy.getId()));
    }

    @Test
    public void updateToy() {
    }

    @Test
    public void updateCustomer() {
    }

    @Test
    public void updateTransaction() {
    }

    @Test
    public void updateCart() {
    }

    @Test
    public void updateTransactionProduct() {
    }

    @Test
    public void deleteToy() {
    }

    @Test
    public void deleteCustomer() {
    }

    @Test
    public void deleteTransaction() {
    }

    @Test
    public void clearCart() throws ParserConfigurationException {
        Session session = App.getInstance().getNewSession();
        Account user = new Account();
        user.setUsername("Test_User");
        user.setPassword("Test_User");
        Product toy = new Product(123, "Big Bird", new BigDecimal("2.50"), true, "");

        toy = tsS.addToy(session, toy);
        assertNotNull(toy);
        Customer newUser = tsS.signUp(session, user);
        assertNotNull(newUser);
        List<CartItem> cartItems = tsS.addToCart(session, newUser.getId(), toy.getId());
        assertNotNull(cartItems);
        assertTrue(tsS.clearCart(session, newUser.getId()));

        assertNotNull(tsS.deleteCustomer(session, newUser.getId()));
        assertNotNull(tsS.deleteToy(session, toy.getId()));
    }

    @Test
    public void deleteTransactionProduct() {
    }

    @Test
    public void removeFromCart() throws ParserConfigurationException {
        Session session = App.getInstance().getNewSession();
        Account user = new Account();
        user.setUsername("Test_User");
        user.setPassword("Test_User");
        Product toy1 = new Product(123, "Big Bird", new BigDecimal("2.50"), true, "");
        Product toy2 = new Product(123, "Tickle Me Elmo", new BigDecimal("2.50"), true, "");


        toy1 = tsS.addToy(session, toy1);
        assertNotNull(toy1);
        toy2 = tsS.addToy(session, toy2);
        assertNotNull(toy2);
        Customer newUser = tsS.signUp(session, user);
        assertNotNull(newUser);
        List<CartItem> cartItems = tsS.addToCart(session, newUser.getId(), toy1.getId());
        assertNotNull(cartItems);
        cartItems = tsS.addToCart(session, newUser.getId(), toy2.getId());
        assertNotNull(cartItems);
        cartItems = tsS.addToCart(session, newUser.getId(), toy1.getId());
        assertNotNull(cartItems);
        List<CartItem> remainingCart = tsS.removeFromCart(session, newUser.getId(), toy1.getId(), 1);
        assertNotNull(remainingCart);
        assertTrue(tsS.clearCart(session, newUser.getId()));

        assertNotNull(tsS.deleteCustomer(session, newUser.getId()));
        assertNotNull(tsS.deleteToy(session, toy1.getId()));
    }

    @Test
    public void getUserCart() throws ParserConfigurationException {
        Session session = App.getInstance().getNewSession();
        Account user = new Account();
        user.setUsername("Test_User");
        user.setPassword("Test_User");
        Product toy = new Product(123, "Big Bird", new BigDecimal("2.50"), true, "");

        toy = tsS.addToy(session, toy);
        assertNotNull(toy);
        Customer newUser = tsS.signUp(session, user);
        assertNotNull(newUser);
        List<CartItem> cartItems = tsS.addToCart(session, newUser.getId(), toy.getId());
        assertNotNull(cartItems);
        List<CartItem> remainingCart = tsS.getUserCart(session, newUser.getId());
        assertNotNull(remainingCart);
        assertTrue(tsS.clearCart(session, newUser.getId()));

        assertNotNull(tsS.deleteCustomer(session, newUser.getId()));
        assertNotNull(tsS.deleteToy(session, toy.getId()));
    }
}