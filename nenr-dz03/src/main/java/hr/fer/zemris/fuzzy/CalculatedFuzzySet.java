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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < domain.getCardinality(); i++) {
			DomainElement element = domain.elementForIndex(i);
			double value = membershipFunction.valueAt(i);
			
			if (Math.abs(value) > 10e-6)
				sb.append("(" + element + ", " + value + ")" + (i == domain.getCardinality() - 1 ? "" : ", "));
		}
		
		return sb.toString();
	}
	
}
