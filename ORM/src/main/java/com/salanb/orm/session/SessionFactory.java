package com.salanb.orm.session;

import com.salanb.orm.configuration.Configuration;
import com.salanb.orm.utillities.Identifier;

import java.util.List;
import java.util.Map;

public interface SessionFactory {

    SessionImplementation getSession();

    void close();
    Map<Class, Map<String, String>> getFieldMaps();
    Map<Class, String> getTableMaps();
    Map<String, Map<String, String>> getTableTypeMaps();
    public Map<Class, List<String>> getPrimaryKeys();
    Identifier getId(Object pojo);
}
