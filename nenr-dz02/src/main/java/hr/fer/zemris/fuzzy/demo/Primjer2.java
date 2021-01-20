package hr.fer.zemris.fuzzy.demo;

import hr.fer.zemris.fuzzy.Domain;
import hr.fer.zemris.fuzzy.DomainElement;
import hr.fer.zemris.fuzzy.IDomain;
import hr.fer.zemris.fuzzy.IFuzzySet;
import hr.fer.zemris.fuzzy.MutableFuzzySet;
import hr.fer.zemris.fuzzy.Relations;

public class Primjer2 {
	
	public static void main(String[] args) {
		IDomain u1 = Domain.intRange(1, 5); // {1,2,3,4}
		IDomain u2 = Domain.intRange(1, 4); // {1,2,3}
		IDomain u3 = Domain.intRange(1, 5); // {1,2,3,4}
		
		IFuzzySet r1 = new MutableFuzzySet(Domain.combine(u1, u2))
				.set(DomainElement.of(1,1), 0.3)
				.set(DomainElement.of(1,2), 1)
				.set(DomainElement.of(3,3), 0.5)
				.set(DomainElement.of(4,3), 0.5);
		
		IFuzzySet r2 = new MutableFuzzySet(Domain.combine(u2, u3))
				.set(DomainElement.of(1,1), 1)
				.set(DomainElement.of(2,1), 0.5)
				.set(DomainElement.of(2,2), 0.7)
				.set(DomainElement.of(3,3), 1)
				.set(DomainElement.of(3,4), 0.4);
		
		IFuzzySet r1r2 = Relations.compositionOfBinaryRelations(r1, r2);
		
		for(DomainElement e : r1r2.getDomain()) {
			System.out.println("mu(" + e + ")=" + r1r2.getValueAt(e));
		}
	}
	
}
