package com.salanb.webapp.servlets;

import com.salanb.webapp.controllers.ToyStoreController;
import com.salanb.webapp.repositories.ToyStoreRepo;
import com.salanb.webapp.repositories.ToyStoreRepoImplementation;
import com.salanb.webapp.services.ToyStoreService;
import com.salanb.webapp.services.ToyStoreServiceImplementation;
import com.salanb.webapp.utilities.ProcessRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RequestHelper {

    private static RequestHelper instance;
    public static RequestHelper getInstance(){
        if(instance == null)
            instance = new RequestHelper();
        return instance;
    }
    private ToyStoreRepo tsR = new ToyStoreRepoImplementation();
    private ToyStoreService tsS = new ToyStoreServiceImplementation(tsR);
    private ToyStoreController tsC = new ToyStoreController(tsS);
    private Map<String, ProcessRequest> getProcessMap;
    private Map<String, ProcessRequest> postProcessMap;
    private Map<String, ProcessRequest> putProcessMap;
    private Map<String, ProcessRequest> deleteProcessMap;

    private RequestHelper() {
        getProcessMap = new HashMap<>();
        postProcessMap = new HashMap<>();
        putProcessMap = new HashMap<>();
        deleteProcessMap = new HashMap<>();

        // Map the product function
        getProcessMap.put("toys", tsC::getToys);
        getProcessMap.put("customers", tsC::getCustomers);
        getProcessMap.put("transactions", tsC::getTransactions);
        getProcessMap.put("cart", tsC::getCarts);
        getProcessMap.put("productransaction", tsC::getProductTransaction);
        getProcessMap.put("showmycart", tsC::showMyCart);

        postProcessMap.put("addtoy", tsC::addToy);
        postProcessMap.put("login", tsC::login);
        postProcessMap.put("singup", tsC::signUp);
        postProcessMap.put("checkout", tsC::checkout);
        postProcessMap.put("addtocart", tsC::addToCart);

        putProcessMap.put("toys", tsC::updateToys);
        putProcessMap.put("customers", tsC::updateCustomers);
        putProcessMap.put("transactions", tsC::updateTransactions);
        putProcessMap.put("cart", tsC::updateCarts);
        putProcessMap.put("productransaction", tsC::updateProductTransaction);

        deleteProcessMap.put("toys", tsC::deleteToys);
        deleteProcessMap.put("customers", tsC::deleteCustomers);
        deleteProcessMap.put("transactions", tsC::deleteTransactions);
        deleteProcessMap.put("cart", tsC::clearCart);
        deleteProcessMap.put("productransaction", tsC::deleteProductTransaction);
        deleteProcessMap.put("removefromcart", tsC::removeFromCart);

    }

    public void getProcess(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uri = request.getRequestURI();
        String[] splitURI = uri.split("/");
        if(splitURI.length < 3)
            tsC.getWelcome(request, response);
        else{
            ProcessRequest proc = getProcessMap.get(splitURI[2]);
            if(proc == null)
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            else
                proc.processRequest(request, response);
        }

    }

    public void postProcess(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uri = request.getRequestURI();
        String[] splitURI = uri.split("/");
        if(splitURI.length < 3)
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        else{
            ProcessRequest proc = postProcessMap.get(splitURI[2]);
            if(proc == null)
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            else
                proc.processRequest(request, response);
        }
    }

    public void putProcess(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uri = request.getRequestURI();
        String[] splitURI = uri.split("/");
        if(splitURI.length < 3)
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        else{
            ProcessRequest proc = putProcessMap.get(splitURI[2]);
            if(proc == null)
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            else
                proc.processRequest(request, response);
        }
    }

    public void deleteProcess(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uri = request.getRequestURI();
        String[] splitURI = uri.split("/");
        if(splitURI.length < 3)
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        else{
            ProcessRequest proc = deleteProcessMap.get(splitURI[2]);
            if(proc == null)
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            else
                proc.processRequest(request, response);
        }
    }
}
