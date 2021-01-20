package hr.fer.zemris.fuzzysystems;

import java.util.List;

/**
 * 
 * 
 * @author Ivan Skorupan
 */
public interface FuzzySystem {
	
	int determine(List<Integer> values, ConclusionEngine engine);
	
}
