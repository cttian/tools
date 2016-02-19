package lambda;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class j8Viterbi {

	enum Hs {
		H1, H2, H3
	}

	enum Os {
		O1, O2
	}

	public static void main(String[] args) {

	}

	public void Vitervi(List<Os> o, double[] pi, HidenState<Hs, Os>[] hs, double[][] tran) {
		int hsL = Hs.values().length;
		final double[][] p = Arrays.asList(new double[hsL][]).stream().map(e -> {
			e = new double[o.size()];
			Arrays.fill(e, 0.);
			return e;
		}).toArray(double[][]::new);
		int[][] path = new int[o.size()][hsL];
		for (int i = 0; i < pi.length; i++) {
			p[i][0] = pi[i] * hs[i].out(o.get(0));
			path[0][i] = 0;
		}
		for (int i = 1; i < o.size(); i++) {
			for (int j = 0; j < hsL; j++) {
				for (int m = 0; m < hsL; m++) {
					if (p[i][j] < p[i - 1][m] * tran[m][j]) {
						p[i][j] = p[i - 1][m] * tran[m][j];
						path[i][j] = m;
					}
				}
				p[i][j] *= hs[j].out(o.get(i));
			}
		}
		int last[] = new int[o.size()];
		last[o.size() - 1] = 0;
		for (int i = 1; i < hsL; i++) {
			if (p[o.size()][last[o.size() - 1]] < p[o.size()][i]) {
				last[o.size() - 1] = i;
			}
		}
		for (int i = o.size() - 2; i >= 0; i--) {
			last[i] = path[i + 1][last[i + 1]];
		}
		System.out.println(last.toString());
	}

}

@FunctionalInterface
interface Convert<T> {
	default Optional<Integer> def(T o) {
		return null;
	}

	public Optional<Integer> convert(T o);
}

class HidenState<T, V> {

	T s;
	double[] outMatrix;
	Convert<V> convert;

	public HidenState(T s, double[] outMatrix) {
		this.s = s;
		this.outMatrix = outMatrix;
	}

	public double out(V o) {
		return this.outMatrix[convert.convert(o).get()];
	}
}
