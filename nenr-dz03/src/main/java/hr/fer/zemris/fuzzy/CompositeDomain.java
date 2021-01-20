package hr.fer.zemris.fuzzy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CompositeDomain extends Domain {
	
	private SimpleDomain[] components;
	
	public CompositeDomain(SimpleDomain... components) {
		this.components = components;
	}
	
	@Override
	public int getCardinality() {
		int cardinality = 1;
		for (int i = 0; i < components.length; i++) {
			cardinality *= components[i].getCardinality();
		}
		
		return cardinality;
	}

	@Override
	public SimpleDomain getComponent(int index) {
		return components[index];
	}

	@Override
	public int getNumberOfComponents() {
		return components.length;
	}

	@Override
	public Iterator<DomainElement> iterator() {
		return new CompositeDomainIterator();
	}
	
	private class CompositeDomainIterator implements Iterator<DomainElement> {
		
		private List<Iterator<DomainElement>> componentIterators = new ArrayList<>();
		
		private int[] current = new int[components.length];
		
		public CompositeDomainIterator() {
			for (int i = 0; i < components.length; i++) {
				Iterator<DomainElement> iter = components[i].iterator();
				componentIterators.add(iter);
				if (i < components.length - 1) current[i] = iter.next().getComponentValue(0);
			}
		}
		
		@Override
		public boolean hasNext() {
			for (Iterator<DomainElement> iter : componentIterators) {
				if (iter.hasNext()) return true;
			}
			
			return false;
		}

		@Override
		public DomainElement next() {
			int cursor;
			
			for (cursor = componentIterators.size() - 1; cursor >= 0; cursor--)
				if (componentIterators.get(cursor).hasNext()) break;
			
			for (int i = cursor + 1; i < componentIterators.size(); i++)
				componentIterators.set(i, components[i].iterator());
			
			for (int i = cursor; i < componentIterators.size(); i++)
				current[i] = componentIterators.get(i).next().getComponentValue(0);
			
			return new DomainElement(current.clone());
		}
		
	}

}
