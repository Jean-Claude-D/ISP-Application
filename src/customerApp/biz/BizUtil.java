package biz;

import java.util.Scanner;
import java.util.function.Predicate;
import java.util.function.Function;

public final class BizUtil {
	private static final Scanner READ = new Scanner(System.in);
	
	private BizUtil() {
	}
	
	public static <T> T getValidUserInput(String message, String expected, Predicate<String> p, Function<String, T> parse) {
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
	
	public static boolean validParse(String num) {
		try {
			Integer.parseInt(num);
		}
		catch(NumberFormatException exc) {
			return false;
		}
		
		return true;
	}
}