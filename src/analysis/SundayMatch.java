package analysis;

import java.util.HashMap;

public class SundayMatch {

	public static int match(String source, String pattern) {
		if (source == null || source.isEmpty() || pattern == null || pattern.isEmpty())
			return -1;
		int sl = source.length();
		int pl = pattern.length();
		HashMap<Character, Integer> cache = new HashMap<Character, Integer>(pl);
		for (int i = pl - 1; i >= 0; i--) {
			char c = pattern.charAt(i);
			if (!cache.containsKey(c)) {
				cache.put(c, i);
			}
		}
		int i = 0, j = 0;
		while (i <= sl - pl) {
			int l = 0;
			j = 0;
			while (j < pl && source.charAt(i + l) == pattern.charAt(j)) {
				l++;
				j++;
			}
			if (j == pl)
				return i;
			char c = source.charAt(i + pl);
			Integer index = cache.get(c);
			if (index == null) {
				i += pl;
			} else {
				i += pl - index;
			}
		}
		return -1;
	}

	public static void main(String[] args) {
		String source = "我是好人";
		String pattern = "好人";
		System.out.println(match(source, pattern));
		
		source.indexOf(pattern);
	}
}
