package com.salanb.orm.session;

import com.salanb.orm.logging.MyLogger;
import com.salanb.orm.utillities.Identifier;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Transaction {
    private final Session parent;

    Transaction(Session parent) {
        this.parent = parent;
    }

//    public Identifier save(Object pojo) {
//
//        return pojo;
//    }
//
//    public Identifier update(Object pojo) {
//
//        return pojo;
//    }
//
//    public Identifier delete(Object pojo){
//
//        return pojo;
//    }
//
//    public Object get(Class clazz, Identifier id){
//
//        return pojo;
//    }

    /**
     * Given an object with a well formed ID, retreive it from
     * the cache, if it isn't cached, get it from the repo
     * @param pojo
     * @return
     */
    public Object get(Object pojo){
        SessionFactoryImplementation sf =
                (SessionFactoryImplementation) parent.getParent();
        Identifier id = sf.getId(pojo);
        String tableName = sf.getTableMaps().get(pojo.getClass());

        Object retVal = sf.getFromCachedData(tableName, id);
        if(retVal != null)
            return retVal;

        Class clazz = pojo.getClass();
        return getObjectFromRepo(tableName, clazz, id, sf.getPrimaryKeys().get(clazz), sf.getFieldMaps().get(clazz));
    }

    private Object getObjectFromRepo(String tableName, Class clazz, Identifier id,
                                     List<String> primaryKey, Map<String, String> fieldToNamesMap){
        StringBuilder query = new StringBuilder();
        query.append("SELECT ");

        // add the names
        Field[] fields= clazz.getDeclaredFields();
        for(int i = 0; i < fields.length; ){
            String name = fieldToNamesMap.get(fields[i++].getName());
            if(name != null)
                query.append(name);
            else if(i == fields.length)
                query.setLength(query.length() - 2);
            if(i < fields.length)
                query.append(", ");
        }

        // add the From Clause
        query.append(" FROM ").append(tableName);

        // add the Where Clause
        query.append(" WHERE ");
        Iterator<String> it = primaryKey.iterator();
        query.append(fieldToNamesMap.get(it.next())).append("=?");
        while(it.hasNext()){
            query.append(" AND ").
                    append(fieldToNamesMap.get(it.next())).append("=?");
        }

        // Prepare the Statement
        PreparedStatement ps = null;
        try {
            ps = parent.getConnection().getConnection().prepareStatement(query.toString());

            // Set the placeholders
            Iterator<Object> idIt = id.iterator();
            int i = 0;
            while(idIt.hasNext()){
                Object placeholder = idIt.next();
                Class placeholderClass = placeholder.getClass();
                if (placeholderClass == Integer.class) {
                    ps.setInt(++i, (Integer) placeholder);
                }else if(placeholderClass == Long.class){
                    ps.setLong(++i, (Long) placeholder);
                }else if(placeholderClass == Short.class){
                    ps.setShort(++i, (Short) placeholder);
                }else if(placeholderClass == BigDecimal.class){
                    ps.setBigDecimal(++i, (BigDecimal) placeholder);
                }else if(placeholderClass == Character.class){
                    ps.setNString(++i, ((Character) placeholder).toString());
                }else if(placeholderClass == String.class){
                    ps.setString(++i, (String)placeholder);
                }else if(placeholderClass == Boolean.class){
                    ps.setBoolean(++i, (Boolean)placeholder);
                }else {
                    MyLogger.logger.fatal("an undefiened class was used as a primary/composite key");
                    throw new RuntimeException("an undefiened class was used as a primary/composite key");
                }
            }

            // Execute the Statement
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                Object retVal = buildObject(clazz, rs, fieldToNamesMap);
                if(retVal != null)
                    ((SessionFactoryImplementation)parent.getParent()).
                            addToCachedData(retVal);
                return retVal;
            }

        }catch (SQLException e) {
            String msg = "Could not retrieve object from Database" + clazz + " " + id;
            MyLogger.logger.error(msg);
            return null;
        }

        // nothing was found return null
        return null;
    }

    /**
     * Given a class, a ResultSet of a query, and the map of class's fields to table names
     * construct an object and return it to caller
     * @param clazz - The class of the object to construct
     * @param rs - the Result set from the Query
     * @param fieldToNamesMap - A Map of a class's fields to it's table names
     * @return The new object constructed with the information
     */
    private Object buildObject(Class clazz, ResultSet rs, Map<String, String> fieldToNamesMap) {

        // construct the object and populate it with info
        Object retVal = null;
        try {
            retVal = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            String msg ="Mapped class was not a public POJO";
            MyLogger.logger.error(msg);
            return null;
        }

        try {
            for (Field field : clazz.getDeclaredFields()) {
                String type = field.getGenericType().getTypeName();
                String name = fieldToNamesMap.get(field.getName());
                switch (type){
                    case "int":
                    case "java.lang.Integer":{
                        field.setAccessible(true);
                        Integer val = rs.getInt(name);
                        if(val != null)
                            field.set(retVal, val);
                        break;}
                    case "long":
                    case "java.lang.Long":{
                        field.setAccessible(true);
                        Long val = rs.getLong(name);
                        if(val != null)
                            field.set(retVal, val);
                        break;}
                    case "short":
                    case "java.lang.Short":{
                        field.setAccessible(true);
                        Short val = rs.getShort(name);
                        if(val != null)
                            field.set(retVal, val);
                        break;}
                    case "java.math.BigDecimal":{
                        field.setAccessible(true);
                        BigDecimal val = rs.getBigDecimal(name);
                        if(val != null)
                            field.set(retVal, val);
                        break;}
                    case "char":
                    case "java.lang.Character":{
                        field.setAccessible(true);
                        String val = rs.getNString(name);
                        if(val != null)
                            field.set(retVal, val.charAt(0));
                        break;}
                    case "java.lang.String":{
                        field.setAccessible(true);
                        String val = rs.getString(name);
                        if(val != null)
                            field.set(retVal, val);
                        break;}
                    case "boolean":
                    case "java.lang.Boolean":{
                        field.setAccessible(true);
                        Boolean val = rs.getBoolean(name);
                        if(val != null)
                            field.set(retVal, val);
                        break;}

                }

            }
        } catch (IllegalAccessException e) {
            String msg = "A field could not be accessed by reflection";
            MyLogger.logger.fatal(msg);
            throw new RuntimeException(msg, e);
        }catch (SQLException e) {
            String msg = "Could not extract info from the Return Set Properly, Make sure you mapped out the POJO fully";
            MyLogger.logger.fatal(msg);
            throw new RuntimeException(msg, e);
        }

        return retVal;
    }

    void close(){
        // TODO Signal to the session it is time to close
    }

}
