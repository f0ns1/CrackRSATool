package crackRSATool;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;


public class Maintool {
	private static final String MODE_ENCRYPT = "ENCRYPT";
	private static final String MODE_CRACK = "CRACK";
	private static  int MAX_THREADS;


		public static void main(String[] args) {
			System.out.println("=======================================================");
			System.out.println("||        Break module tool for RSA algorithm ");
			System.out.println("=======================================================");
			String modulus = "48789828618640905340268650455641742611513932500406667";
			modulus = args[0];

			BigInteger m = new BigInteger(modulus);
			System.out.println("Break module : " + modulus);
			System.out.println("module length : " + modulus.length());

			 List<BigInteger> primeList = primeNumbersBruteForce(m);
			// List<BigInteger> primeList = null;
			BigInteger it = new BigInteger("2");
			List<BigInteger> primeListVoid = new ArrayList<>();
			//List<BigInteger> primeList = primerNumberBruteForceRecursive(primeListVoid, m, it);
			System.out.println("All prime numbers for module : " + primeList.size());
			boolean found = false;
			List<HashMap<String, BigInteger>> foundKeyPair = new ArrayList();
			for (BigInteger p : primeList) {
				found = isDivOnPrimeList(m.divide(p), primeList);
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
			BigInteger publicKey = new BigInteger(args[1]);
			BigInteger privateKey;
			System.out.println("Show RSA posibilities for module :..........................................");
			for (HashMap<String, BigInteger> map : foundKeyPair) {
				p = map.get("p");
				q = map.get("q");
				phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
				privateKey = publicKey.modInverse(phi);

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

			}

			System.out.println("\n\nExport To file : [Y/N]");
			Scanner scanner = new Scanner(System.in);
			String Y = scanner.nextLine();
			if (Y.equalsIgnoreCase("y")) {
				System.out.println("Enter file input path: ");
				String file = scanner.nextLine();
				createFile(file, foundKeyPair, m, publicKey);
			}

			System.out.println("\n\n Continuae with RSA  : [Y/N]");
			
			String continueAttack = scanner.nextLine();
			if (continueAttack.equalsIgnoreCase("y")) {
				System.out.println("==========================================");
				System.out.println("|| Start Math Attack for break RSA");
				System.out.println("==========================================");
				System.out.println("Mathematical Posibilities for decryption  : " + foundKeyPair.size());
				System.out.println("Enter Encrypted message : ");
				String message = scanner.nextLine();
				crackDataMethod(foundKeyPair, modulus, publicKey, message);

			}

		}

		private static void crackDataMethod(List<HashMap<String, BigInteger>> foundKeyPair, String modulus,
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
				System.out.println("\t Verify : pxq "+p.multiply(q)+"  m - (pxq) "+new BigInteger(modulus).subtract(p.multiply(q)));
				
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
				createFileAttack(attackList, path);
			}

		}

		private static void createFileAttack(List<HashMap<String, Object>> attackList, String path) {
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
					System.out.println("Error in closing the BufferedWriter" + ex);
				}
			}
		}

		public static BigInteger crack(BigInteger message, BigInteger privateKey, BigInteger modulus) {
			return message.modPow(privateKey, modulus);
		}

		private static void createFile(String fileName, List<HashMap<String, BigInteger>> foundKeyPair, BigInteger modulus,
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
				System.out.println("File written Successfully");

			} catch (IOException ioe) {
				ioe.printStackTrace();
			} finally {
				try {
					if (bw != null)
						bw.close();
				} catch (Exception ex) {
					System.out.println("Error in closing the BufferedWriter" + ex);
				}
			}
		}

		private static boolean isDivOnPrimeList(BigInteger divide, List<BigInteger> primeList) {
			return primeList.contains(divide);
		}

		public static List<BigInteger> primeNumbersBruteForce(BigInteger n) {
			List<BigInteger> primeNumbers = new ArrayList();
			for (BigInteger i = new BigInteger("2"); i.compareTo(n) < 0; i = i.add(BigInteger.ONE)) {
				if (isPrimeBruteForce(i)) {
					primeNumbers.add(i);
				}
			}
			return primeNumbers;
		}

		public static boolean isPrimeBruteForce(BigInteger number) {
			for (BigInteger i = new BigInteger("2"); i.compareTo(number) < 0; i = i.add(BigInteger.ONE)) {
				if (number.mod(i).compareTo(BigInteger.ZERO) == 0) {
					return false;
				}
			}
			return true;
		}

		private static List<BigInteger> primerNumberBruteForceRecursive(List<BigInteger> list, BigInteger n, BigInteger i) {
			if (isPrimeRecursive(i, new BigInteger("2"))) {
				list.add(i);
			}
			//System.out.println("it = " + i.toString() + " numer " + n);
			return (i.compareTo(n) == 0) ? list : primerNumberBruteForceRecursive(list, n, i.add(BigInteger.ONE));
		}

		private static boolean isPrimeRecursive(BigInteger number, BigInteger i) {
			if (number.compareTo(i) == 0) {
				return true;
			}
			return (number.mod(i).compareTo(BigInteger.ZERO) == 0) ? false
					: isPrimeRecursive(number, i.add(BigInteger.ONE));
		}

}
