package hr.fer.zemris.fuzzy;

public interface IFuzzySet {
	
	IDomain getDomain();
	
	double getValueAt(DomainElement element);
	
}
