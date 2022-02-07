package com.salanb.webapp.servlets;


import com.salanb.orm.logging.MyLogger;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class MainServlet extends HttpServlet {

    public MainServlet() {
        super();
    }
    RequestHelper rq = RequestHelper.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

        try {
            rq.getProcess(request, response);;
        } catch (IOException e) {
            StringBuilder msg = new StringBuilder();
            msg.append("Error, responding to request.").append(e.getMessage());
            StackTraceElement[] stack = e.getStackTrace();
            for(int i = 0; i < stack.length; ++i)
                msg.append(stack[i]).append("\n");
            MyLogger.logger.error(msg);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            rq.postProcess(request, response);
        } catch (IOException e) {
            StringBuilder msg = new StringBuilder();
            msg.append("Error, responding to request.").append(e.getMessage());
            StackTraceElement[] stack = e.getStackTrace();
            for(int i = 0; i < stack.length; ++i)
                msg.append(stack[i]).append("\n");
            MyLogger.logger.error(msg);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {
        try {
            rq.putProcess(request, response);
        } catch (IOException e) {
            StringBuilder msg = new StringBuilder();
            msg.append("Error, responding to request.").append(e.getMessage());
            StackTraceElement[] stack = e.getStackTrace();
            for(int i = 0; i < stack.length; ++i)
                msg.append(stack[i]).append("\n");
            MyLogger.logger.error(msg);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        try {
            rq.deleteProcess(request, response);
        } catch (IOException e) {
            StringBuilder msg = new StringBuilder();
            msg.append("Error, responding to request.").append(e.getMessage());
            StackTraceElement[] stack = e.getStackTrace();
            for(int i = 0; i < stack.length; ++i)
                msg.append(stack[i]).append("\n");
            MyLogger.logger.error(msg);
        }
    }


}
