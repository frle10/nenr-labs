package hr.fer.zemris.fuzzysystems;

import java.util.List;

import hr.fer.zemris.fuzzy.DomainElement;
import hr.fer.zemris.fuzzy.IFuzzySet;
import hr.fer.zemris.fuzzy.MutableFuzzySet;

/**
 * 
 * 
 * @author Ivan Skorupan
 */
public class FuzzyRule {

	private List<IFuzzySet> antecedents;
	
	private IFuzzySet consequence;
	
	public FuzzyRule(List<IFuzzySet> antecedents, IFuzzySet consequence) {
		this.antecedents = antecedents;
		this.consequence = consequence;
	}
	
	public IFuzzySet implication(List<Integer> values, ConclusionEngine engine) {
		double membership = 1.;
		
		int i = 0;
		for (; i < antecedents.size(); i++) {
			IFuzzySet antecedent = antecedents.get(i);
			
			if (antecedent != null)
				membership = engine.conclude(membership, antecedent, DomainElement.of(values.get(i)));
		}
		
		MutableFuzzySet localConclusion = new MutableFuzzySet(consequence.getDomain());
		for (i = 0; i < localConclusion.getDomain().getCardinality(); i++) {
			DomainElement element = localConclusion.getDomain().elementForIndex(i);
			localConclusion.set(element, engine.conclude(membership, consequence, element));
		}
		
		return localConclusion;
	}

	public List<IFuzzySet> getAntecedents() {
		return antecedents;
	}

	public IFuzzySet getConsequence() {
		return consequence;
	}
	
}
