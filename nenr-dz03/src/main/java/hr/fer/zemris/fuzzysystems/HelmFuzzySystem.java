package hr.fer.zemris.fuzzysystems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hr.fer.zemris.fuzzy.IFuzzySet;

import static hr.fer.zemris.fuzzysystems.AntecedentDatabase.*;
import static hr.fer.zemris.fuzzysystems.ConsequenceDatabase.*;

/**
 * 
 * 
 * @author Ivan Skorupan
 */
public class HelmFuzzySystem extends AbstractFuzzySystem {

	public HelmFuzzySystem(Defuzzifier defuzzifier, List<FuzzyRule> rules) {
		super(defuzzifier, rules);
		initializeRules();
	}

	public HelmFuzzySystem(Defuzzifier defuzzifier) {
		this(defuzzifier, null);
	}

	private void initializeRules() {
		IFuzzySet[] antecedents = new IFuzzySet[] {null, null, TOO_CLOSE, null, null, null};
		List<IFuzzySet> tooCloseLeft = new ArrayList<>(Arrays.asList(antecedents));

		addRule(new FuzzyRule(tooCloseLeft, SHARP_RIGHT));

		List<IFuzzySet> tooCloseRight = new ArrayList<>(Arrays.asList(antecedents));
		tooCloseRight.set(2, null);
		tooCloseRight.set(3, TOO_CLOSE);
		addRule(new FuzzyRule(tooCloseRight, SHARP_LEFT));
		
		/*
		List<IFuzzySet> tooCloseFullRight = new ArrayList<>(Arrays.asList(antecedents));
		tooCloseFullRight.set(2, null);
		tooCloseFullRight.set(1, FAR_TOO_CLOSE);
		addRule(new FuzzyRule(tooCloseFullRight, SHARP_LEFT));
		
		List<IFuzzySet> tooCloseFullLeft = new ArrayList<>(Arrays.asList(antecedents));
		tooCloseFullLeft.set(2, null);
		tooCloseFullLeft.set(0, FAR_TOO_CLOSE);
		addRule(new FuzzyRule(tooCloseFullLeft, SHARP_RIGHT));
		*/

		List<IFuzzySet> wrongDirection = new ArrayList<>(Arrays.asList(antecedents));
		wrongDirection.set(2, null);
		wrongDirection.set(5, WRONG_DIRECTION);
		addRule(new FuzzyRule(wrongDirection, FLIP));
	}

}
