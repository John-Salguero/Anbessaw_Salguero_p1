package com.salanb.orm.utillities;

import com.salanb.orm.logging.MyLogger;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * For this to work as an identifier in a hashmap both isEqual and hashCode have to be overridden
 */
public class IdentifierImplementation extends LinkedList<Object> implements Identifier {

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
                if(!thisIT.next().equals(rhsIT.next()))
                    return false;
            }
        }

        // All elements are identical
        return true;
    }

    /**
     * Returns a hash code depending on all the elements in the list
     * should be equal to all identical lists
     * @return the lower part of a SHA256 hash of the element contents
     */
    @Override
    public int hashCode(){
        StringBuilder Message = new StringBuilder();

        for(Object elem : this) {
            Class clazz = elem.getClass();
            if (clazz == Integer.class) {
                Message.append((Integer)elem);
            }else if(clazz == Long.class){
                Message.append((Long)elem);
            }else if(clazz == Short.class){
                Message.append((Short)elem);
            }else if(clazz == BigDecimal.class){
                Message.append((BigDecimal)elem);
            }else if(clazz == Character.class){
                Message.append((Character)elem);
            }else if(clazz == String.class){
                Message.append((String)elem);
            }else if(clazz == Boolean.class){
                Message.append((Boolean)elem);
            }else {
                MyLogger.logger.fatal("an undefiened class was used as a primary/composite key");
                throw new RuntimeException("an undefiened class was used as a primary/composite key");
            }
        }

        return HashGenerator.getInstance().getMessageDigestInt(Message.toString());
    }
}
