package test.controllers;

import com.salanb.orm.App;
import com.salanb.webapp.controllers.ToyStoreController;
import com.salanb.webapp.repositories.ToyStoreRepo;
import com.salanb.webapp.repositories.ToyStoreRepoImplementation;
import com.salanb.webapp.services.ToyStoreService;
import com.salanb.webapp.services.ToyStoreServiceImplementation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ToyStoreControllerTest {

    @Mock
    HttpServletResponse response;
    @Mock
    HttpServletRequest request;
    @Mock
    HttpSession session;

    @BeforeClass
    public static void initTests(){


    }

    @Test
    public void getToys() throws ParserConfigurationException, IOException {


//        System.out.println("Testing fails");
//        System.out.println("Starting test");
//        ToyStoreRepo tsR = new ToyStoreRepoImplementation();
//        System.out.println(" have tsR");
//        ToyStoreService tsS = new ToyStoreServiceImplementation(tsR);
//        System.out.println("Have tsS");
//        ToyStoreController tsC = new ToyStoreController(tsS);
//        System.out.println("Have tsC");
//
//        Mockito.when(request.getSession()).thenReturn(session);
//        Mockito.when(request.getRequestURI()).thenReturn("localhost/ServletName/toys");
//        Mockito.when(session.getAttribute("session")).thenReturn(App.getInstance().getNewSession());
//
//        tsC.getToys(request, response);
//
//        Mockito.verify(request, Mockito.times(1)).getSession();
//        Mockito.verify(request, Mockito.times(1)).getRequestURI();
//        Mockito.verify(session, Mockito.times(1)).getAttribute("session");
    }

    @Test
    public void addToy() {
    }

    @Test
    public void login() {
    }

    @Test
    public void getWelcome() {
    }

    @Test
    public void signUp() {
    }

    @Test
    public void getProductTransaction() {
    }

    @Test
    public void getCustomers() {
    }

    @Test
    public void getTransactions() {
    }

    @Test
    public void getCarts() {
    }

    @Test
    public void addToCart() {
    }

    @Test
    public void checkout() {
    }

    @Test
    public void updateToys() {
    }

    @Test
    public void updateCustomers() {
    }

    @Test
    public void updateTransactions() {
    }

    @Test
    public void updateCarts() {
    }

    @Test
    public void updateProductTransaction() {
    }

    @Test
    public void deleteToys() {
    }

    @Test
    public void deleteCustomers() {
    }

    @Test
    public void deleteTransactions() {
    }

    @Test
    public void clearCart() {
    }

    @Test
    public void deleteProductTransaction() {
    }

    @Test
    public void removeFromCart() {
    }

    @Test
    public void showMyCart() {
    }
}