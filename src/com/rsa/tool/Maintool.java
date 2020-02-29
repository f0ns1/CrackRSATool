package com.rsa.tool;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import com.rsa.tool.actions.AttackMode;
import com.rsa.tool.utils.Utils;


public class Maintool {
	private static final String MODE_ENCRYPT = "ENCRYPT";
	private static final String MODE_CRACK = "CRACK";
	private static  int MAX_THREADS;
	private static String trace="10000";
	private static Utils utl = new Utils();
	
		public static void main(String[] args) {
			System.out.println("=======================================================");
			System.out.println("||        Break module tool for RSA algorithm ");
			System.out.println("=======================================================");
			String modulus = "48789828618640905340268650455641742611513932500406667";
			modulus = "12345";//args[0];
			BigInteger publicKey = new BigInteger("65573");
			trace ="100";//args[2];
			BigInteger m = new BigInteger(modulus);
			System.out.println("Break module : " + modulus);
			System.out.println("module length : " + modulus.length());
			AttackMode attack = new AttackMode();
			List <HashMap<String, BigInteger>> foundKeyPair= attack.foundKeyPairs(m, trace, modulus);
			System.out.println("\n\nExport To file : [Y/N]");
			Scanner scanner = new Scanner(System.in);
			String Y = scanner.nextLine();
			if (Y.equalsIgnoreCase("y")) {
				System.out.println("Enter file input path: ");
				String file = scanner.nextLine();
				utl.createFile(file, foundKeyPair, m, publicKey);
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
				attack.crackDataMethod(foundKeyPair, modulus, publicKey, message);

			}

		}

		

		

}
