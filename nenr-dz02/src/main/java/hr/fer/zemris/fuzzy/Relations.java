package hr.fer.zemris.fuzzy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Relations {
	
	public static boolean isFuzzyEquivalence(IFuzzySet relation) {
		return isReflexive(relation) && isSymmetric(relation) && isMaxMinTransitive(relation);
	}
	
	public static IFuzzySet compositionOfBinaryRelations(IFuzzySet r1, IFuzzySet r2) {
		IDomain r1Domain = r1.getDomain();
		IDomain r2Domain = r2.getDomain();
		
		SimpleDomain u = r1Domain.getComponent(0);
		SimpleDomain w = r2Domain.getComponent(1);
		SimpleDomain v = r1Domain.getComponent(1);
		IDomain domain = Domain.combine(u, w);
		
		MutableFuzzySet composition = new MutableFuzzySet(domain);
		
		Iterator<DomainElement> itX = u.getComponent(0).iterator();
		Iterator<DomainElement> itY = v.getComponent(0).iterator();
		Iterator<DomainElement> itZ = w.getComponent(0).iterator();
		
		List<Double> minimums = new ArrayList<>();
		while (itX.hasNext()) {
			DomainElement currentX = itX.next();
			int x = currentX.getComponentValue(0);
			
			while (itZ.hasNext()) {
				minimums.clear();
				DomainElement currentZ = itZ.next();
				int z = currentZ.getComponentValue(0);
				
				while (itY.hasNext()) {
					DomainElement currentY = itY.next();
					int y = currentY.getComponentValue(0);
					double minimum = Math.min(
							r1.getValueAt(DomainElement.of(x, y)), r2.getValueAt(DomainElement.of(y, z)));
					minimums.add(minimum);
				}
				
				itY = v.getComponent(0).iterator();
				
				double maximum = findMax(minimums);
				composition.set(DomainElement.of(x, z), maximum);
			}
			
			itZ = w.getComponent(0).iterator();
		}
		
		return composition;
	}

	public static boolean isSymmetric(IFuzzySet relation) {
		if (!isUTimesURelation(relation)) return false;
		
		IDomain domain = relation.getDomain();
		Iterator<DomainElement> it = domain.iterator();
		
		while (it.hasNext()) {
			DomainElement current = it.next();
			DomainElement symmetricElement = DomainElement.of(current.getComponentValue(1), current.getComponentValue(0));
			
			if (relation.getValueAt(current) != relation.getValueAt(symmetricElement)) return false;
		}
		
		return true;
	}

	public static boolean isReflexive(IFuzzySet relation) {
		if (!isUTimesURelation(relation)) return false;
		
		IDomain domain = relation.getDomain();
		Iterator<DomainElement> it = domain.getComponent(0).iterator();
		
		while (it.hasNext()) {
			DomainElement current = it.next();
			int currentValue = current.getComponentValue(0);
			
			if (relation.getValueAt(DomainElement.of(currentValue, currentValue)) != 1)
				return false;
		}
		
		return true;
	}

	public static boolean isMaxMinTransitive(IFuzzySet relation) {
		if (!isUTimesURelation(relation)) return false;
		
		IDomain domain = relation.getDomain();
		Iterator<DomainElement> itX = domain.getComponent(0).iterator();
		Iterator<DomainElement> itY = domain.getComponent(0).iterator();
		Iterator<DomainElement> itZ = domain.getComponent(0).iterator();
		
		List<Double> minimums = new ArrayList<>();
		while (itX.hasNext()) {
			DomainElement currentX = itX.next();
			int x = currentX.getComponentValue(0);
			
			while (itZ.hasNext()) {
				minimums.clear();
				DomainElement currentZ = itZ.next();
				int z = currentZ.getComponentValue(0);
				
				while (itY.hasNext()) {
					DomainElement currentY = itY.next();
					int y = currentY.getComponentValue(0);
					double minimum = Math.min(
							relation.getValueAt(DomainElement.of(x, y)), relation.getValueAt(DomainElement.of(y, z)));
					minimums.add(minimum);
				}
				
				itY = domain.getComponent(0).iterator();
				
				double maximum = findMax(minimums);
				if (relation.getValueAt(DomainElement.of(x, z)) < maximum)
					return false;
			}
			
			itZ = domain.getComponent(0).iterator();
		}
		
		return true;
	}

	public static boolean isUTimesURelation(IFuzzySet relation) {
		IDomain domain = relation.getDomain();
		return (domain.getNumberOfComponents() == 2 && domain.getComponent(0).equals(domain.getComponent(1)));
	}
	
	private static double findMax(List<Double> minimums) {
		double maximum = minimums.get(0);
		
		for (int i = 1; i < minimums.size(); i++) {
			double current = minimums.get(i);
			if (current > maximum) maximum = current;
		}
		
		return maximum;
	}

}
