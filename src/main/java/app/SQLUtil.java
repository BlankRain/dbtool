package app;

import java.io.File;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class SQLUtil {
	static File root = new File("./sql");
	private static Map<String, String> sqlMap = new ConcurrentHashMap<String, String>();// thread
																						// safe

	static {
		File[] xmls = root.listFiles((x) -> {
			return x.getName().endsWith(".xml") && x.isFile();
		});
		for (File sqlFile : xmls) {
			parseSQL(sqlFile);
		}
	}

	private static void parseSQL(File sqlFile) {
		try {
			String xmlmsg = Files.readAllLines(sqlFile.toPath(), Charset.forName("utf8")).stream()
					.collect(Collectors.joining());
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new StringReader(xmlmsg));
			Element sqlroot = document.getRootElement();
			@SuppressWarnings("unchecked")
			List<Element> sqls = sqlroot.elements("sql");
			for (Element sql : sqls) {
				Element sqlName = sql.element("sqlName");
				Element sqlValue = sql.element("sqlValue");
				sqlMap.put(sqlName.getTextTrim(), sqlValue.getText());
				System.out.println("Init SQL Name  :\t" + sqlName.getTextTrim());
				System.out.println("Init SQL Value :\t" + sqlValue.getText());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getSql(String sqlName) {
		return sqlMap.get(sqlName);
	}
}
