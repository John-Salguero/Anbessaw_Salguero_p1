package com.salanb.webapp.utilities;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface ProcessRequest {
    void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
