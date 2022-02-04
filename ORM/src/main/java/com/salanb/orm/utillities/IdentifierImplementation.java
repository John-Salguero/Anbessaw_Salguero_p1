package com.salanb.orm.utillities;

import com.salanb.orm.logging.MyLogger;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;

/**
 * For this to work as an identifier in a hashmap both isEqual and hashCode have to be overridden
 */
public class IdentifierImplementation extends LinkedList<Object> implements Identifier {

    /**
     * Overrides the add method to prevent null primary keys
     * @param obj - The object representing a primary key
     * @return true if it was added properly
     */
    @Override
    public boolean add(Object obj){
        if(obj == null) {
            String msg = "A Primary Key is not permitted to be null";
            MyLogger.logger.error(msg);
            throw new InputMismatchException("Primary key can not be null.");
        }

        return super.add(obj);
    }

    /**
     * Returns a hash code depending on all the elements in the list
     * should be equal to all identical lists
     * @return the lower part of a SHA256 hash of the element contents
     */
    @Override
    public int hashCode(){
        Object[] Message = new Object[size()];
        int count = 0;

        for(Object elem : this) {
            if(elem == null) {
                String msg = "A Primary Key is not permitted to be null";
                MyLogger.logger.fatal(msg);
                throw new RuntimeException(msg);
            }
            Class<?> clazz = elem.getClass();
            if (clazz == Integer.class || clazz == Long.class || clazz == Short.class ||
                    clazz == BigDecimal.class || clazz == Character.class || clazz == Double.class ||
                    clazz == String.class || clazz == Boolean.class){
                Message[count++] = elem;
            }else {
                MyLogger.logger.fatal("an undefined class was used as a primary/composite key");
                throw new RuntimeException("an undefined class was used as a primary/composite key");
            }
        }

        return Objects.hash(Message);
    }

    /**
     * Compares all the values in the list to be equal
     * @param objRHS The other list to compare to
     * @return true if all the elements in the list are equal
     */
    @Override
    public boolean equals(Object objRHS) {
        if(objRHS.getClass() != this.getClass())
            return false;
        Identifier rhs = (Identifier) objRHS;
        if(size() != rhs.size())
            return false;
        else {
            Iterator<Object> thisIT = this.iterator();
            Iterator<Object> rhsIT = rhs.iterator();
            while(thisIT.hasNext())
            {
                Object lhsObject = thisIT.next();
                Object rhsObject = rhsIT.next();
                if(lhsObject == null || rhsObject == null) {
                    String msg = "A Primary Key is not permitted to be null";
                    MyLogger.logger.fatal(msg);
                    throw new RuntimeException(msg);
                }
                if(!lhsObject.equals(rhsObject))
                    return false;
            }
        }

        // All elements are identical
        return true;
    }
}
