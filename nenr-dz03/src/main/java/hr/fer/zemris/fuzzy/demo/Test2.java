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

public class Test2 {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		Defuzzifier def = new COADefuzzifier();
		
		AbstractFuzzySystem aSystem = new AccelerationFuzzySystem(def);
		AbstractFuzzySystem hSystem = new HelmFuzzySystem(def);
		
		System.out.print("Enter L, D, LK, DK, V, S: ");
		String[] lineTokens = scanner.nextLine().trim().split("\\s+");
		
		List<Integer> values = new ArrayList<>();
		for (int i = 0; i < lineTokens.length; i++)
			values.add(Integer.parseInt(lineTokens[i]));
		
		IFuzzySet aConclusion = aSystem.determineHelper(values, Constants.MINIMUM_ENGINE);
		System.out.println("The acceleration conclusion is: " + aConclusion);
		System.out.println();
		
		System.out.println("The final acceleration value is: " + def.decode(aConclusion));
		System.out.println();
		
		IFuzzySet hConclusion = hSystem.determineHelper(values, Constants.MINIMUM_ENGINE);
		System.out.println("The angle conclusion is: " + hConclusion);
		System.out.println();
		
		System.out.println("The final angle value is: " + def.decode(hConclusion));
		System.out.println();
		
		scanner.close();
	}

}
