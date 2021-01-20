package hr.fer.zemris.fuzzysystems;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.fuzzy.IFuzzySet;

import static hr.fer.zemris.fuzzy.Operations.*;

/**
 * 
 * 
 * @author Ivan Skorupan
 */
public abstract class AbstractFuzzySystem implements FuzzySystem {
	
	private Defuzzifier defuzzifier;
	
	private List<FuzzyRule> rules;
	
	public AbstractFuzzySystem(Defuzzifier defuzzifier, List<FuzzyRule> rules) {
		this.defuzzifier = defuzzifier;
		this.rules = (rules == null) ? new ArrayList<>() : rules;
	}
	
	public AbstractFuzzySystem(Defuzzifier defuzzifier) {
		this(defuzzifier, null);
	}
	
	@Override
	public int determine(List<Integer> values, ConclusionEngine engine) {
		IFuzzySet determinedValue = determineHelper(values, engine);
		
		return defuzzifier.decode(determinedValue);
	}
	
	public IFuzzySet determineHelper(List<Integer> values, ConclusionEngine engine) {
		IFuzzySet determinedValue = rules.get(0).implication(values, engine);
		for (int i = 1; i < rules.size(); i++) {
			FuzzyRule rule = rules.get(i);
			determinedValue = binaryOperation(determinedValue, rule.implication(values, engine), ZADEH_OR);
		}
		
		return determinedValue;
	}
	
	public void addRule(FuzzyRule rule) {
		rules.add(rule);
	}
	
	public FuzzyRule getRule(int index) {
		return rules.get(index);
	}

	public Defuzzifier getDefuzzifier() {
		return defuzzifier;
	}

	public List<FuzzyRule> getRules() {
		return rules;
	}

	public void setDefuzzifier(Defuzzifier defuzzifier) {
		this.defuzzifier = defuzzifier;
	}

	public void setRules(List<FuzzyRule> rules) {
		this.rules = rules;
	}
	
}
