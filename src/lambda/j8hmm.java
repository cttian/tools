package lambda;

import java.util.Arrays;
import java.util.List;

public class j8hmm {
	public static void main(String[] args) {
		Hmm hmm = new Hmm(new double[] { 0.5, 0.5 }, new double[][] { { 0.5, 0.5 }, { 0.5, 0.5 } },
				new double[][] { { 0.5, 0.5 }, { 0.5, 0.5 } });
		System.out.println(Forward.cal(hmm, Arrays.asList(0, 1)));
	}
}

class Hmm {
	public double pi[]; // 初始概率
	public double[][] a; // 隐藏状态(0,1)转换概率 
	public double[][] b; // 输出可能的都是0,1;输出概率

	public Hmm(double[] pi, double[][] a, double[][] b) {
		this.pi = pi;
		this.a = a;
		this.b = b;
	}

	public int stateSize() {
		return a.length;
	}

}

class Forward {

	public static double cal(final Hmm hmm, final List<Integer> o) {
		final Double[][] p = Arrays.asList(new Double[o.size()][]).stream().map(e -> {
			e = new Double[hmm.stateSize()];
			Arrays.fill(e, 0.);
			return e;
		}).toArray(Double[][]::new);
		for (int i = 0; i < hmm.stateSize(); i++) {
			p[0][i] = hmm.pi[i] * hmm.b[i][o.get(i)];
		}
		for (int i = 1; i < o.size(); i++) {
			for (int j = 0; j < hmm.stateSize(); j++) {
				for (int m = 0; m < hmm.stateSize(); m++) {
					p[i][j] += p[i - 1][m] * hmm.a[o.get(i - 1)][o.get(i)] * hmm.b[m][o.get(i)];
				}
			}
		}
		return Arrays.asList(p[o.size() - 1]).stream().mapToDouble(e -> e).sum();
	}
}
