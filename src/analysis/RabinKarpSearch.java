package analysis;

public class RabinKarpSearch {

	/**
	 * 将pattern和text的子串转换为数字,这样就只比较一次就能知道两者是否相同。 这里没有考虑溢出情况
	 * double是64位的，小数位表示用64-1-11=53位表示，就有可能精度损失了,比较不准确
	 * 
	 * @param text
	 * @param pattern
	 * @return
	 */
	public static int simple(String text, String pattern) {
		if (text == null || pattern == null || text.isEmpty() || pattern.isEmpty()) {
			return -1;
		}
		int tLength = text.length();
		int pLength = pattern.length();
		if (pLength > tLength)
			return -1;
		double base = Math.pow(2, 16); // 这个相当于char集合的长度(所有可能的值的个数),像十进制的10,八进制的8
		double h = Math.pow(base, pLength - 1);
		double t = 0;
		double p = 0;
		for (int i = 0; i <= pLength - 1; i++) { // pattern和text的第一个子串转为数字
			t = pattern.charAt(i) + base * t;
			p = text.charAt(i) + base * p;
			// System.out.println(p);
		}
		for (int i = 0; i <= tLength - pLength; i++) {
			if (t == p) {
				return i;
			}
			if (i + pLength < tLength) // i=tLength - pLength
				p = text.charAt(i + pLength) + (p - h * text.charAt(i)) * base;
		}
		return -1;
	}

	/***
	 * 转换为数字的时候不使用浮点数,使用整形，为防止溢出，除以一个质数,因为这个取模运算，最后还要进行一次一一匹配；
	 * 使用整形应该无论何时都可以，因为计算过程中，超出int范围的时候，中间结果会暂时使用long保存，最后放入返回的结果中时取低位的，其他的舍弃；
	 * 这样的话就不存在精度的问题了。因为不涉及浮点数
	 * 
	 * @param text
	 * @param pattern
	 * @return
	 */
	public static int exact(String text, String pattern) {
		if (text == null || pattern == null || text.isEmpty() || pattern.isEmpty()) {
			return -1;
		}
		int tLength = text.length();
		int pLength = pattern.length();
		int q = 900000001;
		if (pLength > tLength)
			return -1;
		// double base = Math.pow(2, 15);
		// //这个相当于char集合的长度(所有可能的值的个数),像十进制的10,八进制的8;
		// 这样可以在后边的计算中区分不同位置的值，根据需要进行操作；是结果相同的必定相同；
		// 否则的话，base取1，如果pattern值为6,那么hash值相等的可能是1+5,2+4这些(acsii)
		int base = 1;
		for (int i = 0; i < 16; i++) {
			base *= 2;
		}
		int h = 1;
		for (int i = 1; i < pLength; i++) {
			h *= base;
		}
		int t = 0;
		int p = 0;
		for (int i = 0; i <= pLength - 1; i++) { // pattern和text的第一个子串转为数字
			t = (pattern.charAt(i) + base * t) % q;
			p = (text.charAt(i) + base * p) % q;
		}
		for (int i = 0; i <= tLength - pLength; i++) {
			if (t == p) {
				for (int j = i; j < i + pLength; j++) {
					if (pattern.charAt(j - i) != text.charAt(j)) {
						break;
					}
					if (j - i == pLength - 1) {
						return i;
					}
				}
			}
			if (i + pLength < tLength) // i=tLength - pLength
				p = (text.charAt(i + pLength) + (p - h * text.charAt(i)) * base) % q;
		}
		return -1;
	}

	public static void main(String[] args) {
		String pattern = "01234567890123456789012345678901234567890123456789";
		String text = "1012345678901234567890123456789012345678901234567890123456789";
		System.out.println(simple(text, pattern));
		System.out.println(exact(text, pattern));
		/*
		 * String test = "000111222"; for(char c : test.toCharArray()){
		 * System.out.println((int)c); }
		 * 
		 * 10^12 2^30
		 */
		System.out.println(Integer.toBinaryString(20014999));
	}
}
