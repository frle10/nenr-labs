package hr.fer.zemris.fuzzy;

import java.util.Iterator;

public class SimpleDomain extends Domain {

	private int first;

	private int last;

	public SimpleDomain(int first, int last) {
		this.first = first;
		this.last = last;
	}

	@Override
	public int getCardinality() {
		return last - first;
	}

	@Override
	public SimpleDomain getComponent(int index) {
		return this;
	}

	@Override
	public int getNumberOfComponents() {
		return 1;
	}

	@Override
	public Iterator<DomainElement> iterator() {
		return new Iterator<DomainElement>() {
			private int current = first;
			
			@Override
			public boolean hasNext() {
				return current < last ? true : false;
			}

			@Override
			public DomainElement next() {
				return new DomainElement(current++);
			}
		};
	}

	public int getFirst() {
		return first;
	}

	public int getLast() {
		return last;
	}

}
