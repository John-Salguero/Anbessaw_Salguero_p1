package com.salanb.orm.session;

import com.salanb.orm.utillities.Identifier;
import com.salanb.orm.utillities.JDBCConnection;

/**
 * The interface for the Sessions which allow for transactions
 */
public interface Session {

    void close();

    SessionFactory getParent();
    Transaction getTransaction();
    void setDirtyFlag(String tableName, Identifier key);
    JDBCConnection getConnection();
}
