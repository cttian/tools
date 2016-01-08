package poi;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class DBConfig {

	String cfgPath = "db.cfg";
	Properties properties = null;

	public DBConfig(String path) {
		if (path == null || path.isEmpty()) {
			System.out.println("配置文件路径错误！");
		}
		File cfg = new File(path + "\\conf\\" + cfgPath);
		if (!cfg.exists()) {
			System.out.println("指定path：" + path + "不存在");
		}
		properties = new Properties();
		try {
			properties.load(new FileReader(cfg));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("加载配置文件：" + path + "出错！");
		}
	}

	public String getVal(String key) {
		if (key == null || key.isEmpty())
			return null;
		if (properties != null) {
			if (properties.containsKey(key)) {
				return properties.getProperty(key);
			}
		}
		return null;
	}

}
