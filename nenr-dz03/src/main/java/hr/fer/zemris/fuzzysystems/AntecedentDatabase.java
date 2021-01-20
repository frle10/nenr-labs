package hr.fer.zemris.fuzzysystems;

import hr.fer.zemris.fuzzy.CalculatedFuzzySet;
import hr.fer.zemris.fuzzy.IFuzzySet;

import static hr.fer.zemris.fuzzysystems.Constants.*;
import static hr.fer.zemris.fuzzy.StandardFuzzySets.*;

/**
 * 
 * 
 * @author Ivan Skorupan
 */
public class AntecedentDatabase {
	
	public static final IFuzzySet WRONG_DIRECTION = new CalculatedFuzzySet(DIRECTION_DOMAIN, lFunction(0, 1));
	
	public static final IFuzzySet TOO_CLOSE = new CalculatedFuzzySet(DISTANCE_DOMAIN, lFunction(45, 55));
	
	// public static final IFuzzySet FAR_TOO_CLOSE = new CalculatedFuzzySet(DISTANCE_DOMAIN, lFunction(35, 45));
	
	public static final IFuzzySet TOO_SLOW = new CalculatedFuzzySet(VELOCITY_DOMAIN, lFunction(50, 80));
	
	public static final IFuzzySet TOO_FAST = new CalculatedFuzzySet(VELOCITY_DOMAIN, gammaFunction(80, 90));
	
}
