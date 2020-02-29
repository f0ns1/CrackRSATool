package com.rsa.tool.actions;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class GeneratePrimes {

	/****
	 * Class constructor 
	 * 
	 * ***/
	public GeneratePrimes() {
		
	}
	
	
	/****
	 * This method is used to generate prime numbers using brute force from Zero to input number
	 * 
	 * @param m max prime number
	 * @param traceLength to for interface refresh
	 * @return list with prime numbers founs
	 * ***/
	public  List<BigInteger> primeNumbersBruteForce(BigInteger n,String traceLength) {
		List<BigInteger> primeNumbers = new ArrayList();
		BigInteger process = new BigInteger("0");
		BigInteger trace = new BigInteger(traceLength);
		for (BigInteger i = new BigInteger("2"); i.compareTo(n) < 0; i = i.add(BigInteger.ONE)) {
			if (isPrimeBruteForce(i)) {
				primeNumbers.add(i);
				process=process.add(BigInteger.ONE);
				if(process.compareTo(trace) == 0) {
					System.out.println("Working Process ......... : Numnber of primes found = "+primeNumbers.size()+" Iterate status  ["+i+"] of ["+n+"]");
					process= BigInteger.ZERO;
				}
			}
		}
		return primeNumbers;
	}

	
	
	/****
	 * This method is used to know if input number is contained on primeList 
	 * 
	 * @param divide input name
	 * @param primeList number
	 * @return operation status
	 * ****/
	boolean isDivOnPrimeList(BigInteger divide, List<BigInteger> primeList) {
		return primeList.contains(divide);
	}
	
	/****
	 * This method is used to know if input number is prime usign brute force, div by all precc
	 * @param number input  data
	 * @return operation ststus if number is prime
	 * **/
	private  boolean isPrimeBruteForce(BigInteger number) {
		for (BigInteger i = new BigInteger("2"); i.compareTo(number) < 0; i = i.add(BigInteger.ONE)) {
			if (number.mod(i).compareTo(BigInteger.ZERO) == 0) {
				return false;
			}
		}
		return true;
	}

	
	/****
	 * Stack overflow if recursive process > 3 or 4 K
	 * implemented but not used 
	 * *****/
	private  List<BigInteger> primerNumberBruteForceRecursive(List<BigInteger> list, BigInteger n, BigInteger i) {
		if (isPrimeRecursive(i, new BigInteger("2"))) {
			list.add(i);
		}
		return (i.compareTo(n) == 0) ? list : primerNumberBruteForceRecursive(list, n, i.add(BigInteger.ONE));
	}

	/***
	 * This method is used to know if input number is prime usign reursive call.ñ
	 * implemented but not used, because get an stack overflow when calling for most than 3000 or 4000 
	 * 
	 * 
	 * ***/
	private  boolean isPrimeRecursive(BigInteger number, BigInteger i) {
		if (number.compareTo(i) == 0) {
			return true;
		}
		return (number.mod(i).compareTo(BigInteger.ZERO) == 0) ? false
				: isPrimeRecursive(number, i.add(BigInteger.ONE));
	}

}
