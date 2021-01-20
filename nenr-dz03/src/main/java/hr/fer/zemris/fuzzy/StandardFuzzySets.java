package hr.fer.zemris.fuzzy;

public class StandardFuzzySets {
	
	public StandardFuzzySets() {}
	
	public static IIntUnaryFunction lFunction(int alpha, int beta) {
		return new IIntUnaryFunction() {
			@Override
			public double valueAt(int index) {
				if (index < alpha) return 1.;
				else if (index >= beta) return 0.;
				else return (beta - index) / (double)(beta - alpha);
			}
		};
	}
	
	public static IIntUnaryFunction gammaFunction(int alpha, int beta) {
		return new IIntUnaryFunction() {
			@Override
			public double valueAt(int index) {
				if (index < alpha) return 0.;
				else if (index >= beta) return 1.;
				else return (index - alpha) / (double)(beta - alpha);
			}
		};
	}
	
	public static IIntUnaryFunction lambdaFunction(int alpha, int beta, int gamma) {
		return new IIntUnaryFunction() {
			@Override
			public double valueAt(int index) {
				if (index < alpha) return 0.;
				else if (index >= alpha && index < beta) return (index - alpha) / (double)(beta - alpha);
				else if (index >= beta && index < gamma) return (gamma - index) / (double)(gamma - beta);
				else return 0.;
			}
		};
	}
	
}
