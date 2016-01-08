package analysis;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/***
 * 有限自动机
 * 
 * @author tiantian
 *
 */
public class FiniteAutomataSearch {

	public static Map<Character, Integer[]> buildFA(String pattern) {
		int length = pattern.length();
		int maxLength[] = new int[length];
		for (int i = 0, j = 1; i < length && j < length;) { // 调用KMP算法的预处理操作,构造状态转移函数需要
			if (pattern.charAt(i) == pattern.charAt(j)) {
				maxLength[j] = i + 1;
				j++;
				i++;
			} else if (i == 0) {
				j++;
			} else {
				i = maxLength[i - 1];
			}
		}
		System.out.println("模式串最大公共长度数组:" + Arrays.toString(maxLength));
		HashMap<Character, Integer[]> faTable = new HashMap<Character, Integer[]>(length);
		for (int i = 0; i < length; i++) {
			char c = pattern.charAt(i);
			if (!faTable.containsKey(c)) {
				Integer[] state = new Integer[length];
				for (int j = 0; j < length; j++) {
					int p = j;
					while (p != 0 && c != pattern.charAt(p)) {
						p = maxLength[p - 1];
					}
					if (c == pattern.charAt(p)) {
						state[j] = p + 1;
					} else {
						state[j] = 0;
					}
				}
				faTable.put(c, state);
			}
		}
		for (Entry<Character, Integer[]> entry : faTable.entrySet()) {
			System.out.format("%1$s = %2$s\n", entry.getKey(), Arrays.toString(entry.getValue()));
		}
		return faTable;
	}

	public static int faSearch(String text, String pattern) {
		if (text == null || pattern == null || text.isEmpty() || pattern.isEmpty()) {
			return -1;
		}
		Map<Character, Integer[]> faTable = buildFA(pattern);
		int state = 0;
		for (int i = 0; i < text.length(); i++) {
			Character c = text.charAt(i);
			Integer[] table = faTable.get(c);
			if (table == null) {
				state = 0;
				continue;
			}
			state = table[state];
			if (state == 7) {
				return i - pattern.length() + 1;
			}
		}
		return -1;
	}

	/***
	 * 对模式串和母串连续匹配成功的字符，将标记数组中当前位置j+1标记1 这个比朴素BF比较的次数还要多一般，而且还需要额外的空间
	 * 
	 * @param text
	 * @param pattern
	 * @return
	 */
	public static int bitap(String text, String pattern) {
		if (text == null || pattern == null || text.isEmpty() || pattern.isEmpty()
				|| text.length() < pattern.length()) {
			return -1;
		}
		int length = pattern.length();
		int[] map = new int[length + 1];
		map[0] = 1;
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			for (int j = length - 1; j >= 0; j--) {
				map[j + 1] = map[j] & (c == pattern.charAt(j) ? 1 : 0);
			}
			if (map[length] == 1) {
				return i - length + 1;
			}
		}
		return -1;
	}
	
	public static void main(String[] args) {
		String pattern = "ababaca";
		String text = "eababaca";
		System.out.println(faSearch(text, pattern));
		System.out.println(bitap(text, pattern));
	}

}
