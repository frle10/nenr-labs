package hr.fer.zemris.fuzzy.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import hr.fer.zemris.fuzzysystems.AccelerationFuzzySystem;
import hr.fer.zemris.fuzzysystems.COADefuzzifier;
import hr.fer.zemris.fuzzysystems.Defuzzifier;
import hr.fer.zemris.fuzzysystems.FuzzySystem;
import hr.fer.zemris.fuzzysystems.HelmFuzzySystem;

import static hr.fer.zemris.fuzzysystems.Constants.*;

/**
 * 
 * 
 * @author Ivan Skorupan
 */
public class Demo {
	
	/**
	 * A string representing the end of further iteration because the program is done.
	 */
	private static final String END = "KRAJ";
	
	/**
	 * The starting point for this program's execution.
	 * 
	 * @param args - command line arguments (not used)
	 */
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		Defuzzifier def = new COADefuzzifier();
		
		FuzzySystem accelerationSystem = new AccelerationFuzzySystem(def);
		FuzzySystem helmSystem = new HelmFuzzySystem(def);
		
		while (true) {
			String inputLine = scanner.nextLine().trim();
			if (inputLine.equals(END)) break;
			
			String[] inputLineTokens = inputLine.split("\\s+");
			List<Integer> values = new ArrayList<>();
			
			for (int i = 0; i < inputLineTokens.length; i++)
				values.add(Integer.parseInt(inputLineTokens[i]));
			
			// System.err.println(values);
			
			int a = accelerationSystem.determine(values, PRODUCT_ENGINE);
			int k = helmSystem.determine(values, PRODUCT_ENGINE);
			
			// System.err.println(a + " " + k);
			
			System.out.println(a + " " + k);
			System.out.flush();
		}
		
		scanner.close();
	}

}
