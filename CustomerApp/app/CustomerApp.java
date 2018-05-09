package app;

import lib.UserInputUtil;

public final class CustomerApp {
	private CustomerApp() {
	}
	
	public static void main(String[] args) {
		char userInput = 'q';
		
		do {
			userInput = UserInputUtil.getCharInput(
				"Please enter your choice",
				"Must be a valid character",
				(c) -> {return true;}
			);
		}while(userInput != 'q');
	}
}