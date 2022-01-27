package com.salanb.orm.session;

import com.salanb.orm.configuration.Configuration;

public class SessionFactory {

    public SessionFactory(Configuration config) {

        /// TODO: Strip out the white space and initialize the Session Factory

    }

    public Session getSession() {
        return new Session();
    }
}
