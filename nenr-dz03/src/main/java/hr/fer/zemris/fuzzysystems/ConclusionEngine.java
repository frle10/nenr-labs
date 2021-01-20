package hr.fer.zemris.fuzzysystems;

import hr.fer.zemris.fuzzy.DomainElement;
import hr.fer.zemris.fuzzy.IFuzzySet;

@FunctionalInterface
public interface ConclusionEngine {
	
	double conclude(double value, IFuzzySet predicate, DomainElement element);
	
}
