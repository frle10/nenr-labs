package hr.fer.zemris.fuzzy;

public class CalculatedFuzzySet implements IFuzzySet {
	
	private IDomain domain;
	
	private IIntUnaryFunction membershipFunction;
	
	public CalculatedFuzzySet(IDomain domain, IIntUnaryFunction membershipFunction) {
		this.domain = domain;
		this.membershipFunction = membershipFunction;
	}
	
	@Override
	public IDomain getDomain() {
		return domain;
	}

	@Override
	public double getValueAt(DomainElement element) {
		return membershipFunction.valueAt(domain.indexOfElement(element));
	}

}
