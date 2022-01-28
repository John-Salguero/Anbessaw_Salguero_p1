package com.salanb.orm.session;

import com.salanb.orm.configuration.Configuration;

public interface SessionFactory {

    public SessionImplementation getSession();
}
