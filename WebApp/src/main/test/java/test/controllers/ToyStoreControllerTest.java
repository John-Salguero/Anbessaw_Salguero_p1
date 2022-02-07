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

import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;

import static org.junit.Assert.*;

@RunWith(Junit4)
public class ToyStoreControllerTest {

    static ToyStoreService tsS;
    static ToyStoreController tsC;
    static ToyStoreRepo tsR;

    @Mock
    HttpServletResponse response;
    @Mock
    HttpServletRequest request;
    @Mock
    HttpSession session;

    @BeforeClass
    public static void initTests(){
        tsR = new ToyStoreRepoImplementation();
        tsS = new ToyStoreServiceImplementation(tsR);
        tsC = new ToyStoreController(tsS);
    }

    @Test
    public void getToys() throws ParserConfigurationException, IOException {

        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(request.getRequestURI()).thenReturn("localhost/ServletName/toys");
        Mockito.when(session.getAttribute("session")).thenReturn(App.getInstance().getNewSession());

        tsC.getToys(request, response);

        Mockito.verify(request, Mockito.times(1)).getSession();
        Mockito.verify(request, Mockito.times(1)).getRequestURI();
        Mockito.verify(session, Mockito.times(1)).getAttribute("session");
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