package hr.fer.zemris.fuzzy;

import java.util.Iterator;

public abstract class Domain implements IDomain {
	
	public Domain() {}
	
	public static IDomain intRange(int first, int last) {
		return new SimpleDomain(first, last);
	}
	
	public static Domain combine(IDomain first, IDomain second) {
		SimpleDomain[] components = new SimpleDomain[first.getNumberOfComponents() + second.getNumberOfComponents()];
		int i = 0;
		for (; i < first.getNumberOfComponents(); i++) {
			components[i] = first.getComponent(i);
		}
		
		for (int j = 0; j < second.getNumberOfComponents(); j++) {
			components[i + j] = second.getComponent(j);
		}
		
		return new CompositeDomain(components);
	}
	
	public int indexOfElement(DomainElement element) {
		int i = 0;
		Iterator<DomainElement> it = iterator();
		
		while (it.hasNext()) {
			DomainElement elem = it.next();
			if (element.equals(elem)) return i;
			i++;
		}
		
		return -1;
	}
	
	public DomainElement elementForIndex(int index) {
		int i = 0;
		Iterator<DomainElement> it = iterator();
		
		while (it.hasNext()) {
			DomainElement elem = it.next();
			if (i == index) return elem;
			i++;
		}
		
		return null;
	}
	
}
