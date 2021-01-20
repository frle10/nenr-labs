package hr.fer.zemris.fuzzy.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import hr.fer.zemris.fuzzy.IFuzzySet;
import hr.fer.zemris.fuzzysystems.AbstractFuzzySystem;
import hr.fer.zemris.fuzzysystems.AccelerationFuzzySystem;
import hr.fer.zemris.fuzzysystems.COADefuzzifier;
import hr.fer.zemris.fuzzysystems.Constants;
import hr.fer.zemris.fuzzysystems.Defuzzifier;
import hr.fer.zemris.fuzzysystems.HelmFuzzySystem;

public class Test1 {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		Defuzzifier def = new COADefuzzifier();
		
		System.out.print("Enter A for acceleration system or K for helm system: ");
		String systemMark = scanner.nextLine();
		
		AbstractFuzzySystem system = systemMark.equals("A") ? new AccelerationFuzzySystem(def) : new HelmFuzzySystem(def);
		System.out.print("Enter L, D, LK, DK, V, S: ");
		String[] lineTokens = scanner.nextLine().trim().split("\\s+");
		
		List<Integer> values = new ArrayList<>();
		for (int i = 0; i < lineTokens.length; i++)
			values.add(Integer.parseInt(lineTokens[i]));
		
		System.out.print("Choose rule index from 0 to " + (system.getRules().size() - 1) + ": ");
		int ruleIndex = Integer.parseInt(scanner.nextLine());
		
		IFuzzySet conclusion = system.getRule(ruleIndex).implication(values, Constants.MINIMUM_ENGINE);
		System.out.println("The conclusion is: " + conclusion);
		System.out.println();
		
		System.out.println("The final value is: " + def.decode(conclusion));
		System.out.println();
		scanner.close();
	}

}
