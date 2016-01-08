package poi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import poi.DxidExcelDeal.Node;

public class MysqlUtil {

	public static class DBHelper {
		public static String url = "jdbc:mysql://127.0.0.1/student";
		public static String classname = "com.mysql.jdbc.Driver";
		public static String user = "root";
		public static String password = "root";
		public static String sql = "select dxid,redata from dxids";

		public Connection conn = null;
		public PreparedStatement pst = null;

		public DBHelper(String path) {
			DBConfig config = new DBConfig(path);
			url = config.getVal("url");
			classname = config.getVal("classname");
			user = config.getVal("user");
			password = config.getVal("password");
			try {
				Class.forName(classname);// 指定连接类型
				conn = DriverManager.getConnection(url, user, password);// 获取连接
				pst = conn.prepareStatement("");// 准备执行语句
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public List<String[]> exe() {
			List<String[]> list = new ArrayList<>();
			try {
				ResultSet ret = pst.executeQuery();// 执行语句，得到结果集
				while (ret.next()) {
					String dxid1 = ret.getString(1);
					String dxid2 = ret.getString(2);
					list.add(new String[] { dxid1, dxid2 });
				} // 显示数据
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				close();
			}
			return list;
		}

		public void close() {
			try {
				this.conn.close();
				this.pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		String path = "";
		String out = "";
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-path")) {
				path = args[++i];
				out = path + "\\data\\logs.txt";
			}
		}
		System.out.println(path);
		System.out.println(out);
		DBHelper db1 = new DBHelper(path);// 创建DBHelper对象
		List<String[]> list = db1.exe();
		DxidExcelDeal deal = new DxidExcelDeal();
		HashMap<Long, Node> net = deal.addEachotherMap(list, path);
		Map<Long, String> result = deal.dealFriends(net);
		deal.WriteCata(result, out, deal.new TextState());
	}

}
