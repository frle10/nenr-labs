package hr.fer.zemris.fuzzy;

public class MutableFuzzySet implements IFuzzySet {
	
	private IDomain domain;
	
	private double[] memberships;
	
	public MutableFuzzySet(IDomain domain) {
		this.domain = domain;
		memberships = new double[domain.getCardinality()];
	}
	
	@Override
	public IDomain getDomain() {
		return domain;
	}

	@Override
	public double getValueAt(DomainElement element) {
		return memberships[domain.indexOfElement(element)];
	}
	
	public MutableFuzzySet set(DomainElement element, double membership) {
		memberships[domain.indexOfElement(element)] = membership;
		return this;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < domain.getCardinality(); i++) {
			DomainElement element = domain.elementForIndex(i);
			double value = memberships[i];
			
			if (Math.abs(value) > 10e-6)
				sb.append("(" + element + ", " + value + ")" + (i == domain.getCardinality() - 1 ? "" : ", "));
		}
		
		return sb.toString();
	}
	
}
