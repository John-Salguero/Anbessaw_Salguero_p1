package com.salanb.orm.session;

import com.salanb.orm.utillities.Identifier;

import java.util.List;
import java.util.Map;

public interface SessionFactory {

    SessionImplementation getSession();

    void close();
    Map<Class<?>, Map<String, String>> getFieldMaps();
    Map<Class<?>, String> getTableMaps();
    Map<String, Map<String, String>> getTableTypeMaps();
    Map<Class<?>, List<String>> getPrimaryKeys();
    Map<Class<?>, List<SessionFactory.GeneratorType>> getGenerationType();
    Identifier getId(Object pojo);

    /**
     * The way new ids are generated
     */
    enum GeneratorType {
        DEFINED,
        NATURAL,
        FRAMEWORK
    }
}
