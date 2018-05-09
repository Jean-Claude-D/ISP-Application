package lib;

import java.util.Scanner;

import java.util.function.Predicate;
import java.util.function.Function;


public final class UserInputUtil {
	private static final Scanner READ = new Scanner(System.in);
	
	private UserInputUtil() {
	}
	
	public static <T> T getValidInput(String message, String expected, Predicate<String> p, Function<String, T> parse) {
		boolean firstTimeAsking = true;
		boolean isValid = false;
		String userInput;
		
		do {
			if(!firstTimeAsking) {
				System.err.println("\n" + expected + "\n");
			}
			
			System.out.println("\n" + message + "\n");
			System.out.print(": ");
			
			userInput = READ.nextLine();
			
			firstTimeAsking = false;
			isValid = p.test(userInput);
		}while(!isValid);
		
		return parse.apply(userInput);
	}
	
	public static String getStringInput(String message, String expected, Predicate<String> p) {
		return getValidInput(message, expected, p, (input) -> {return input;});
	}
	
	public static String getStringInput(String message) {
		return getStringInput(message, "", (input) -> {return true;});
	}
	
	public static char getCharInput(String message, String expected, Predicate<Character> p) {
		boolean firstTimeAsking = true;
		boolean isValid = false;
		char userInput;
		
		do {
			if(!firstTimeAsking) {
				System.err.println("\n" + expected + "\n");
			}
			
			System.out.println("\n" + message + "\n");
			System.out.print(": ");
			
			userInput = READ.next(".").charAt(0);
			
			firstTimeAsking = false;
			isValid = p.test(userInput);
		}while(!isValid);
		
		return userInput;
	}
}