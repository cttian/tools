package doubletrie;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
	public static void main(String[] args) throws IOException {
		List<String> words = new ArrayList<String>();
		words.add("一举");
		words.add("一举一动");
		words.add("一举成名");
		words.add("一举成名天下知");
		words.add("万能");
		words.add("万能胶");
		Set<Character> charset = new HashSet<Character>();
		// 这个字典如果要加入新词必须按字典序，参考下面的代码
		// Collections.sort(words);
		// BufferedWriter writer = new BufferedWriter(new
		// FileWriter("./data/sorted.dic", false));
		// for (String w : words)
		// {
		// writer.write(w);
		// writer.newLine();
		// }
		System.out.println("字典词条：" + words.size());

		{
			String infoCharsetValue = "";
			String infoCharsetCode = "";
			for (Character c : charset) {
				infoCharsetValue += c.charValue() + "    ";
				infoCharsetCode += (int) c.charValue() + " ";
			}
			infoCharsetValue += '\n';
			infoCharsetCode += '\n';
			System.out.print(infoCharsetValue);
			System.out.print(infoCharsetCode);
		}

		DoubleArrayTrie dat = new DoubleArrayTrie();
		System.out.println("是否错误: " + dat.build(words)); //words应该要求排过序的,否则构造可能不对
		List<Integer> integerList = dat.commonPrefixSearch("一举成名天下知");
		for (int index : integerList) {
			System.out.println(words.get(index));
		}
	}
}
