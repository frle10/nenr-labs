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

}
