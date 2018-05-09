package biz;

import java.util.function.Supplier;
import java.util.List;
import java.util.ArrayList;

import lib.Pair;

public final class Menu {
	private List<Pair<String, Supplier<String>>> options;
	private Supplier<String> additionalHeaderInfo;
	private String APP_NAME;
	
	public Menu(String appName, Supplier<String> additionalHeaderInfo, List<Pair<String, Supplier<String>>> options) {
		if(appName == null) {
			throw new IllegalArgumentException("Menu Constructor : appName must have a value");
		}
		
		this.APP_NAME = appName;
		
		if(options == null) {
			throw new IllegalArgumentException("Menu Constructor : options must have a value");
		}
		
		this.options = new ArrayList<Pair<String, Supplier<String>>>(options.size());
		
		for(Pair<String, Supplier<String>> option : options) {
			if(option == null) {
				throw new IllegalArgumentException("Menu Constructor : all option must have values");
			}
			
			this.options.add(option);
		}
		
		this.additionalHeaderInfo = additionalHeaderInfo;
	}
	
	public void run() {
		Integer userChoice;
		do {
			System.out.println(buildMenuHeader());
			
			if(this.additionalHeaderInfo != null) {
				System.out.println(this.additionalHeaderInfo.get());
			}
			
			for(int i = 0; i < options.size(); i++) {
				System.out.println((i + 1) + ".\t" + options.get(i).key);
			}
			System.out.println("0.\tExit " + this.APP_NAME);
			
			userChoice = BizUtil.getValidUserInput(
				"Please Enter your Choice",
				"You must enter a valid integer from 0 to " + this.options.size() + 1,
				(String input) -> {
					if(input == null || input.trim().equals("") || !BizUtil.validParse(input)) {
						return false;
					}
					
					int inputNum = Integer.parseInt(input);
					
					return inputNum >= 0 && inputNum <= this.options.size() + 1;
				},
				(String input) -> {
					return Integer.parseInt(input);
				}
			);
			
			for(int i = 0; i < this.options.size(); i++) {
				if(i + 1 == userChoice) {
					System.out.println(this.options.get(i).value.get());
					System.out.println('\n');
				}
			}
			
		} while(userChoice != 0);
	}
	
	private String buildMenuHeader() {
		String pad = "**********";
		
		StringBuilder aroundPad = new StringBuilder(pad);
		for(int i = 0; i < this.APP_NAME.length() + 2; i++) {
			aroundPad.append('*');
		}
		aroundPad.append(pad);
		
		return aroundPad.toString() + '\n' + pad + ' ' + this.APP_NAME + ' ' + pad + '\n' + aroundPad.toString() + '\n';
	}
}