package DaoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Dao.UserDao;
import entity.User;

public class UserDaoImpl implements UserDao{
	JDBConnect j = new JDBConnect();
	Connection con = j.connectSql();
	
	
	public User login(String username) throws Exception {
		System.out.println("我来啦~");
		long a=System.currentTimeMillis();
		User u= null;
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("select * from userinfo");
		// user 为你表的名称
		while (rs.next()) {
			String name = rs.getString("name");
			if (name.equals(username)) {
				String id = rs.getString("uid");
				//String name=rs.getString(name);
				String phoneNumber=rs.getString("phoneNumber");
				
				double money=rs.getDouble("money");
				
				u = new User();
				u.setID(id);
				u.setName(name);
				u.setPhoneNumber(phoneNumber);
				u.setMoney(money);
				

				break;
			}

		}
		/*for(int i=0;i<u.getCurrentPackageId().size();i++) {
			System.out.println(u.getCurrentPackageId().get(i));
		}*/
		
		System.out.println(u.getID());
		//时间
		System.out.println("执行登录耗时 : "+(System.currentTimeMillis()-a)/1000f+" 秒 ");
		return u;
	}
	
	public String register(String username, String phoneNumber, Double money) throws Exception {
		long a=System.currentTimeMillis();
		
		String result = "true";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("select * from userinfo");
		String errorinfo="";
		if (isSpecialChar(username)) {
			result = "invalid";// 含有特殊字符
			errorinfo="用户名中含有非法字符!";
			System.out.println("用户名中含有非法字符！");
			return errorinfo;
		}
		// user 为你表的名称
		while (rs.next()) {
			String name = rs.getString("name");
			if (name.equals(username)) {// 用户名重复
				result = "false";
				errorinfo="用户名已被注册";
				break;
			}
			String number=rs.getString("phoneNumber");
			if(number.equals(phoneNumber)) {//手机号已经被注册
				result="false";
				errorinfo="手机号已被注册";
				break;
			}

		}
		if (result.equals("false")) {
			System.out.println(errorinfo);
			return errorinfo;
		} else {
			// 得到现有user总数
			rs = stmt.executeQuery("select * from userinfo");
			int currentUserNum = 1;
			while (rs.next()) {
				currentUserNum++;
			}
			// 插入user
			String ID = "u" + currentUserNum;
			//System.out.println(ID);
			String sql2 = "insert into userinfo values(?,?,?,?)";
			PreparedStatement pst = con.prepareStatement(sql2);// 创建一个Statement对象
			pst.setString(1, ID);
			pst.setString(2, username);
			pst.setString(3, phoneNumber);
			pst.setDouble(4, money);
			pst.executeUpdate();
			System.out.println("注册成功！");
			//时间
			System.out.println("执行注册耗时 : "+(System.currentTimeMillis()-a)/1000f+" 秒 ");
			return ID;
		}
	}

	public static boolean isSpecialChar(String str) {
		String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.find();
	}
	
}
