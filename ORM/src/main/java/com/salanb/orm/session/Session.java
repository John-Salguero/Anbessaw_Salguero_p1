package com.salanb.orm.session;

import com.salanb.orm.utillities.Identifier;
import com.salanb.orm.utillities.JDBCConnection;
import com.salanb.orm.utillities.Pair;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The interface for the Sessions which allow for transactions
 */
public interface Session {

    void close();
    void writeAllCache(
    Map<String, Map<Identifier, Object>> cachedData,
    Map<String, Set<Pair<Class<?>, Identifier>>> cacheToDelete);

    SessionFactory getParent();
    Transaction getTransaction();
    void setDirtyFlag(Identifier key);
    JDBCConnection getConnection();
    <T> List<T> getTableFromRepo(Class<T> clazz);
    boolean isInvalid();
}
