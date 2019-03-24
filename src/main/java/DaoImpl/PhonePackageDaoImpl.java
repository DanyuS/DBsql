package DaoImpl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import Dao.PhonePackageDao;
import entity.PhonePackage;
import entity.UsageInfo;
import entity.User;

public class PhonePackageDaoImpl implements PhonePackageDao {
	JDBConnect j = new JDBConnect();
	Connection con = j.connectSql();

	public void getAllPhonePackage(User user) throws Exception {
		// 查询所有套餐服务
		long a = System.currentTimeMillis();
		Statement stmt = con.createStatement();
		System.out.println("pid" + "\t" + "type" + "\t" + "freeCallRange" + "\t" + "freeMailRange" + "\t"
				+ "freeLocalDataRange" + "\t" + "freeDomainDataRange" + "\t" + "basicPay");
		ResultSet rs = stmt.executeQuery("select * from packageservice");
		while (rs.next()) {
			String pid = rs.getString("pid");
			String type = rs.getString("type");
			int freeCallRange = rs.getInt("freeCallRange");
			int freeMailRange = rs.getInt("freeMailRange");
			double freeLocalDataRange = rs.getDouble("freeLocalDataRange");
			double freeDomainDataRange = rs.getDouble("freeDomainDataRange");
			double basicPay = rs.getDouble("basicPay");
			System.out.println(pid + "\t" + type + "\t" + freeCallRange + "\t" + freeMailRange + "\t"
					+ freeLocalDataRange + "\t" + freeDomainDataRange + "\t" + basicPay);

		}
		System.out.println("执行查询套餐耗时 : " + (System.currentTimeMillis() - a) / 1000f + " 秒 ");
	}

	public void searchAcceptedPhonePackage(User user) throws Exception {
		// TODO Auto-generated method stub
		List<String> acceptedPhonePackage = new ArrayList<String>();// 已接受套餐id
		String uid = user.getID();

		Timestamp currentTime = new Timestamp(System.currentTimeMillis());

		Statement stmt = con.createStatement();
		// 接受的套餐id
		ResultSet rs = stmt.executeQuery("select * from usageinfo where uid='" + uid + "'");
		while (rs.next()) {
			String ele = rs.getString("pid");
			Timestamp endTime = rs.getTimestamp("endTime");
			if (currentTime.before(endTime)) {
				acceptedPhonePackage.add(ele);
			}
		}

		System.out.println("以下套餐已经订阅：");
		System.out.println("pid" + "\t" + "type" + "\t" + "freeCallRange" + "\t" + "freeMailRange" + "\t"
				+ "freeLocalDataRange" + "\t" + "freeDomainDataRange" + "\t" + "basicPay");
		for (int i = 0; i < acceptedPhonePackage.size(); i++) {
			ResultSet rs2 = stmt
					.executeQuery("select * from packageservice where pid='" + acceptedPhonePackage.get(i) + "'");
			while (rs2.next()) {
				String pid = rs2.getString("pid");
				String type = rs2.getString("type");
				int freeCallRange = rs2.getInt("freeCallRange");
				int freeMailRange = rs2.getInt("freeMailRange");
				double freeLocalDataRange = rs2.getDouble("freeLocalDataRange");
				double freeDomainDataRange = rs2.getDouble("freeDomainDataRange");
				double basicPay = rs2.getDouble("basicPay");
				System.out.println(pid + "\t" + type + "\t" + freeCallRange + "\t" + freeMailRange + "\t"
						+ freeLocalDataRange + "\t" + freeDomainDataRange + "\t" + basicPay);

			}
		}
	}

	public void searchUnacceptedPhonePackage(User user) throws Exception {
		// TODO Auto-generated method stub
		List<String> phonePackage = new ArrayList<String>();// 全部套餐id
		List<String> acceptedPhonePackage = new ArrayList<String>();// 已接受套餐id
		List<String> unacceptedPhonePackage = new ArrayList<String>();// 未接受套餐id
		String uid = user.getID();

		Timestamp currentTime = new Timestamp(System.currentTimeMillis());

		Statement stmt = con.createStatement();
		// 接受的套餐id
		ResultSet rs = stmt.executeQuery("select * from usageinfo where uid='" + uid + "'");
		while (rs.next()) {
			String ele = rs.getString("pid");
			Timestamp endTime = rs.getTimestamp("endTime");
			if (currentTime.before(endTime)) {
				acceptedPhonePackage.add(ele);
			}

		}
		// 全部套餐id
		ResultSet rs1 = stmt.executeQuery("select * from packageservice");
		while (rs1.next()) {
			String ele = rs1.getString("pid");
			phonePackage.add(ele);
		}
		for (int i = 0; i < acceptedPhonePackage.size(); i++) {
			for (int j = 0; j < phonePackage.size(); j++) {
				if (phonePackage.get(j).equals(acceptedPhonePackage.get(i))) {
					phonePackage.remove(phonePackage.get(j));
				}
			}
		}
		for (int i = 0; i < phonePackage.size(); i++) {
			unacceptedPhonePackage.add(phonePackage.get(i));
		}
		for (int i = 0; i < unacceptedPhonePackage.size(); i++) {
			System.out.println(unacceptedPhonePackage.get(i));
		} // 得到未接受的套餐列表

		System.out.println("以下套餐可以选择：");
		System.out.println("pid" + "\t" + "type" + "\t" + "freeCallRange" + "\t" + "freeMailRange" + "\t"
				+ "freeLocalDataRange" + "\t" + "freeDomainDataRange" + "\t" + "basicPay");
		for (int i = 0; i < unacceptedPhonePackage.size(); i++) {
			ResultSet rs2 = stmt
					.executeQuery("select * from packageservice where pid='" + unacceptedPhonePackage.get(i) + "'");
			while (rs2.next()) {
				String pid = rs2.getString("pid");
				String type = rs2.getString("type");
				int freeCallRange = rs2.getInt("freeCallRange");
				int freeMailRange = rs2.getInt("freeMailRange");
				double freeLocalDataRange = rs2.getDouble("freeLocalDataRange");
				double freeDomainDataRange = rs2.getDouble("freeDomainDataRange");
				double basicPay = rs2.getDouble("basicPay");
				System.out.println(pid + "\t" + type + "\t" + freeCallRange + "\t" + freeMailRange + "\t"
						+ freeLocalDataRange + "\t" + freeDomainDataRange + "\t" + basicPay);

			}
		}
	}

	public void choosePhonePackage(User user) throws Exception {
		System.out.println("开启订阅服务");
		searchUnacceptedPhonePackage(user);
		System.out.println("请输入你想要选择的套餐编号：(PS:默认套餐结束时间为2018-12-31 23:59:59)");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String pid = null;
		pid = br.readLine();

		PhonePackage phonePackage = searchPhonePackage(pid);

		System.out.println("当即生效请按1次月生效请按2：");
		BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
		String str1 = null;
		str1 = br1.readLine();
		Timestamp startTime = null;

		if (str1.equals("1")) {
			startTime = new Timestamp(System.currentTimeMillis());
			System.out.println("添加套餐的时间是" + startTime);
		} else if (str1.equals("2")) {
			String timeStr = "2018-11-01 00:00:00";
			startTime = Timestamp.valueOf(timeStr);
		}

		String timeStr = "2018-12-31 23:59:59";
		Timestamp endTime = Timestamp.valueOf(timeStr);

		Statement stmt = con.createStatement();

		ResultSet rs1 = stmt.executeQuery("select * from usageinfo");
		int id = 0;
		while (rs1.next()) {
			id++;
		}

		ResultSet rs = stmt.executeQuery("select * from usageinfo");

		while (rs.next()) {
			// System.out.println(ID);
			String sql = "insert into usageinfo values(?,?,?,0,?,0,?,0,?,0,?,?,?,?)";
			PreparedStatement pst = con.prepareStatement(sql);// 创建一个Statement对象
			pst.setInt(1, id + 1);
			pst.setString(2, user.getID());
			pst.setString(3, pid);
			pst.setInt(4, phonePackage.getFreeCallRange());
			pst.setInt(5, phonePackage.getFreeMailRange());
			pst.setDouble(6, phonePackage.getFreeLocalDataRange());
			pst.setDouble(7, phonePackage.getFreeDomainDataRange());
			pst.setDouble(8, phonePackage.getBasicPay());
			pst.setTimestamp(9, startTime);
			pst.setTimestamp(10, endTime);
			pst.executeUpdate();
			System.out.println("添加成功！");
			break;
		}
	}

	public PhonePackage searchPhonePackage(String pid) throws Exception {
		// TODO Auto-generated method stub
		// System.out.println("我来找你啦");
		PhonePackage phonePackage = new PhonePackage();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("select * from packageservice");
		while (rs.next()) {
			String p = rs.getString("pid");
			if (p.equals(pid)) {
				String type = rs.getString("type");
				int freeCallRange = rs.getInt("freeCallRange");
				int freeMailRange = rs.getInt("freeMailRange");
				double freeLocalDataRange = rs.getDouble("freeLocalDataRange");
				double freeDomainDataRange = rs.getDouble("freeDomainDataRange");
				double basicPay = rs.getDouble("basicPay");

				phonePackage.setID(pid);
				phonePackage.setType(type);
				phonePackage.setFreeCallRange(freeCallRange);
				phonePackage.setFreeMailRange(freeMailRange);
				phonePackage.setFreeLocalDataRange(freeLocalDataRange);
				phonePackage.setFreeDomainDataRange(freeDomainDataRange);
				phonePackage.setBasicPay(basicPay);
			}
		}
		return phonePackage;
	}

	public void unsubscribePhonePackage(User user) throws Exception {
		// TODO Auto-generated method stub
		long a = System.currentTimeMillis();
		System.out.println("开启退订服务");
		searchAcceptedPhonePackage(user);
		System.out.println("请输入你想要退订的套餐编号：");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String pid = null;
		pid = br.readLine();

		PhonePackage phonePackage = searchPhonePackage(pid);

		System.out.println("当即生效请按1次月生效请按2：");
		BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
		String str1 = null;
		str1 = br1.readLine();
		Timestamp endTime = null;

		if (str1.equals("1")) {
			endTime = new Timestamp(System.currentTimeMillis());
			System.out.println("终止套餐的时间是" + endTime);
		} else if (str1.equals("2")) {
			String timeStr = "2018-11-01 00:00:00";
			endTime = Timestamp.valueOf(timeStr);
		}

		UsageInfo usageInfo = new UsageInfo();// 记录套餐退订时相关信息

		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("select * from usageinfo");

		while (rs.next()) {
			// System.out.println(ID);
			String sql = "update usageinfo set endTime = '" + endTime + "' where uid ='" + user.getID() + "' and pid='"
					+ pid + "'";
			PreparedStatement pst = con.prepareStatement(sql);// 创建一个Statement对象
			pst.executeUpdate();

			String uid = rs.getString("uid");
			// String pid=rs.getString("pid");
			int callUsage = rs.getInt("callUsage");
			int freeCallRemain = rs.getInt("freeCallRemain");
			int mailUsage = rs.getInt("mailUsage");
			int freeMailRemain = rs.getInt("freeMailRemain");
			double localDataUsage = rs.getDouble("localDataUsage");
			double freeLocalDataRemain = rs.getDouble("freeLocalDataRemain");
			double domainDataUsage = rs.getDouble("domainDataUsage");
			double freeDomainDataRemain = rs.getDouble("freeDomainDataRemain");
			double account = rs.getDouble("account");

			usageInfo.setUserID(uid);
			usageInfo.setPackageID(pid);
			usageInfo.setCallUsage(callUsage);
			usageInfo.setFreeCallRemain(freeCallRemain);
			usageInfo.setMailUsage(mailUsage);
			usageInfo.setFreeMailRemain(freeMailRemain);
			usageInfo.setLocalDataUsage(localDataUsage);
			usageInfo.setFreeLocalDataRemain(freeLocalDataRemain);
			usageInfo.setDomainDataUsage(domainDataUsage);
			usageInfo.setFreeDomainDataRemain(freeDomainDataRemain);
			usageInfo.setAccount(account);

			System.out.println("退订成功！");

		}
		System.out.println("执行退订耗时 : " + (System.currentTimeMillis() - a) / 1000f + " 秒 ");
		double money = calculateWhenUnsubscribe(user, usageInfo, phonePackage);

		long a1 = System.currentTimeMillis();
		ResultSet rs1 = stmt.executeQuery("select * from userinfo");
		while (rs1.next()) {
			String sql = "update userinfo set money = '" + money + "' where uid ='" + user.getID() + "'";
			PreparedStatement pst = con.prepareStatement(sql);// 创建一个Statement对象
			pst.executeUpdate();
			System.out.println("修改账户余额成功！");
		}
		System.out.println("执行修改账户余额耗时 : " + (System.currentTimeMillis() - a1) / 1000f + " 秒 ");

	}

	// 计算退订后的消费金额便于修改用户账户
	public double calculateWhenUnsubscribe(User user, UsageInfo usageInfo, PhonePackage phonePackage)
			throws SQLException {
		double sum = 0;

		int call = 0;
		int mail = 0;
		double localData = 0;
		double domainData = 0;

		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("select * from packageservice");
		while (rs.next()) {
			String pid = rs.getString("pid");
			if (pid.equals(usageInfo.getPackageID())) {
				call = rs.getInt("freeCallRange");
				mail = rs.getInt("freeMailRange");
				localData = rs.getDouble("freeLocalDataRange");
				domainData = rs.getDouble("freeDomainDataRange");
			}
		}

		if (usageInfo.getFreeCallRemain() == 0) {
			sum = sum + (usageInfo.getCallUsage() - phonePackage.getFreeCallRange()) * 0.5;
		}
		if (usageInfo.getFreeMailRemain() == 0) {
			sum = sum + (usageInfo.getMailUsage() - phonePackage.getFreeMailRange()) * 0.1;
		}
		if (usageInfo.getFreeLocalDataRemain() == 0) {
			sum = sum + (usageInfo.getLocalDataUsage() - phonePackage.getFreeLocalDataRange()) * 3;
		}
		if (usageInfo.getFreeDomainDataRemain() == 0) {
			sum = sum + (usageInfo.getDomainDataUsage() - phonePackage.getFreeDomainDataRange() * 5);
		}
		sum = sum + phonePackage.getBasicPay();
		System.out.println("你居然花了" + sum);
		return sum;
	}

	public void bill(User user) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("听说你要看账单");
		long a = System.currentTimeMillis();
		// 订阅的套餐，套餐使用情况，账户余额
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());

		List<String> l1 = new ArrayList<String>();

		Statement stmt = con.createStatement();
		System.out.println("uid" + "\t" + "pid" + "\t" + "callUsage" + "\t" + "freeCallRemain" + "\t" + "mailUsage"
				+ "\t" + "freeMailRemain" + "\t" + "localDataUsage" + "\t" + "freeLocalDataRemain" + "\t"
				+ "domainDataUsage" + "\t" + "freeDomainDataRemain" + "\t" + "account" + "\t" + "startTime" + "\t"
				+ "endTime");
		ResultSet rs = stmt.executeQuery("select * from usageinfo where uid='" + user.getID() + "'");
		while (rs.next()) {
			Timestamp startTime = rs.getTimestamp("startTime");
			Timestamp endTime = rs.getTimestamp("endTime");
			if (currentTime.after(startTime) && currentTime.before(endTime)) {
				String uid = rs.getString("uid");
				String pid = rs.getString("pid");
				int callUsage = rs.getInt("callUsage");
				int freeCallRemain = rs.getInt("freeCallRemain");
				int mailUsage = rs.getInt("mailUsage");
				int freeMailRemain = rs.getInt("freeMailRemain");
				double localDataUsage = rs.getDouble("localDataUsage");
				double freeLocalDataRemain = rs.getDouble("freeLocalDataRemain");
				double domainDataUsage = rs.getDouble("domainDataUsage");
				double freeDomainDataRemain = rs.getDouble("freeDomainDataRemain");
				double account = rs.getDouble("account");
				System.out.println(uid + "\t" + pid + "\t" + callUsage + "\t" + freeCallRemain + "\t" + mailUsage
						+ "\t" + freeMailRemain + "\t" + localDataUsage + "\t" + freeLocalDataRemain + "\t"
						+ domainDataUsage + "\t" + freeDomainDataRemain + "\t" + account + "\t" + startTime + "\t"
						+ endTime);
			}
		}

		ResultSet rs1 = stmt.executeQuery("select * from userinfo where uid='" + user.getID() + "'");
		while(rs1.next()) {
			double money=rs1.getDouble("money");
			System.out.println("账户余额为："+money);
		}
		
		System.out.println("执行生成月账单耗时 : " + (System.currentTimeMillis() - a) / 1000f + " 秒 ");
	}

	public void searchHistoryPackage(User user) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("听说你要看历史记录");
		long a = System.currentTimeMillis();
		
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());

		List<String> l1 = new ArrayList<String>();

		Statement stmt = con.createStatement();
		System.out.println("uid" + "\t" + "pid" + "\t"  + "account" + "\t" + "startTime" + "\t"
				+ "endTime");
		ResultSet rs = stmt.executeQuery("select * from usageinfo where uid='" + user.getID() + "'");
		while (rs.next()) {
			Timestamp startTime = rs.getTimestamp("startTime");
			Timestamp endTime = rs.getTimestamp("endTime");
			if (currentTime.after(startTime)) {
				String uid = rs.getString("uid");
				String pid = rs.getString("pid");
				
				double account = rs.getDouble("account");
				System.out.println(uid + "\t" + pid+ "\t" + account + "\t" + startTime + "\t"
						+ endTime);
			}
		}
		
		System.out.println("执行查询历史订购套餐耗时 : " + (System.currentTimeMillis() - a) / 1000f + " 秒 ");
		
	}

}
