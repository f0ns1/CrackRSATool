package com.rsa.tool.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;

public class Utils {
	private static final String HEADER="Utils.";
	/***
	 * Classs constructor
	 * 
	 * **/
	public Utils() {
		
	}
	
	public static void createFileAttack(List<HashMap<String, Object>> attackList, String path) {
		BufferedWriter bw = null;
		try {
			File file = new File(path);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			BigInteger i = new BigInteger("1");
			for(HashMap<String, Object>map : attackList) {
				BigInteger p=(BigInteger)map.get("p");
				BigInteger q= (BigInteger)map.get("q");
				Object phi= map.get("phi");
				Object modulus=map.get("modulus");
				Object privateKey= map.get("privateKey");
				Object publickey=map.get("publicKey");
				Object message= map.get("message");
				Object cracked= map.get("cracked");
				bw.write(" \n ========================Option [" + i + "]");
				bw.write("\n p : q " + p + " : " + q);
				bw.write("\n phi " + phi);
				bw.write("\n modulus " + modulus);
				bw.write("\n publicKey : privateKey " + publickey + " : " + privateKey);
				bw.write("\n encrypted : cracked  " + message + " : " + cracked);
				bw.write("\n\t Verify : pxq "+p.multiply(q)+"  m - (pxq) "+new BigInteger(modulus.toString()).subtract(p.multiply(q)));
				i= i.add(BigInteger.ONE);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
			} catch (Exception ex) {
				System.out.println(HEADER+"Error in closing the BufferedWriter" + ex);
			}
		}
	}



	public static void createFile(String fileName, List<HashMap<String, BigInteger>> foundKeyPair, BigInteger modulus,
			BigInteger publicKey) {
		BufferedWriter bw = null;
		try {
			File file = new File(fileName);

			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			BigInteger i = new BigInteger("1");
			for (HashMap<String, BigInteger> map : foundKeyPair) {
				bw.write("RSA data algorithm : [" + i + "]");
				bw.write(" [p,q] ");
				bw.write(map.values().toString());
				BigInteger p = map.get("p");
				BigInteger q = map.get("q");
				bw.write(" [phi] ");
				BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
				bw.write(phi.toString());
				BigInteger privateKey = publicKey.modInverse(phi);
				bw.write(" [privatekey , publickkey] ");
				bw.write(" [" + privateKey + "," + publicKey + "]");
				bw.write("\n");
				i = i.add(BigInteger.ONE);
			}
			System.out.println(HEADER+"File written Successfully");

		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
			} catch (Exception ex) {
				System.out.println(HEADER+"Error in closing the BufferedWriter" + ex);
			}
		}
	}
}
