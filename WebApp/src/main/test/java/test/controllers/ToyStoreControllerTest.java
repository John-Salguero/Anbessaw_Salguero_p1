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
import java.io.PrintWriter;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ToyStoreControllerTest {

    @Mock
    HttpServletResponse response;
    @Mock
    HttpServletRequest request;
    @Mock
    HttpSession session;
    @Mock
    PrintWriter writer;

    @BeforeClass
    public static void initTests(){


    }

    @Test
    public void getToys() throws ParserConfigurationException, IOException {
        ToyStoreRepo tsR = new ToyStoreRepoImplementation();
        ToyStoreService tsS = new ToyStoreServiceImplementation(tsR);
        ToyStoreController tsC = new ToyStoreController(tsS);

        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(request.getRequestURI()).thenReturn("localhost/ServletName/toys");
        Mockito.when(session.getAttribute("session")).thenReturn(App.getInstance().getNewSession());
        Mockito.when(response.getWriter()).thenReturn(writer);

        tsC.getToys(request, response);

        Mockito.verify(request, Mockito.times(1)).getSession();
        Mockito.verify(request, Mockito.times(1)).getRequestURI();
        Mockito.verify(session, Mockito.times(1)).getAttribute("session");
    }

}