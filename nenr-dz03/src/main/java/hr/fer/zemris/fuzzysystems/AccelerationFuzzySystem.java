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
public class AccelerationFuzzySystem extends AbstractFuzzySystem {

	public AccelerationFuzzySystem(Defuzzifier defuzzifier, List<FuzzyRule> rules) {
		super(defuzzifier, rules);
		initializeRules();
	}

	public AccelerationFuzzySystem(Defuzzifier defuzzifier) {
		this(defuzzifier, null);
	}
	
	private void initializeRules() {
		IFuzzySet[] antecedents = new IFuzzySet[] {null, null, null, null, TOO_SLOW, null};
		List<IFuzzySet> tooSlow = new ArrayList<>(Arrays.asList(antecedents));
		
		addRule(new FuzzyRule(tooSlow, ACCELERATE));
		
		List<IFuzzySet> tooFast = new ArrayList<>(Arrays.asList(antecedents));
		tooFast.set(4, TOO_FAST);
		addRule(new FuzzyRule(tooFast, DECELERATE));
	}
	
}
