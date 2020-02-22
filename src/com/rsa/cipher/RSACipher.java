package com.rsa.cipher;

import java.math.BigInteger;
import java.security.SecureRandom;

public class RSACipher {
	private final static SecureRandom random = new SecureRandom();

	public final BigInteger modulus;
	public BigInteger publicKey = new BigInteger("65537");
	private final BigInteger privateKey;
	private  BigInteger phi;
	private BigInteger p;
	private BigInteger q;
	
	
	/***
	 * Class constructor for default operation
	 * 
	 * 
	 ***/
	public RSACipher(int nBits) {

		do {
			this.p = BigInteger.probablePrime(nBits / 2, random);
			this.q = BigInteger.probablePrime(nBits / 2, random);
			this.phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
		} while (!publicKey.gcd(phi).equals(BigInteger.ONE) || p.equals(q));

		this.modulus = p.multiply(q);
		this.privateKey = publicKey.modInverse(phi);
	}

	/***
	 * Class constructor for custom RSA operation
	 * 
	 * 
	 ***/
	public RSACipher(BigInteger mod, BigInteger publicKey, BigInteger privateKey, BigInteger p, BigInteger q) {
		this.publicKey = publicKey;
		this.phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
		modulus = mod;
		this.privateKey = privateKey;
		System.out.println("p = " + p);
		System.out.println("q = " + q);
		System.out.println("phi = " + phi);
		System.out.println("modulus = " + mod);
		System.out.println("privateKey = " + privateKey);
	}

	/***
	 * Encrypt data operation on decimal base
	 * 
	 * @param message input message on decimal (decode on Ascii format )
	 * @return encrypt data on decimal base 
	 * **/
	public BigInteger encrypt(BigInteger message) throws Exception {
		if (message.compareTo(modulus) >= 0) {
			throw new Exception("m: " + message + " >= modulus: " + modulus +" can't encrypt ");
		}
		return message.modPow(publicKey, modulus);
	}

	/***
	 * Decrypt operation on decimal base
	 * 
	 * @param encrypted data input 
	 * @return decrypted data
	 * **/
	public BigInteger decrypt(BigInteger encrypted) {
		return encrypted.modPow(privateKey, modulus);
	}

	
}
