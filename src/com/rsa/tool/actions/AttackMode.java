package com.rsa.tool.actions;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import javax.swing.text.Utilities;

import com.rsa.tool.utils.Utils;

public class AttackMode {
	private final String HEADER = "AttackMode.";

	private GeneratePrimes primes;
	private static Utils utl;
	private String p="176 795785 843333 659350 390219";
	private String q="275 967146 987743 635463 977793";
	public AttackMode() {	
		primes = new GeneratePrimes();
		utl = new Utils();
	}

	public List<HashMap<String, BigInteger>> foundKeyPairs(BigInteger m, String publickKey, String trace) {
		System.out.println(HEADER + "founKeyPair()    init method ");
		List<BigInteger> primeList = primes.primeNumbersBruteForce(m, trace);
		BigInteger it = new BigInteger("2");
		List<BigInteger> primeListVoid = new ArrayList<>();
		System.out.println("All prime numbers for module : " + primeList.size());
		boolean found = false;
		List<HashMap<String, BigInteger>> foundKeyPair = new ArrayList();
		for (BigInteger p : primeList) {
			found = primes.isDivOnPrimeList(m.divide(p), primeList);
			if (found) {
				HashMap<String, BigInteger> map = new HashMap<>();
				map.put("p", p);
				map.put("q", m.divide(p));
				foundKeyPair.add(map);
			}
		}
		BigInteger p;
		BigInteger q;
		BigInteger phi;
		BigInteger publicKey = new BigInteger(publickKey);
		BigInteger privateKey;
		System.out.println(HEADER+"Show RSA posibilities for module :..........................................");
		for (HashMap<String, BigInteger> map : foundKeyPair) {
			try {
			p = map.get("p");
			q = map.get("q");
			phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
			System.out.println(HEADER+"PublicKey = "+publickKey);
			System.out.println(HEADER+"phi = "+phi);
			privateKey = publicKey.modInverse(phi);
			System.out.println(HEADER+"privateKey = "+phi);
			System.out.println("=================" + p.multiply(q).toString() + "==========================");
			System.out.println("\t p : " + p + " length = " + p.toString().length());
			System.out.println("\t q : " + q + " length = " + q.toString().length());
			System.out.println("\t phi : " + phi + " length = " + phi.toString().length());
			System.out.println("\t publicKey : " + publicKey + " length = " + publicKey.toString().length());
			System.out.println("\t private : " + privateKey + " length = " + privateKey.toString().length());
			System.out.println("\t pmodulus : " + m + " length = " + m.toString().length());
			System.out.println("\t\t verify : publicKey.gcd(phi).equals(BigInteger.ONE) "
					+ publicKey.gcd(phi).equals(BigInteger.ONE));
			System.out.println("\t\t verify : p.equals(q) " + p.equals(q));
			System.out.println("\t\t verify : p.multiply(q) " + p.multiply(q));
			System.out.println("============================================");
			}catch(Exception e) {
				
			}
		}
		System.out.println(HEADER + "foundKeyPairs() return values = " + foundKeyPair.size());
		return foundKeyPair;

	}

	/***
	 * This method is used to find a keyPair that pxq = modulus In that case we can
	 * break the RSA algorithm and use this Object (p,q,phi and privateKey) to
	 * generate an RSA object for decrypt RSA algorithm
	 * 
	 **/
	public static void crackDataMethod(List<HashMap<String, BigInteger>> foundKeyPair, String modulus,
			BigInteger publicKey, String message) {
		BigInteger i = new BigInteger("1");
		List<HashMap<String, Object>> attackList = new ArrayList();
		for (HashMap<String, BigInteger> map : foundKeyPair) {
			HashMap<String, Object> mapAttack = new HashMap<>();
			BigInteger p = map.get("p");
			BigInteger q = map.get("q");
			BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
			BigInteger privateKey = publicKey.modInverse(phi);
			BigInteger cracked = crack(new BigInteger(message), privateKey, new BigInteger(modulus));
			System.out.println(" \n ========================Option [" + i + "]");
			System.out.println(" p : q " + p + " : " + q);
			System.out.println(" phi " + phi);
			System.out.println(" modulus " + modulus);
			System.out.println(" publicKey : privateKey " + publicKey + " : " + privateKey);
			System.out.println(" encrypted : cracked  " + message + " : " + cracked.toString());
			System.out.println("\t Verify : pxq " + p.multiply(q) + "  m - (pxq) "
					+ new BigInteger(modulus).subtract(p.multiply(q)));

			i = i.add(BigInteger.ONE);
			mapAttack.put("p", p);
			mapAttack.put("q", q);
			mapAttack.put("phi", phi);
			mapAttack.put("modulus", modulus);
			mapAttack.put("privateKey", privateKey);
			mapAttack.put("publicKey", publicKey);
			mapAttack.put("message", message);
			mapAttack.put("cracked", cracked);
			attackList.add(mapAttack);
		}
		System.out.println(" \n ================================================ \n");
		System.out.println(" \n =================== End Attack =================  \n");
		System.out.println("Do You Wat to save on File ? : [Y/N]");
		Scanner scanner = new Scanner(System.in);
		String save = scanner.nextLine();
		if (save.equalsIgnoreCase("y")) {
			System.out.println("Enter file path : ");
			String path = scanner.nextLine();
			utl.createFileAttack(attackList, path);
		}

	}

	public static BigInteger crack(BigInteger message, BigInteger privateKey, BigInteger modulus) {
		return message.modPow(privateKey, modulus);
	}

}
