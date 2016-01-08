package analysis;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BMSearch {

	/**
	 * bad character 模式串中包含的字符,最后一次出现的位置j； 如果坏字符出现时，对应和模式串i的位置，那么移动的距离就是i-j
	 **/
	public static Map<Character, Integer> bmBc(char[] p) {
		int length = p.length;
		HashMap<Character, Integer> bmBc = new HashMap<>(length);
		for (int i = length - 1; i >= 0; i--) {
			if (!bmBc.containsKey(p[i])) {
				bmBc.put(p[i], i);
			}
		}
		return bmBc;
	}

	/***
	 * good suffix 这其实是在求最长公共长度,和KMP的类似,KMP的是从前往后，这里是从后往前
	 * 
	 * @param p
	 * @return
	 */
	public static int[] bmGs(char[] p) {
		int length = p.length;
		int[] suffix = new int[length]; //记录好后缀的长度，有些可能记录的是位置，类似的。
		int j = length - 1;
		for (int i = length - 2; i >= 0; i--) {
			if (p[i] == p[j]) {
				suffix[i] = suffix[i + 1] + 1;
				i--;
				j--;
			} else if (j == length - 1) {
				i--;
			} else {
				j = length - suffix[j + 1];
			}
		}

		int[] gs = new int[length];
		gs[length - 1] = length;
		Arrays.fill(gs, length);
		for (int i = length - 2; i >= 0; i--) { //这里其实可以和上边的合并的，不过合并以后理解的更困难了
			if (suffix[i] != 0) {
				int index = length - 1 - suffix[i]; //当前位置匹配的好后缀开始位置的索引，可以在上边直接求出，放在后缀素组，这里就不用计算了
				if (gs[index] == length) {
					gs[index] = index - i;
				}
			}
		}

		return gs;
	}

	/**
	 * BF倒序方法的改进,修改的位置是出现不匹配的时候,前进的步数
	 * 
	 * @param pattern
	 * @param text
	 * @return
	 */
	public static int suffixMatchBF(String pattern, String text) {
		char[] p = pattern.toCharArray();
		char[] t = text.toCharArray();
		int[] bmGs = null;
		Map<Character, Integer> bmBc = null;
		int i = 0;
		int length = p.length;
		int tLength = t.length;
		int maxLength = tLength - length;
		while (i <= maxLength && i >= 0) {
			int j = length - 1;
			for (; j >= 0 && p[j] == t[i + j]; j--)
				;
			if (j < 0) {
				return i;
			} else {
				if (bmBc == null || bmGs == null) {
					bmBc = bmBc(p);
					bmGs = bmGs(p);
				}
				Integer bc = bmBc.get(t[i + j]);
				if (bc != null) {
					bc = j - bc;// 只有<>0两种情况,不会出现=0
				} else {
					bc = j + 1;
				}
				int gs = bmGs[j - 1]; // 这个里边永远都是>0的
				i += Math.max(bc, gs); // 这里得到的永远>0
			}
		}
		return -1;
	}

	public static void main(String[] args) {
		String s =   "dabcd";
		String s2 = "dabdcda";
		System.out.println(suffixMatchBF(s, s2));
	}
}
