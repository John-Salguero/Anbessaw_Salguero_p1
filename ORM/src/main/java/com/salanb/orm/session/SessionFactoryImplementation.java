package com.salanb.orm.session;

import com.salanb.orm.configuration.Configuration;
import com.salanb.orm.utillities.Identifier;

import java.util.List;
import java.util.Map;

public class SessionFactoryImplementation implements SessionFactory {

    Map<Class, Map<String, String>> filedMaps;
    Map<Class, String> tableMaps;
    Map<String, Map<String, String>> typeMaps;
    Map<Class, List<String>> primaryKeys;
    Map<String, Identifier> dirtyFlags;



    public SessionFactoryImplementation(Configuration config) {

        /// TODO: Strip out the white space and initialize the Session Factory

    }

    @Override
    public SessionImplementation getSession() {
        return new SessionImplementation();
    }
}
