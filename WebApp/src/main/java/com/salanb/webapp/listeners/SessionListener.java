package com.salanb.webapp.listeners;

import com.salanb.orm.App;
import com.salanb.orm.logging.MyLogger;
import com.salanb.orm.session.Session;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

import javax.xml.parsers.ParserConfigurationException;

public class SessionListener implements HttpSessionListener {


    public void sessionCreated(final HttpSessionEvent event) {

        // open a connection to the Database
        Session newSession = null;
        try {
            newSession = App.getInstance().getNewSession();
        } catch (ParserConfigurationException e) {
            String msg = "Incorrect configuration used for the ORM";
            MyLogger.logger.fatal(msg);
            throw new RuntimeException(msg, e);
        }

        event.getSession().setAttribute("session", newSession);
        event.getSession().setAttribute("admin", Boolean.valueOf("false"));
        event.getSession().setAttribute("loggedIn", Boolean.valueOf("false"));
    }
    public void sessionDestroyed(final HttpSessionEvent event) {

        // Automatically close the session if the session is invalidated
        Session session = (Session) event.getSession().getAttribute("session");
        session.close();
    }
}