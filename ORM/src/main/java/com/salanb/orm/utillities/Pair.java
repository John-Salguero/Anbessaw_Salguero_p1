package com.salanb.orm.utillities;

import java.util.Objects;

/**
 * Custom implementatin of Pair because apparently Maven doesn't work without this
 * @param <T>
 * @param <U>
 */
public class Pair<T, U> {
    private final T object1;
    private final U object2;

    /**
     * Override of the equals method, compares all the fields
     * @param o - The object to compare against
     * @return - Returns true if the objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(object1, pair.object1) && Objects.equals(object2, pair.object2);
    }

    /**
     * Returns the Hash of the two objects
     * @return an int of the two objects
     */
    @Override
    public int hashCode() {
        return Objects.hash(object1, object2);
    }

    /**
     * Returns a string holding the values of the two objects
     * @return A String holding the value
     */
    @Override
    public String toString() {
        return "Pair{" +
                "object1=" + object1 +
                ", object2=" + object2 +
                '}';
    }

    /**
     * Gets the reference value of the first object
     * @return The first object
     */
    public T getKey() {
        return object1;
    }

    /**
     * Gets the reference value of the second object
     * @return The second object
     */
    public U getValue(){
        return object2;
    }

    /**
     * The constructor which makes a pair out of two objects
     * @param object1 The first object
     * @param object2 The second object
     */
    public Pair(T object1, U object2){
        this.object1 = object1;
        this.object2 = object2;
    }
}
