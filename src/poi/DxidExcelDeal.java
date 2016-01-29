package poi;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class DxidExcelDeal {

	class Node {
		public Set<Long> friends = new HashSet<Long>();
		public Long dxid;

		public Node(Long dxid) {
			super();
			this.dxid = dxid;
		}

		public void addFriend(Long friendid) {
			if (!friends.contains(friendid)) {
				friends.add(friendid);
			}
		}

		public Long[] sort() {
			Long[] array = friends.toArray(new Long[friends.size()]);
			for (int i = array.length - 1; i > 0; i--) {
				for (int j = 0; j < i; j++) {
					if (array[j] > array[j + 1]) {
						long temp = array[j];
						array[j] = array[j + 1];
						array[j + 1] = temp;
					}
				}
			}
			return array;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null)
				return false;
			if (obj instanceof DxidExcelDeal.Node) {
				Node n = (DxidExcelDeal.Node) obj;
				return n.dxid == this.dxid;
			}
			return false;
		}
	}

	public HashMap<Long, Node> addEachotherMap(List<String[]> raw) {
		return addEachotherMap(raw, "");
	}

	public HashMap<Long, Node> addEachotherMap(List<String[]> raw, String path) {
		if (raw == null || raw.size() == 0)
			return null;
		List<String> existed = new ArrayList<String>();
		HashMap<Long, Node> eachothermap = new HashMap<Long, Node>(raw.size() * 2);
		for (String[] row : raw) {
			if (row == null || row.length < 2) {
				existed.add((row == null || row.length == 0) ? "null" : row[0]);
				continue;
			}
			Long dxid = Long.parseLong(row[0]);
			Long dxid1 = Long.parseLong(row[1]);
			Node node = eachothermap.get(dxid);
			Node other = eachothermap.get(dxid1);

			Set<Long> exist = new HashSet<Long>();
			if (node != null && other != null)
				if (node.friends.equals(other.friends) && node.friends.contains(dxid)
						&& other.friends.contains(dxid1)) {
					existed.add(dxidFillHighWith0(dxid) + "\t" + dxidFillHighWith0(dxid1));
				}
			if (node != null) {
				Set<Long> f = node.friends;
				for (Long l : f) {
					eachothermap.get(l).friends = exist;
				}
				exist.addAll(f);
			}
			if (other != null) {
				Set<Long> f = other.friends;
				for (Long l : f) {
					eachothermap.get(l).friends = exist;
				}
				exist.addAll(f);
			}
			exist.add(dxid);
			exist.add(dxid1);
			if (node == null) {
				node = new Node(dxid);
				eachothermap.put(dxid, node);
				node.friends = exist;
			}

			if (other == null) {
				other = new Node(dxid1);
				eachothermap.put(dxid1, other);
				other.friends = exist;
			}
		}
		System.out.println("读取行数：" + raw.size());
		System.out.println("dxid总数：" + eachothermap.size());
		System.out.println("重复行数：" + existed.size());
		raw = null;
		if (!"".equals(path)) {
			this.WriteCata(existed, new File(path).getParent() + "\\repeat.log", this.new ListState());
		}
		existed = null;
		return eachothermap;
	}

	public static String dxidFillHighWith0(Long dxid){
		return String.format("%012d", dxid);
	}
	
	public static String dxidFillHighWith0(String dxid){
		return String.format("%012d", Long.parseLong(dxid));
	}
	
	public Map<Long, String> dealFriends(HashMap<Long, Node> net) {
		Set<Long> cache = new HashSet<Long>();
		List<Node> result = new ArrayList<Node>();
		for (Entry<Long, Node> entry : net.entrySet()) {
			Long key = entry.getKey();
			if (!cache.contains(key)) {
				Node value = entry.getValue();
				Set<Long> friends = value.friends;
				result.add(value);
				cache.addAll(friends);
			}
		}
		Map<Long, String> maxdxid = new HashMap<Long, String>(result.size());
		for (Node node : result) {
			Long[] sort = node.sort();
			StringBuffer buffer = new StringBuffer();
			Long max = sort[sort.length - 1];
			if (maxdxid.get(max) == null) {
				for (Long dxid : sort) {
					if (dxid != max) {
						if (buffer.length() > 0) {
							buffer.append(",");
						}
						buffer.append(dxid);
					}
				}
				if (sort.length >= 2) {
					maxdxid.put(max, buffer.toString());
				}
			}
		}
		return maxdxid;
	}

	public Map<Long, String> does(String path) {
		ExcelUtil re = new ExcelUtil();
		List<String[]> list = re.readExcel(path);
		if (list != null) {
			HashMap<Long, Node> net = addEachotherMap(list, path);
			return dealFriends(net);
		}
		return new HashMap<Long, String>(0);
	}

	public interface State<T> {
		void write(T cata, BufferedWriter writer);
	}

	class ListState implements State<List<String>> {

		@Override
		public void write(List<String> cata, BufferedWriter writer) {
			for (String other : cata) {
				try {
					writer.write(other + "\r\n");
				} catch (IOException e) {
					e.printStackTrace();
					continue;
				}
			}
		}
	}

	class TextState implements State<Map<Long, String>> {

		@Override
		public void write(Map<Long, String> cata, BufferedWriter writer) {
			for (Entry<Long, String> entry : cata.entrySet()) {
				String[] dxids = entry.getValue().split(",");
				Long max = entry.getKey();
				for (String other : dxids) {
					try {
						writer.write(dxidFillHighWith0(max) + "\t" + dxidFillHighWith0(other) + "\r\n");
					} catch (IOException e) {
						e.printStackTrace();
						continue;
					}
				}
			}
		}

	}

	public <T> void WriteCata(T cata, String filename, State<T> state) {
		BufferedWriter writer = null;
		try {
			writer = getWriter(filename);
			if (writer != null) {
				state.write(cata, writer);
				writer.flush();
				writer.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				writer = null;
			}
		}
	}

	public BufferedWriter getWriter(String filename) {
		File file = new File(filename);
		BufferedWriter writer = null;
		try {
			if (!file.exists()) {
				File parentsDir = new File(file.getParent());
				if (!parentsDir.exists()) {
					parentsDir.mkdirs();
				}
				file.createNewFile();
			}
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(filename)), "UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return writer;
	}

	public static void main(String[] args) {
		String path = "";
		String out = "";
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-path")) {
				path = args[++i] + "\\data";
				out = path + "\\logs.txt";
			}
		}
		System.out.println(path);
		System.out.println(out);
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (file.isDirectory()) {
			String[] list = file.list();
			if (list != null && list.length > 0) {
				file = new File(path, list[0]);
			} else {
				return;
			}
		}
		DxidExcelDeal deal = new DxidExcelDeal();
		Map<Long, String> result = deal.does(file.getAbsolutePath());
		deal.WriteCata(result, out, deal.new TextState());
	}

}
