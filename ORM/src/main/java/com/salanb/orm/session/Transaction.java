package com.salanb.orm.session;

import com.salanb.orm.logging.MyLogger;
import com.salanb.orm.utillities.HashGenerator;
import com.salanb.orm.utillities.Identifier;
import com.salanb.orm.utillities.ResourceNotFoundException;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
     * @throws IllegalStateException Throws when trying to save an object already in the database
     */
    public <T> T save(T pojo) throws IllegalStateException {

        if(parent.isInvalid())
            throw new IllegalStateException("This session has closed.");

        // Throws if it cannot be saved
        objectCanBeSaved(pojo);
        // Generate the Ids that have been configured to be generated
        populateGeneratedIds(pojo);

        // Save the object to the database since the Database could decide how to assign values
        return (T)((SessionImplementation)parent).save(pojo);
    }


    /**
     * Given an object, throws an exception if the object is already in the database,
     * and has defined Ids
     * it has id's that need to be generated
     * @param pojo The object to test
     */
    private boolean objectCanBeSaved(Object pojo) throws IllegalStateException {

        SessionFactoryImplementation sf = (SessionFactoryImplementation) parent.getParent();

        Object oldPOJO = get(pojo);
        if(oldPOJO != null) { // if the data already exists, see what the id generation is set to
            List<SessionFactory.GeneratorType> genTypes = sf.getGenerationType().get(pojo.getClass());
            boolean isPureDefined = true;
            for (SessionFactory.GeneratorType type : genTypes) {
                if(type == SessionFactory.GeneratorType.DEFINED)
                    continue;
                isPureDefined = false;
                break;
            }

            if(isPureDefined) { // the item is purely defined, and is already in the database - cannot save it
                String msg = "Cannot save item that already exists in the database";
                MyLogger.logger.error(msg);
                throw new IllegalStateException(msg);
            }
        }
        return true;
    }

    /**
     * Given an object, updates the object in the cache
     * if the object doesn't already exist return null
     * as this is the wrong request to save a new entry
     * @param pojo - the object to be updated
     * @return - The identifier of that object
     */
    public <T> T update(T pojo) throws ResourceNotFoundException {

        if(parent.isInvalid())
            throw new IllegalStateException("This session has closed.");

        T oldPOJO = get(pojo);
        if(oldPOJO == null)
            throw new ResourceNotFoundException("The object can not be found in the Database");

        SessionFactoryImplementation sf = (SessionFactoryImplementation) parent.getParent();
        T newPOJO = (T)sf.addToCachedData(pojo);

        Identifier id = sf.getId(newPOJO);

        // add to the dirty flags that we updated some info
        parent.setDirtyFlag(id);

        return newPOJO;
    }

    /**
     * Given an object, mark it for deletion in the cached objects
     * @param pojo - The object to be Deleted
     * @return -
     */
    public <T> T delete(T pojo){

        if(parent.isInvalid())
            throw new IllegalStateException("This session has closed.");

        SessionFactoryImplementation sf = (SessionFactoryImplementation) parent.getParent();
        SessionImplementation session = (SessionImplementation)parent;

        Identifier id = sf.getId(pojo);
        String tableName = sf.getTableMaps().get(pojo.getClass());


        // add to the dirty flags that we deleted some info
        parent.removeDirtyFlag(id);
        T deletedObject = (T)session.delete(pojo.getClass(), id);
        sf.removeFromCachedData(tableName, id);

        return deletedObject;
    }

    /**
     * Given a Class and an Identifier (Primary/Composite key) get the object
     * @param clazz - The class of the object
     * @param id - the primary/composite key of the object
     * @return The object populated with information from the database
     */
    public <T> T get(Class<T> clazz, Identifier id){

        if(parent.isInvalid())
            throw new IllegalStateException("This session has closed.");

        return (T)((SessionImplementation)parent).getObjectFromRepo(clazz, id);
    }

    /**
     * Given a Class get all of those objects from the Database
     * @param clazz - The class of the object
     * @return The object populated with information from the database
     */
    public <T> List<T> getTable(Class<T> clazz){

        if(parent.isInvalid())
            throw new IllegalStateException("This session has closed.");

        return parent.getTableFromRepo(clazz);
    }

    /**
     * Given an Object get all of those objects from the Database
     * @param pojo - The object related to the table
     * @return The object populated with information from the database
     */
    public <T> List<T> getTable(T pojo){

        if(parent.isInvalid())
            throw new IllegalStateException("This session has closed.");

        return parent.getTableFromRepo((Class<T>)pojo.getClass());
    }

    /**
     * Given an object with a well-formed ID, retrieve it from
     * the cache, if it isn't cached, get it from the repo
     * @param pojo The object to be populated with data from the repo
     * @return - the object populated with data from the repo
     */
    public <T> T get(T pojo){

        if(parent.isInvalid())
            throw new IllegalStateException("This session has closed.");

        if(pojo == null) {
            MyLogger.logger.error("Object is null");
            return null;
        }

        SessionFactory sf =  parent.getParent();
        Identifier id = sf.getId(pojo);
        Class<?> clazz = pojo.getClass();

        return (T)((SessionImplementation)parent).getObjectFromRepo(clazz, id);
    }

    /**
     * Given an Object, generates the ids if configured to do so
     * @param pojo The object to generate its ids
     */
    private void populateGeneratedIds(Object pojo) {

        // The SessionFactory this Transaction is configured from
        SessionFactoryImplementation sf = (SessionFactoryImplementation) parent.getParent();

        Class<?> clazz = pojo.getClass();
        // A map of the object's field names to the database's table column names
        Map<String, String> fieldToNamesMap = sf.getFieldMaps().get(clazz);
        // The name of the table that holds the object
        String tableName = sf.getTableMaps().get(clazz);
        // The Map of column names to their types
        Map<String, String> columnToTypeMap = sf.getTableTypeMaps().get(tableName);
        // The list of fields that make up the primary/composite key of the object
        List<String> primaryKey = sf.getPrimaryKeys().get(clazz);
        // Gets a list of the generator types for the ids
        List<SessionFactory.GeneratorType> generatorTypeList = sf.getGenerationType().get(clazz);

        // iterator for the primary keys to determine new ids for the data
        Iterator<String> primIt = primaryKey.iterator();
        // iterator for the generator types for the different IDs
        Iterator<SessionFactory.GeneratorType> genIt = generatorTypeList.iterator();


            // iterate throw all the placeholders and put in the correct values
            // generate the id's based on the specified way
            // start iterating by getting the next ID we are looking for
            String nextId = fieldToNamesMap.get(primIt.next());
            for(Field field : clazz.getDeclaredFields()){
                String columnName = fieldToNamesMap.get(field.getName());
                if(columnName != null) { // if the field has been mapped to the table
                    // The value being placed into the placeholder
                    Object value;
                    // The type of the column name
                    String type = columnToTypeMap.get(columnName);

                    // if the field is an id generate the id
                    if(nextId.equals(columnName)){
                        switch (genIt.next()){ // provide a generated ID based on configured value
                            // generate the ids
                            case DEFINED: // just use the object's id
                                break;
                            case NATURAL: // let the Database determine the id
                            case FRAMEWORK: // We determine the id
                                value = generateId(type);
                                field.setAccessible(true);
                                try {
                                    field.set(pojo, value);
                                } catch (IllegalAccessException e) {
                                    throw new RuntimeException("Failed accessing a field; This shouldn't be possible");
                                }
                        }
                        if(primIt.hasNext()) { // if we have more id's to check for
                            nextId = primIt.next();
                        }
                    }
                }
            }
    }

    /**
     * The framework implementation of Generating an ID value based on
     * the type I just randomly take a Hash and use that
     * There might be some collisions, this is not recommended
     * @param type The type of object to generate
     * @return The new ID
     */
    private Object generateId(String type) {
        double randSeed = Math.random();
        switch (type) {
            case "integer":
                return HashGenerator.getInstance().getMessageDigestInt(Double.toString(randSeed));
            case "long":
                return HashGenerator.getInstance().getMessageDigestLong(Double.toString(randSeed));
            case "short":
                return HashGenerator.getInstance().getMessageDigestShort(Double.toString(randSeed));
            case "big_decimal":
                return new BigDecimal(randSeed);
            case "character":
                return HashGenerator.getInstance().getMessageDigestString(Double.toString(randSeed)).charAt(0);
            case "string":
                return HashGenerator.getInstance().getMessageDigestString(Double.toString(randSeed));
            case "boolean":
                return randSeed > .5;
            case "double":
                return randSeed;
            default:
                String msg = "Mapped field has malformed type attribute";
                MyLogger.logger.fatal(msg);
                throw new InputMismatchException(msg);
        }
    }

    /**
     * Closes the session the transaction belongs to
     */
    public void close(){
        parent.close();
    }

}
