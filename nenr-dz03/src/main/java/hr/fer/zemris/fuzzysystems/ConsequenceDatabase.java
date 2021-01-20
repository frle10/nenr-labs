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
public class ConsequenceDatabase {

	public static final IFuzzySet ACCELERATE = new CalculatedFuzzySet(ACCELERATION_DOMAIN, gammaFunction(MAX_ACCELERATION * 4 / 3, MAX_ACCELERATION * 5 / 3));

	public static final IFuzzySet DECELERATE = new CalculatedFuzzySet(ACCELERATION_DOMAIN, lFunction(0, MAX_ACCELERATION / 3));

	public static final IFuzzySet SHARP_LEFT = new CalculatedFuzzySet(ANGLE_DOMAIN, gammaFunction(175, 180));

	public static final IFuzzySet SHARP_RIGHT = new CalculatedFuzzySet(ANGLE_DOMAIN, lFunction(0, 5));

	// public static final IFuzzySet LEFT = new CalculatedFuzzySet(ANGLE_DOMAIN, gammaFunction(145, 150));

	// public static final IFuzzySet RIGHT = new CalculatedFuzzySet(ANGLE_DOMAIN, lFunction(30, 35));

	public static final IFuzzySet FLIP = new CalculatedFuzzySet(ANGLE_DOMAIN, gammaFunction(179, 180));

}
