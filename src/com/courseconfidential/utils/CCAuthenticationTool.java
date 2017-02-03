package com.courseconfidential.utils;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class CCAuthenticationTool {

	
	/**
	 * @author D'Mita Levy
	 * Performs all password encrypt, decrypt, and most security and user authentication
	 * 			-generateStrongPasswordHash, getSalt, and toHex sourced from:
	 * 			 source: http://howtodoinjava.com/2013/07/22/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
	 *
	 * Last Update: 1 - 26 - 2015
	 */
	
	
	//the number of acceptable iterations for the creation of a password using PBKDF2
	final static int iterations = 1000;
	
	//never - ever - ever change me ever
	public final static String theSalt ="[B@78b75391";
	
	public CCAuthenticationTool()
	{
		
	}

	public String generateStrongPasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException
	{
	        char[] chars = password.toCharArray();
	        byte[] salt = theSalt.getBytes();
	         
	        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
	        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
	        byte[] hash = skf.generateSecret(spec).getEncoded();
	        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
	 }
	
	 public String getSalt() throws NoSuchAlgorithmException
	 {
	        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
	        byte[] salt = new byte[16];
	        sr.nextBytes(salt);
	        return salt.toString();
	 }
	     
	    /*
	     * source: http://howtodoinjava.com/2013/07/22/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
	     */
	 public String toHex(byte[] array) throws NoSuchAlgorithmException
	 {
	        BigInteger bi = new BigInteger(1, array);
	        String hex = bi.toString(16);
	        int paddingLength = (array.length * 2) - hex.length();
	        if(paddingLength > 0)
	        {
	            return String.format("%0"  +paddingLength + "d", 0) + hex;
	        }else{
	            return hex;
	        }
	 }
	 
}
