package com.salanb.orm.session;

import com.salanb.orm.logging.MyLogger;
import com.salanb.orm.utillities.Identifier;

public class Transaction {
    private final Session parent;

    Transaction(Session parent) {
        this.parent = parent;
    }

    /**
     * Given an object, updates the object in the cache
     * if the object already exist return null
     * as this is the wrong request to update a new entry
     * @param pojo - the object to be updated
     * @return - The identifier of that object
     */
    public Identifier save(Object pojo) {

        if(!parent.isValid())
            throw new IllegalStateException("This session has closed.");


        Object oldPOJO = get(pojo);
        if(oldPOJO != null)
            return null;

        // add the saved object to the cache and set it aside to be saved
        SessionFactoryImplementation sf = (SessionFactoryImplementation) parent.getParent();
        Object newPOJO = sf.addToCachedData(pojo);
        sf.addCachedToSave(pojo);
        if(sf.hasCachedToDelete(pojo))
            sf.removeCacheToDelete(pojo);

        Identifier id = sf.getId(newPOJO);

        // add to the dirty flags that we saved some info
        parent.setDirtyFlag(id);

        return id;
    }

    /**
     * Given an object, updates the object in the cache
     * if the object doesn't already exist return null
     * as this is the wrong request to save a new entry
     * @param pojo - the object to be updated
     * @return - The identifier of that object
     */
    public Identifier update(Object pojo) {

        if(!parent.isValid())
            throw new IllegalStateException("This session has closed.");

        Object oldPOJO = get(pojo);
        if(oldPOJO == null)
            return null;

        SessionFactoryImplementation sf = (SessionFactoryImplementation) parent.getParent();
        Object newPOJO = sf.addToCachedData(pojo);

        Identifier id = sf.getId(newPOJO);

        // add to the dirty flags that we updated some info
        parent.setDirtyFlag(id);

        return id;
    }

    /**
     * Given an object, mark it for deletion in the cached objects
     * @param pojo - The object to be Deleted
     * @return -
     */
    public Identifier delete(Object pojo){

        if(!parent.isValid())
            throw new IllegalStateException("This session has closed.");

        SessionFactoryImplementation sf = (SessionFactoryImplementation) parent.getParent();

        Identifier id = sf.getId(pojo);
        String tableName = sf.getTableMaps().get(pojo.getClass());
        if(sf.hasCachedToDelete(pojo))
            return id;
        sf.addCachedToDelete(tableName, id);
        sf.removeFromCachedData(tableName, id);
        if(sf.hasCacheToSave(pojo))
            sf.removeCacheToSave(tableName, id);

        // add to the dirty flags that we deleted some info
        parent.setDirtyFlag(id);

        return id;
    }

    /**
     * Given a Class and an Identifier (Primary/Composite key) get the object
     * @param clazz - The class of the object
     * @param id - the primary/composite key of the object
     * @return The object populated with information from the database
     */
    public Object get(Class<?> clazz, Identifier id){

        if(!parent.isValid())
            throw new IllegalStateException("This session has closed.");

        return ((SessionImplementation)parent).getObjectFromRepo(clazz, id);
    }

    /**
     * Given an object with a well formed ID, retreive it from
     * the cache, if it isn't cached, get it from the repo
     * @param pojo The object to be populated with data from the repo
     * @return - the object populated with data from the repo
     */
    public Object get(Object pojo){

        if(!parent.isValid())
            throw new IllegalStateException("This session has closed.");

        if(pojo == null) {
            MyLogger.logger.error("Object is null");
            return null;
        }

        SessionFactory sf =  parent.getParent();
        Identifier id = sf.getId(pojo);
        Class<?> clazz = pojo.getClass();

        return ((SessionImplementation)parent).getObjectFromRepo(clazz, id);
    }


    public void close(){
        parent.close();
    }

}
