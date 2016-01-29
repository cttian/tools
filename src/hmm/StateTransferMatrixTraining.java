package hmm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

/**
 * HMM状态转移概率矩阵提取
 * 
 * @author tiantian
 *
 */
public class StateTransferMatrixTraining {

	/**
	 * B-开头，E-结尾，M-中间，S-单字词
	 */
	String[] STATE = new String[] { "B", "E", "M", "S" };
	private static final HashMap<Character, Integer> stateIndex = new HashMap<Character, Integer>();
	private static final long[][] transferMatrix = new long[4][4];
	private static final long[] freq = new long[4];

	static {
		stateIndex.put('B', 0);
		stateIndex.put('E', 1);
		stateIndex.put('M', 2);
		stateIndex.put('S', 3);
	}
	
	public static void main(String[] args) {
		String path = "";
		readTrainingFile(path);
	}

	private static void readTrainingFile(String path) {
		BufferedReader reader = getReader(path);
		if (reader != null) {
			try {
				String line = reader.readLine();
				while (line != null) {
					termTraining(line);
				}
				double[][] cal = cal();
				for (int i = 0; i < cal.length; i++) {
					System.out.println(Arrays.toString(cal[i]));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static double[][] cal() {
		double[][] transfer = new double[4][4];
		int i, j;
		for (i = 0; i < 4; i++) {
			for (j = 0; j < 4; j++) {
				transfer[i][j] = (double) (transferMatrix[i][j] + 1) / freq[i];
			}
		}
		return transfer;
	}

	private static void termTraining(String line) {
		if (line == null || line.isEmpty())
			return;
		line = line.replaceAll("\\pP", "").trim();
		int start = 0, end = 0;
		int len = line.length();
		StringBuilder builder = new StringBuilder();
		while (end < len) {
			int codePointAt = Character.codePointAt(line, end);
			int charLen = Character.charCount(codePointAt);
			if (Character.isSpaceChar(codePointAt)) {
				if (end != start) {
					builder.append('E');
				} else {
					builder.append('S');
				}
				end += charLen;
				start = end;
			} else {
				if (end == start + 1) {
					builder.append('B');
					builder.append('M');
				} else if (end > start) {
					builder.append('M');
				}
				end++;
			}
		}
		initMatrix(builder.toString());
	}

	private static void initMatrix(String states) {
		int i = 0;
		int length = states.length();
		for (; i < length - 1; i++) {
			int x = stateIndex.get(states.charAt(i));
			int y = stateIndex.get(states.charAt(i + 1));
			transferMatrix[x][y]++;
			freq[x]++;
		}
		int index = stateIndex.get(states.charAt(length - 1));
		freq[index]++;
	}

	private static BufferedReader getReader(String path) {
		File file = new File(path);
		if (!file.exists() || !file.isFile()) {
			return null;
		}
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			return reader;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}
