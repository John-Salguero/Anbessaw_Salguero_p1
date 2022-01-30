package com.salanb.orm.utillities;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashGenerator {

    private static final HashGenerator instance = new HashGenerator();
    private HashGenerator() {}
    public static HashGenerator getInstance() { return instance;}
    private static final MessageDigest SHA256Alg;
    private static final MessageDigest MD5;

    private static final char[] HEX_ARRAY = { // Used for transforming the Hash byte data into human-readable form
            '0', '1', '2', '3',
            '4', '5', '6', '7',
            '8', '9', 'a', 'b',
            'c', 'd', 'e', 'f'};

    static
    {
        try {
            SHA256Alg = MessageDigest.getInstance("SHA-256");
            MD5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error: Algorithm could not be used!", e);
        }

    }

    /**
     * The method that securely Hashes information and returns a hex number in text form
     * @param msg - The string that will be hashed
     * @return A String holding the Hash of the message passed in
     */
    public String getMessageDigestString(String msg){

        // Hash the message to securely save and compare
        byte[] hash = SHA256Alg.digest(msg.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hash);
    }

    /**
     * The method that securely Hashes information and returns an int
     * @param msg - The string that will be hashed
     * @return An int holding the Hash of the message passed in
     */
    public int getMessageDigestInt(String msg){

        // Hash the message to securely save and compare
        byte[] hash = SHA256Alg.digest(msg.getBytes(StandardCharsets.UTF_8));
        return bytesToInt(hash);
    }

    /**
     * The method that securely Hashes information and returns a long
     * @param msg - The string that will be hashed
     * @return A Long holding the Hash of the message passed in
     */
    public long getMessageDigestLong(String msg){

        // Hash the message to securely save and compare
        byte[] hash = SHA256Alg.digest(msg.getBytes(StandardCharsets.UTF_8));
        return bytesToLong(hash);
    }

    /**
     * The method that securely Hashes information and returns a short
     * @param msg - The string that will be hashed
     * @return A short holding the Hash of the message passed in
     */
    public short getMessageDigestShort(String msg){

        // Hash the message to securely save and compare
        byte[] hash = SHA256Alg.digest(msg.getBytes(StandardCharsets.UTF_8));
        return bytesToShort(hash);
    }

    /**
     * Transforms the given byte array into a number in int form
     * @param bytes the byte array to transform
     * @return a int representation of the array
     */
    private int bytesToInt(byte[] bytes) {
        int retVal = 0;
        // convert each byte into a hexadecimal value and convert that into ascii format
        for (int j = 0; j < 4; j++) {
            int curByte = bytes[j];
            // bit-shift the byte by 8 to the proper location
            retVal |= (curByte << (8 * j)) & (0xFF  << (8 * j));
        }

        return retVal;
    }

    /**
     * Transforms the given byte array into a number in long form
     * @param bytes the byte array to transform
     * @return a Long representation of the array
     */
    private long bytesToLong(byte[] bytes) {
        long retVal = 0;
        // convert each byte into a hexadecimal value and convert that into ascii format
        for (int j = 0; j < 8; j++) {
            int curByte = bytes[j];
            // bit-shift the byte by 8 to the proper location
            retVal |= (curByte << (8 * j)) & (0xFF  << (8 * j));
        }

        return retVal;
    }

    /**
     * Transforms the given byte array into a number in short form
     * @param bytes the byte array to transform
     * @return a Short representation of the array
     */
    private short bytesToShort(byte[] bytes) {
        short retVal = 0;
        // convert each byte into a hexadecimal value and convert that into ascii format
        for (int j = 0; j < 2; j++) {
            int curByte = bytes[j];
            // bit-shift the byte by 8 to the proper location
            retVal |= (curByte << (8 * j)) & (0xFF  << (8 * j));
        }

        return retVal;
    }

    /**
     * Transforms the given byte array into a hexadecimal number in string format
     * @param bytes the byte array to transform
     * @return a hexadecimal representation of the array in string format
     */
    private String bytesToHex(byte[] bytes) {
        // array of characters in ascii form representing the data in byte array
        char[] hexChars = new char[bytes.length * 2];

        // convert each byte into a hexadecimal value and convert that into ascii format
        for (int j = 0; j < bytes.length; j++) {
            int curByte = bytes[j];
            // bit-shift the byte by 4 to retrieve the higher WORD and use that as an index into the array to convert
            hexChars[j * 2] = HEX_ARRAY[(curByte >>> 4) & 0x0F];
            // AND the lower WORD by 16 to isolate it and use that as an index into the array to convert
            hexChars[j * 2 + 1] = HEX_ARRAY[curByte & 0x0F];
        }
        return new String(hexChars);
    }
}
