package com.salanb.webapp.servlets;

import com.google.gson.Gson;
import com.salanb.orm.App;
import com.salanb.orm.session.Transaction;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

public class RequestHelper {

    private static Gson gson = new Gson();

    public static void getProcess(HttpServletRequest request, HttpServletResponse response) {

        Transaction transaction = null;
        try {
            transaction = App.getInstance().getNewSession().getTransaction();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        List<Movie> movieList = transaction.getTable(Movie.class);

        try {
            response.getWriter().append(gson.toJson(movieList));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void postProcess(HttpServletRequest request, HttpServletResponse response) {
    }

    public static void putProcess(HttpServletRequest request, HttpServletResponse response) {
    }

    public static void deleteProcess(HttpServletRequest request, HttpServletResponse response) {
    }
}
