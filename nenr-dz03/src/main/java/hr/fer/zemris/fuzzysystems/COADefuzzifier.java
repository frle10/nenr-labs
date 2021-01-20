package hr.fer.zemris.fuzzysystems;

import java.util.Iterator;

import hr.fer.zemris.fuzzy.DomainElement;
import hr.fer.zemris.fuzzy.IDomain;
import hr.fer.zemris.fuzzy.IFuzzySet;

import static java.lang.Math.round;

/**
 * 
 * 
 * @author Ivan Skorupan
 */
public class COADefuzzifier implements Defuzzifier {
	
	@Override
	public int decode(IFuzzySet fuzzySet) {
		IDomain domain = fuzzySet.getDomain();
		Iterator<DomainElement> it = domain.iterator();
		
		double numerator = 0, denominator = 0;
		while (it.hasNext()) {
			DomainElement de = it.next();
			double membership = fuzzySet.getValueAt(de);
			numerator += membership * de.getComponentValue(0);
			denominator += membership;
		}
		
		return (int)round(numerator / denominator);
	}
	
}
