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
		System.out.println("������~");
		long a=System.currentTimeMillis();
		User u= null;
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("select * from userinfo");
		// user Ϊ��������
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
		//ʱ��
		System.out.println("ִ�е�¼��ʱ : "+(System.currentTimeMillis()-a)/1000f+" �� ");
		return u;
	}
	
	public String register(String username, String phoneNumber, Double money) throws Exception {
		long a=System.currentTimeMillis();
		
		String result = "true";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("select * from userinfo");
		String errorinfo="";
		if (isSpecialChar(username)) {
			result = "invalid";// ���������ַ�
			errorinfo="�û����к��зǷ��ַ�!";
			System.out.println("�û����к��зǷ��ַ���");
			return errorinfo;
		}
		// user Ϊ��������
		while (rs.next()) {
			String name = rs.getString("name");
			if (name.equals(username)) {// �û����ظ�
				result = "false";
				errorinfo="�û����ѱ�ע��";
				break;
			}
			String number=rs.getString("phoneNumber");
			if(number.equals(phoneNumber)) {//�ֻ����Ѿ���ע��
				result="false";
				errorinfo="�ֻ����ѱ�ע��";
				break;
			}

		}
		if (result.equals("false")) {
			System.out.println(errorinfo);
			return errorinfo;
		} else {
			// �õ�����user����
			rs = stmt.executeQuery("select * from userinfo");
			int currentUserNum = 1;
			while (rs.next()) {
				currentUserNum++;
			}
			// ����user
			String ID = "u" + currentUserNum;
			//System.out.println(ID);
			String sql2 = "insert into userinfo values(?,?,?,?)";
			PreparedStatement pst = con.prepareStatement(sql2);// ����һ��Statement����
			pst.setString(1, ID);
			pst.setString(2, username);
			pst.setString(3, phoneNumber);
			pst.setDouble(4, money);
			pst.executeUpdate();
			System.out.println("ע��ɹ���");
			//ʱ��
			System.out.println("ִ��ע���ʱ : "+(System.currentTimeMillis()-a)/1000f+" �� ");
			return ID;
		}
	}

	public static boolean isSpecialChar(String str) {
		String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~��@#��%����&*��������+|{}������������������������]|\n|\r|\t";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.find();
	}
	
}
