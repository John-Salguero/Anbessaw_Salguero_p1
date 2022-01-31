package com.salanb.orm.session;

import com.salanb.orm.utillities.Identifier;
import com.salanb.orm.utillities.JDBCConnection;
import javafx.util.Pair;

import java.util.Map;
import java.util.Set;

/**
 * The interface for the Sessions which allow for transactions
 */
public interface Session {

    void close();
    void writeAllCache(
    Map<String, Map<Identifier, Object>> cachedData,
    Map<String, Set<Pair<Class<?>, Identifier>>> cacheToDelete,
    Map<String, Set<Identifier>> cacheToSave);

    SessionFactory getParent();
    Transaction getTransaction();
    void setDirtyFlag(Identifier key);
    JDBCConnection getConnection();
    boolean isValid();
}
