package analysis;

public class KMPSearch {

	/****
	 * KMP,最长公共元素的
	 * 
	 * @param s
	 * @param p
	 * @return
	 */
	public static int indexOf(String s, String p) {
		if (s == null || p == null || s.isEmpty() || p.isEmpty())
			return -1;
		int sLength = s.length();
		int pLength = p.length();
		int[] maxLength = new int[pLength]; // 0到对应索引位置i的子串的最大公共长度
		int i = 0, j = 1;
		// 预处理其实也是一个字符串匹配
		while (j < pLength) {
			if (p.charAt(i) == p.charAt(j)) {
				maxLength[j] = i + 1;
				i++;
				j++;
			} else if (i == 0) {// 相当于隐藏执行maxLength[j] = 0;
				j++;
			} else {// abcabdabcabc 这样的就不能直接从头开始
				// 此时匹配字符数为i(i>0)个，遇到了不匹配的，最后一个匹配的索引是i-1
				// 应该找到0---(i-1)子字符串的最大前缀后缀长度,那不就是maxLength[i-1]么，也是下次对比的索引
				i = maxLength[i - 1];
			}
		}
		i = 0;
		j = 0;
		while (i < sLength && j < pLength) { // 比较一下的话，会发现，这里的处理逻辑和上边预处理数组的基本是一致的
			if (s.charAt(i) == p.charAt(j)) {
				if (j == pLength - 1) {
					return i - j;
				}
				i++;
				j++;
			} else if (j == 0) { // 模式串中第一个字符就没匹配,源字符串索引自动后移
				i++;
			} else {
				j = maxLength[j - 1];
				System.out.println(j);
			}
		}
		return -1;
	}

	/**
	 * 暴力匹配BF
	 * 
	 * @param s
	 *            源字符串
	 * @param p
	 *            模式字符串
	 * @return
	 */
	public int violentMatch(String s, String p) {
		if (s == null || p == null || s.isEmpty() || p.isEmpty())
			return -1;
		int sLength = s.length();
		int pLength = p.length();
		int i = 0, j = 0;
		int max = pLength - 1;
		while (i < sLength && j <= max) {
			if (s.charAt(i) == p.charAt(j)) {
				if (j == max) {
					return i - j;
				}
				i++;
				j++;
			} else {
				i = i - j + 1;
				j = 0;
			}
		}
		return -1;
	}

	public static void main(String[] args) {
		String s1 = "abdabeabdabd";
		String s2 = "abdabd";
		System.out.println(indexOf(s1, s2));
	}
}
