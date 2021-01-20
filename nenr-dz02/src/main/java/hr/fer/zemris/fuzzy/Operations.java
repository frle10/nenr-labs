package hr.fer.zemris.fuzzy;

public class Operations {
	
	private static final IUnaryFunction ZADEH_NOT = new IUnaryFunction() {
		@Override
		public double valueAt(double membership) {
			return 1 - membership;
		}
	};
	
	private static final IBinaryFunction ZADEH_AND = new IBinaryFunction() {
		@Override
		public double valueAt(double membership1, double membership2) {
			return Math.min(membership1, membership2);
		}
	};
	
	private static final IBinaryFunction ZADEH_OR = new IBinaryFunction() {
		@Override
		public double valueAt(double membership1, double membership2) {
			return Math.max(membership1, membership2);
		}
	};

	public static IFuzzySet unaryOperation(IFuzzySet set, IUnaryFunction unaryFunction) {
		IDomain domain = set.getDomain();
		MutableFuzzySet resultSet = new MutableFuzzySet(domain);
		
		for (DomainElement element : domain) {
			resultSet.set(element, unaryFunction.valueAt(set.getValueAt(element)));
		}
		
		return resultSet;
	}

	public static IFuzzySet binaryOperation(IFuzzySet first, IFuzzySet second, IBinaryFunction binaryFunction) {
		IDomain domain = first.getDomain();
		MutableFuzzySet resultSet = new MutableFuzzySet(domain);
		
		for (DomainElement element : domain) {
			resultSet.set(element, binaryFunction.valueAt(first.getValueAt(element), second.getValueAt(element)));
		}
		
		return resultSet;
	}

	public static IUnaryFunction zadehNot() {
		return ZADEH_NOT;
	}

	public static IBinaryFunction zadehAnd() {
		return ZADEH_AND;
	}

	public static IBinaryFunction zadehOr() {
		return ZADEH_OR;
	}

	public static IBinaryFunction hamacherTNorm(double nu) {
		return new IBinaryFunction() {
			@Override
			public double valueAt(double membership1, double membership2) {
				double numerator = membership1 * membership2;
				double denominator = nu + (1 - nu) / (membership1 + membership2 - numerator);
				
				return numerator / denominator;
			}
		};
	}

	public static IBinaryFunction hamacherSNorm(double nu) {
		return new IBinaryFunction() {
			@Override
			public double valueAt(double membership1, double membership2) {
				double membershipProduct = membership1 * membership2;
				double numerator = membership1 + membership2 - (2 - nu) * membershipProduct;
				double denominator = 1 - (1 - nu) * membershipProduct;
				
				return numerator / denominator;
			}
		};
	}

}
