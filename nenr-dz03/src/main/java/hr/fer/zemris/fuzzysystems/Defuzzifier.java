package hr.fer.zemris.fuzzysystems;

import hr.fer.zemris.fuzzy.IFuzzySet;

/**
 * 
 * 
 * @author Ivan Skorupan
 */
public interface Defuzzifier {
	
	int decode(IFuzzySet fuzzySet);
	
}
