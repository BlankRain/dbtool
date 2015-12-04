package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
	/**
	 * 一个非常逗逼的jdbc代码
	 */

	public static void main(String[] args) {
		Doubee dou = parseArgs(args);
		if (dou == null) {
			System.out.println("Error and Exit!");
			return;
		}
		System.out.println(dou);
		Connection con = null;// 创建一个数据库连接
		PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
		ResultSet result = null;// 创建一个结果集对象
		try {

			System.out.println("开始尝试连接数据库！");
			String url = dou.getUrl();// 127.0.0.1是本机地址，XE是精简版Oracle的默认数据库名
			String user = dou.getUser();// 用户名,系统默认的账户名
			String password = dou.getPassword();// 你安装时选设置的密码
			con = DriverManager.getConnection(url, user, password);// 获取连接
			Statement stat = con.createStatement();
			if (dou.getSqlType().trim().toLowerCase().equals("q")) {

				ResultSet rSet = stat.executeQuery(dou.getSqlstr());
				ResultSetMetaData rData = rSet.getMetaData();
				List<Object> list = new ArrayList<Object>();
				while (rSet.next()) {
					Map<Object, Object> obj = new HashMap<Object, Object>();
					for (int i = 1; i <= rData.getColumnCount(); i++) {
						obj.put(rData.getColumnName(i).toLowerCase(), rSet.getObject(i));
					}
					list.add(obj);
				}
				rSet.close();
				System.out.println("查询后结果集有：" + list.size() + "条");
				list.stream().forEach(System.out::println);
			} else if (dou.getSqlType().trim().toLowerCase().equals("u")) {
				int boo = stat.executeUpdate(dou.getSqlstr());
				System.out.println("影响" + boo + "条");
			} else if (dou.getSqlType().trim().toLowerCase().equals("x")) {
				boolean boo = stat.execute(dou.getSqlstr());
				System.out.println("执行结果:" + boo);
			}
			System.out.println("---执行成功---！");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// 逐一将上面的几个对象关闭，因为不关闭的话会影响性能、并且占用资源
				// 注意关闭的顺序，最后使用的最先关闭
				if (result != null)
					result.close();
				if (pre != null)
					pre.close();
				if (con != null)
					con.close();
				System.out.println("数据库连接已关闭！");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static Doubee parseArgs(String[] args) {
		Doubee dou = new Doubee();
		int len = args.length;
		try {

			for (int i = 0; i < len; i++) {
				if (args[i].toLowerCase().equals("-u")) {
					dou.setUrl(args[i + 1]);
				} else if (args[i].toLowerCase().equals("-un")) {
					dou.setUser(args[i + 1]);
				} else if (args[i].toLowerCase().equals("-pa")) {
					dou.setPassword(args[i + 1]);
				} else if (args[i].toLowerCase().equals("-s")) {
					dou.setSqlstr(args[i + 1]);
				} else if (args[i].toLowerCase().equals("-t")) {
					dou.setSqlType(args[i + 1]);
				} else if (args[i].toLowerCase().equals("-sid")) {
					dou.setSid(args[i + 1]);
					String sqlstr = SQLUtil.getSql(dou.getSid());
					if(sqlstr!=null){
						dou.setSqlstr(sqlstr);
					}
				} else if (args[i].toLowerCase().equals("-h")) {
					System.out.println("-d to set driver");
					System.out.println("-u to set url");
					System.out.println("-un to set username");
					System.out.println("-pa to set password");
					System.out.println("-s to set sqlstr");
					System.out.println("-sid to set sqlid; sqlid in dir ./sql *.xml files ; it will rewrite sqlstr");
					System.out.println("-t to set sqltype {q:query ,u:update/delete/insert,x:exec}");
					System.out.println("-h for help");
					return null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return dou;
	}

}

/**
 * 
 * database config
 */
class Doubee {
	String url = "notseturl";
	String user = "notsetuser";
	String password = "notsetpassword";
	String sqlstr = "notsetsql";
	String sqlType = "q";
	String sid = "";

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public Doubee() {
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSqlstr() {
		return sqlstr;
	}

	public void setSqlstr(String sqlstr) {
		this.sqlstr = sqlstr;
	}

	public String getSqlType() {
		return sqlType;
	}

	public void setSqlType(String sqlType) {
		this.sqlType = sqlType;
	}

	@Override
	public String toString() {
		return String.format("Doubee config: \nURL:\t%s \nUser:\t%s\nPassWord:\t%s\nSqlStr:\t%s\nSqlType:\t%s", url,
				user, password, sqlstr, sqlType);
	}
}
