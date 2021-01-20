package hr.fer.zemris.fuzzy;

import java.util.Arrays;

public class DomainElement {

	private int[] values;

	public DomainElement(int... values) {
		this.values = values;
	}

	public int getNumberOfComponents() {
		return values.length;
	}

	public int getComponentValue(int index) {
		return values[index];
	}

	public static DomainElement of(int... values) {
		return new DomainElement(values);
	}

	@Override
	public String toString() {
		if (values.length == 1) return String.valueOf(values[0]);
		
		StringBuilder sb = new StringBuilder("(");
		
		for (int value : values) {
			sb.append(value + ",");
		}
		
		sb.deleteCharAt(sb.length() - 1);
		sb.append(")");
		
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(values);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof DomainElement))
			return false;
		DomainElement other = (DomainElement) obj;
		return Arrays.equals(values, other.values);
	}

}
