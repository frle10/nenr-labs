package hr.fer.zemris.fuzzy;

import java.util.Iterator;
import java.util.Objects;

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

	@Override
	public int hashCode() {
		return Objects.hash(first, last);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof SimpleDomain))
			return false;
		SimpleDomain other = (SimpleDomain) obj;
		return first == other.first && last == other.last;
	}

}
