package DaoImpl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import Dao.PackageUseDao;
import Dao.PhonePackageDao;
import entity.User;

public class PackageUseDaoImpl implements PackageUseDao {

	PhonePackageDao phonePackageDao = new PhonePackageDaoImpl();

	JDBConnect j = new JDBConnect();
	Connection con = j.connectSql();

	public void useCall(User user) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("欢迎使用电话服务");
		long b = System.currentTimeMillis();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("select * from consumptiondetail");

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		String st = df.format(new Date(b));// new Date()为获取当前系统时间
		System.out.println("开始时间为" + st);

		// String et="";
		System.out.println("请输入持续时间");

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String total = null;
		total = br.readLine();
		double time = Double.valueOf(total);
		System.out.println("持续时间为" + time + "min");
		/*
		 * SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 * 
		 * Date d1 = (Date) df2.parse(et); Date d2 = (Date) df2.parse(st);
		 * 
		 * long diff = d1.getTime() - d2.getTime();//这样得到的差值是毫秒级别 long days = diff /
		 * (1000 * 60 * 60 * 24);
		 * 
		 * long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60); long minutes
		 * = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
		 * System.out.println(""+days+"天"+hours+"小时"+minutes+"分");
		 */

		long a = System.currentTimeMillis();
		int id = 0;
		while (rs.next()) {
			id++;
		}
		ResultSet rs1 = stmt.executeQuery("select * from consumptiondetail");
		while (rs1.next()) {
			String sql = "insert into consumptiondetail values(?,?,?,?,?)";
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, id + 1);
			pst.setString(2, user.getID());
			System.out.println("         " + user.getID());
			pst.setString(3, "通话");
			pst.setString(4, st);
			pst.setDouble(5, time);
			pst.executeUpdate();
			System.out.println("通话完成！");
			break;
		}
		System.out.println("执行插入电话记录耗时 : " + (System.currentTimeMillis() - a) / 1000f + " 秒 ");

		// 修改usage
		long c = System.currentTimeMillis();
		List<String> acceptedPhonePackage = searchAcceptedPackage(user);

		for (int k = 0; k < acceptedPhonePackage.size(); k++) {
			System.out.println("                  " + acceptedPhonePackage.get(k));
		}

		List<String> l1 = new ArrayList<String>();// 可以免费
		List<String> l2 = new ArrayList<String>();// 必须付费
		List<String> l3 = new ArrayList<String>();// 部分免费部分付费

		if (acceptedPhonePackage.size() != 0) {
			for (int i = 0; i < acceptedPhonePackage.size(); i++) {
				ResultSet rs2 = stmt.executeQuery("select * from usageinfo where pid='" + acceptedPhonePackage.get(i)
						+ "'and uid='" + user.getID() + "'");
				while (rs2.next()) {
					int freeCallRemain = rs2.getInt("freeCallRemain");
					if (freeCallRemain == 0) {
						l2.add(rs2.getString("pid"));
					} else if (freeCallRemain > Integer.parseInt(total)) {
						l1.add(rs2.getString("pid"));
					} else {
						l3.add(rs2.getString("pid"));
					}
				}
			}
		}

		if (l1.size() != 0) {
			for (int i = 0; i < l1.size(); i++) {
				ResultSet rs2 = stmt.executeQuery(
						"select * from usageinfo where pid='" + l1.get(i) + "'and uid='" + user.getID() + "'");
				while (rs2.next()) {
					int freeCallRemain = rs2.getInt("freeCallRemain");
					String sql = "update usageinfo set freeCallRemain = '" + (freeCallRemain - Integer.parseInt(total))
							+ "'where pid='" + l1.get(i) + "'and uid='" + user.getID() + "'";
					PreparedStatement pst = con.prepareStatement(sql);// 创建一个Statement对象
					pst.executeUpdate();
					System.out.println(acceptedPhonePackage.get(i));
					System.out.println((freeCallRemain - Integer.parseInt(total)));
					System.out.println("修改免费电话余额成功！");
				}
				break;
			}
		} else if (l3.size() != 0) {
			for (int i = 0; i < l3.size(); i++) {
				ResultSet rs2 = stmt.executeQuery(
						"select * from usageinfo where pid='" + l3.get(i) + "'and uid='" + user.getID() + "'");
				while (rs2.next()) {
					int freeCallRemain = rs2.getInt("freeCallRemain");
					int callUsage = rs2.getInt("callUsage");
					String sql = "update usageinfo set freeCallRemain = '" + 0 + "'where pid='" + l3.get(i)
							+ "'and uid='" + user.getID() + "'";
					PreparedStatement pst = con.prepareStatement(sql);// 创建一个Statement对象
					pst.executeUpdate();

					String sql1 = "update usageinfo set callUsage = '"
							+ (callUsage + Integer.parseInt(total) - freeCallRemain) + "'";
					PreparedStatement pst1 = con.prepareStatement(sql1);// 创建一个Statement对象
					pst1.executeUpdate();
					System.out.println("添加电话使用成功！");
				}
				break;
			}
		} else if (l2.size() != 0) {
			for (int i = 0; i < l2.size(); i++) {
				ResultSet rs2 = stmt.executeQuery(
						"select * from usageinfo where pid='" + l2.get(i) + "'and uid='" + user.getID() + "'");
				while (rs2.next()) {
					int callUsage = rs2.getInt("callUsage");
					String sql = "update usageinfo set callUsage = '" + (callUsage + Integer.parseInt(total))
							+ "'where pid='" + l2.get(i) + "'and uid='" + user.getID() + "'";
					PreparedStatement pst = con.prepareStatement(sql);// 创建一个Statement对象
					pst.executeUpdate();
					System.out.println("添加2电话使用成功！");
				}
				break;
			}
		}else {//没有任何套餐可以使用
			ResultSet rs2 = stmt.executeQuery("select * from userinfo where uid='"+user.getID()+"'");
			while(rs2.next()) {
				double money=rs2.getDouble("money");
				String sql = "update userinfo set money = '" + (money-Integer.parseInt(total)*0.5)+"'where uid='"+user.getID()+"'";
				PreparedStatement pst = con.prepareStatement(sql);// 创建一个Statement对象
				pst.executeUpdate();
			}
		}

		System.out.println("执行修改电话记录耗时 : " + (System.currentTimeMillis() - c) / 1000f + " 秒 ");
	}

	public List<String> searchAcceptedPackage(User user) throws SQLException {
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
		return acceptedPhonePackage;
	}

	public void useMail(User user) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("欢迎使用短信服务");
		long b = System.currentTimeMillis();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("select * from consumptiondetail");

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		String st = df.format(new Date(b));// new Date()为获取当前系统时间
		System.out.println("开始时间为" + st);

		// String et="";
		System.out.println("请输入发送短信数量");

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String total = null;
		total = br.readLine();
		double time = Double.valueOf(total);
		System.out.println("发送短信数量为为" + time + "条");

		long a = System.currentTimeMillis();
		int id = 0;
		while (rs.next()) {
			id++;
		}
		ResultSet rs1 = stmt.executeQuery("select * from consumptiondetail");
		while (rs1.next()) {
			String sql = "insert into consumptiondetail values(?,?,?,?,?)";
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, id + 1);
			pst.setString(2, user.getID());
			System.out.println("         " + user.getID());
			pst.setString(3, "短信");
			pst.setString(4, st);
			pst.setDouble(5, time);
			pst.executeUpdate();
			System.out.println("发送短信完成！");
			break;
		}
		System.out.println("执行插入短信记录耗时 : " + (System.currentTimeMillis() - a) / 1000f + " 秒 ");

		// 修改usage
		long c = System.currentTimeMillis();
		List<String> acceptedPhonePackage = searchAcceptedPackage(user);

		for (int k = 0; k < acceptedPhonePackage.size(); k++) {
			System.out.println("                  " + acceptedPhonePackage.get(k));
		}

		List<String> l1 = new ArrayList<String>();// 可以免费
		List<String> l2 = new ArrayList<String>();// 必须付费
		List<String> l3 = new ArrayList<String>();// 部分免费部分付费

		if (acceptedPhonePackage.size() != 0) {
			for (int i = 0; i < acceptedPhonePackage.size(); i++) {
				ResultSet rs2 = stmt.executeQuery("select * from usageinfo where pid='" + acceptedPhonePackage.get(i)
						+ "'and uid='" + user.getID() + "'");
				while (rs2.next()) {
					int freeMailRemain = rs2.getInt("freeMailRemain");
					if (freeMailRemain == 0) {
						l2.add(rs2.getString("pid"));
					} else if (freeMailRemain > Integer.parseInt(total)) {
						l1.add(rs2.getString("pid"));
					} else {
						l3.add(rs2.getString("pid"));
					}
				}
			}
		}

		if (l1.size() != 0) {
			for (int i = 0; i < l1.size(); i++) {
				ResultSet rs2 = stmt.executeQuery(
						"select * from usageinfo where pid='" + l1.get(i) + "'and uid='" + user.getID() + "'");
				while (rs2.next()) {
					int freeMailRemain = rs2.getInt("freeMailRemain");
					String sql = "update usageinfo set freeMailRemain = '" + (freeMailRemain - Integer.parseInt(total))
							+ "'where pid='" + l1.get(i) + "'and uid='" + user.getID() + "'";
					PreparedStatement pst = con.prepareStatement(sql);// 创建一个Statement对象
					pst.executeUpdate();
					System.out.println(acceptedPhonePackage.get(i));
					System.out.println((freeMailRemain - Integer.parseInt(total)));
					System.out.println("修改免费短信余额成功！");
				}
				break;
			}
		} else if (l3.size() != 0) {
			for (int i = 0; i < l3.size(); i++) {
				ResultSet rs2 = stmt.executeQuery(
						"select * from usageinfo where pid='" + l3.get(i) + "'and uid='" + user.getID() + "'");
				while (rs2.next()) {
					int freeMailRemain = rs2.getInt("freeMailRemain");
					int MailUsage = rs2.getInt("mailUsage");
					String sql = "update usageinfo set freeMailRemain = '" + 0 + "'where pid='" + l3.get(i)
							+ "'and uid='" + user.getID() + "'";
					PreparedStatement pst = con.prepareStatement(sql);// 创建一个Statement对象
					pst.executeUpdate();

					String sql1 = "update usageinfo set MailUsage = '"
							+ (MailUsage + Integer.parseInt(total) - freeMailRemain) + "'";
					PreparedStatement pst1 = con.prepareStatement(sql1);// 创建一个Statement对象
					pst1.executeUpdate();
					System.out.println("添加短信使用成功！");
				}
				break;
			}
		} else if (l2.size() != 0) {
			for (int i = 0; i < l2.size(); i++) {
				ResultSet rs2 = stmt.executeQuery(
						"select * from usageinfo where pid='" + l2.get(i) + "'and uid='" + user.getID() + "'");
				while (rs2.next()) {
					int MailUsage = rs2.getInt("mailUsage");
					String sql = "update usageinfo set MailUsage = '" + (MailUsage + Integer.parseInt(total))
							+ "'where pid='" + l2.get(i) + "'and uid='" + user.getID() + "'";
					PreparedStatement pst = con.prepareStatement(sql);// 创建一个Statement对象
					pst.executeUpdate();
					System.out.println("添加2短信使用成功！");
				}
				break;
			}
		}else {//没有任何套餐可以使用
			ResultSet rs2 = stmt.executeQuery("select * from userinfo where uid='"+user.getID()+"'");
			while(rs2.next()) {
				double money=rs2.getDouble("money");
				String sql = "update userinfo set money = '" + (money-Integer.parseInt(total)*0.1)+"'where uid='"+user.getID()+"'";
				PreparedStatement pst = con.prepareStatement(sql);// 创建一个Statement对象
				pst.executeUpdate();
			}
		}

		/*
		 * if(acceptedPhonePackage.size()!=0) { for(int
		 * i=0;i<acceptedPhonePackage.size();i++) { ResultSet rs2 =
		 * stmt.executeQuery("select * from usageinfo where pid='" +
		 * acceptedPhonePackage.get(i) + "'and uid='"+user.getID()+"'");
		 * while(rs2.next()) { int freeMailRemain=rs2.getInt("freeMailRemain"); int
		 * MailUsage=rs2.getInt("mailUsage"); if(freeMailRemain>Integer.parseInt(total))
		 * { String sql =
		 * "update usageinfo set freeMailRemain = '"+(freeMailRemain-Integer.parseInt(
		 * total))+"'where pid='" + acceptedPhonePackage.get(i) +
		 * "'and uid='"+user.getID()+"'"; PreparedStatement pst =
		 * con.prepareStatement(sql);// 创建一个Statement对象 pst.executeUpdate();
		 * System.out.println(acceptedPhonePackage.get(i));
		 * System.out.println((freeMailRemain-Integer.parseInt(total)));
		 * System.out.println("修改免费短信余额成功！"); break; }else if(freeMailRemain==0) {
		 * String sql =
		 * "update usageinfo set MailUsage = '"+(MailUsage+Integer.parseInt(total))
		 * +"'where pid='" + acceptedPhonePackage.get(i) +
		 * "'and uid='"+user.getID()+"'"; PreparedStatement pst =
		 * con.prepareStatement(sql);// 创建一个Statement对象 pst.executeUpdate();
		 * System.out.println("添加短信使用成功！"); break; }else { String sql =
		 * "update usageinfo set freeMailRemain = '"+0+"'where pid='" +
		 * acceptedPhonePackage.get(i) + "'and uid='"+user.getID()+"'";
		 * PreparedStatement pst = con.prepareStatement(sql);// 创建一个Statement对象
		 * pst.executeUpdate();
		 * 
		 * 
		 * String sql1 =
		 * "update usageinfo set MailUsage = '"+(MailUsage+Integer.parseInt(total)-
		 * freeMailRemain)+"'"; PreparedStatement pst1 = con.prepareStatement(sql1);//
		 * 创建一个Statement对象 pst1.executeUpdate(); System.out.println("添加短信使用成功！"); break;
		 * } } break; } }
		 */
		System.out.println("执行修改短信记录耗时 : " + (System.currentTimeMillis() - c) / 1000f + " 秒 ");
	}

	public void useLocalData(User user) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("欢迎使用当地流量服务");
		long b = System.currentTimeMillis();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("select * from consumptiondetail");

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		String st = df.format(new Date(b));// new Date()为获取当前系统时间
		System.out.println("开始时间为" + st);

		// String et="";
		System.out.println("请输入使用流量数量");

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String total = null;
		total = br.readLine();
		double time = Double.valueOf(total);
		System.out.println("使用流量数量为" + time + "M");

		long a = System.currentTimeMillis();
		int id = 0;
		while (rs.next()) {
			id++;
		}
		ResultSet rs1 = stmt.executeQuery("select * from consumptiondetail");
		while (rs1.next()) {
			String sql = "insert into consumptiondetail values(?,?,?,?,?)";
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, id + 1);
			pst.setString(2, user.getID());
			pst.setString(3, "当地流量");
			pst.setString(4, st);
			pst.setDouble(5, time);
			pst.executeUpdate();
			System.out.println("使用当地流量完成！");
			break;
		}
		System.out.println("执行插入当地流量记录耗时 : " + (System.currentTimeMillis() - a) / 1000f + " 秒 ");

		// 修改usage
		long c = System.currentTimeMillis();
		List<String> acceptedPhonePackage = searchAcceptedPackage(user);

		for (int k = 0; k < acceptedPhonePackage.size(); k++) {
			System.out.println("                  " + acceptedPhonePackage.get(k));
		}

		List<String> l1 = new ArrayList<String>();// 可以免费
		List<String> l2 = new ArrayList<String>();// 必须付费
		List<String> l3 = new ArrayList<String>();// 部分免费部分付费

		if (acceptedPhonePackage.size() != 0) {
			for (int i = 0; i < acceptedPhonePackage.size(); i++) {
				ResultSet rs2 = stmt.executeQuery("select * from usageinfo where pid='" + acceptedPhonePackage.get(i)
						+ "'and uid='" + user.getID() + "'");
				while (rs2.next()) {
					int freeLocalDataRemain = rs2.getInt("freeLocalDataRemain");
					if (freeLocalDataRemain == 0) {
						l2.add(rs2.getString("pid"));
					} else if (freeLocalDataRemain > Double.parseDouble(total)) {
						l1.add(rs2.getString("pid"));
					} else {
						l3.add(rs2.getString("pid"));
					}
				}
			}
		}

		if (l1.size() != 0) {
			for (int i = 0; i < l1.size(); i++) {
				ResultSet rs2 = stmt.executeQuery(
						"select * from usageinfo where pid='" + l1.get(i) + "'and uid='" + user.getID() + "'");
				while (rs2.next()) {
					int freeLocalDataRemain = rs2.getInt("freeLocalDataRemain");
					String sql = "update usageinfo set freeLocalDataRemain = '"
							+ (freeLocalDataRemain - Double.parseDouble(total)) + "'where pid='" + l1.get(i)
							+ "'and uid='" + user.getID() + "'";
					PreparedStatement pst = con.prepareStatement(sql);// 创建一个Statement对象
					pst.executeUpdate();
					System.out.println(acceptedPhonePackage.get(i));
					System.out.println((freeLocalDataRemain - Double.parseDouble(total)));
					System.out.println("修改免费当地流量余额成功！");
				}
				break;
			}
		} else if (l3.size() != 0) {
			for (int i = 0; i < l3.size(); i++) {
				ResultSet rs2 = stmt.executeQuery(
						"select * from usageinfo where pid='" + l3.get(i) + "'and uid='" + user.getID() + "'");
				while (rs2.next()) {
					int freeLocalDataRemain = rs2.getInt("freeLocalDataRemain");
					int LocalDataUsage = rs2.getInt("LocalDataUsage");
					String sql = "update usageinfo set freeLocalDataRemain = '" + 0 + "'where pid='" + l3.get(i)
							+ "'and uid='" + user.getID() + "'";
					PreparedStatement pst = con.prepareStatement(sql);// 创建一个Statement对象
					pst.executeUpdate();

					String sql1 = "update usageinfo set LocalDataUsage = '"
							+ (LocalDataUsage + Double.parseDouble(total) - freeLocalDataRemain) + "'";
					PreparedStatement pst1 = con.prepareStatement(sql1);// 创建一个Statement对象
					pst1.executeUpdate();
					System.out.println("添加当地流量使用成功！");
				}
				break;
			}
		} else if (l2.size() != 0) {
			for (int i = 0; i < l2.size(); i++) {
				ResultSet rs2 = stmt.executeQuery(
						"select * from usageinfo where pid='" + l2.get(i) + "'and uid='" + user.getID() + "'");
				while (rs2.next()) {
					int LocalDataUsage = rs2.getInt("LocalDataUsage");
					String sql = "update usageinfo set LocalDataUsage = '"
							+ (LocalDataUsage + Double.parseDouble(total)) + "'where pid='" + l2.get(i) + "'and uid='"
							+ user.getID() + "'";
					PreparedStatement pst = con.prepareStatement(sql);// 创建一个Statement对象
					pst.executeUpdate();
					System.out.println("添加2当地流量使用成功！");
				}
				break;
			}
		}else {//没有任何套餐可以使用
			ResultSet rs2 = stmt.executeQuery("select * from userinfo where uid='"+user.getID()+"'");
			while(rs2.next()) {
				double money=rs2.getDouble("money");
				String sql = "update userinfo set money = '" + (money-Double.parseDouble(total)*3)+"'where uid='"+user.getID()+"'";
				PreparedStatement pst = con.prepareStatement(sql);// 创建一个Statement对象
				pst.executeUpdate();
			}
		}

		System.out.println("执行修改当地流量记录耗时 : " + (System.currentTimeMillis() - c) / 1000f + " 秒 ");
	}

	public void useDomainData(User user) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("欢迎使用全国流量服务");
		long b = System.currentTimeMillis();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("select * from consumptiondetail");

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		String st = df.format(new Date(b));// new Date()为获取当前系统时间
		System.out.println("开始时间为" + st);

		// String et="";
		System.out.println("请输入使用流量数量");

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String total = null;
		total = br.readLine();
		double time = Double.valueOf(total);
		System.out.println("使用流量数量为" + time + "M");

		long a = System.currentTimeMillis();
		int id = 0;
		while (rs.next()) {
			id++;
		}
		ResultSet rs1 = stmt.executeQuery("select * from consumptiondetail");
		while (rs1.next()) {
			String sql = "insert into consumptiondetail values(?,?,?,?,?)";
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, id + 1);
			pst.setString(2, user.getID());
			pst.setString(3, "全国流量");
			pst.setString(4, st);
			pst.setDouble(5, time);
			pst.executeUpdate();
			System.out.println("使用全国流量完成！");
			break;
		}
		System.out.println("执行插入全国流量记录耗时 : " + (System.currentTimeMillis() - a) / 1000f + " 秒 ");

		// 修改usage
		long c = System.currentTimeMillis();
		List<String> acceptedPhonePackage = searchAcceptedPackage(user);

		for (int k = 0; k < acceptedPhonePackage.size(); k++) {
			System.out.println("                  " + acceptedPhonePackage.get(k));
		}

		List<String> l1 = new ArrayList<String>();// 可以免费
		List<String> l2 = new ArrayList<String>();// 必须付费
		List<String> l3 = new ArrayList<String>();// 部分免费部分付费

		if (acceptedPhonePackage.size() != 0) {
			for (int i = 0; i < acceptedPhonePackage.size(); i++) {
				ResultSet rs2 = stmt.executeQuery("select * from usageinfo where pid='" + acceptedPhonePackage.get(i)
						+ "'and uid='" + user.getID() + "'");
				while (rs2.next()) {
					int freeDomainDataRemain = rs2.getInt("freeDomainDataRemain");
					if (freeDomainDataRemain == 0) {
						l2.add(rs2.getString("pid"));
					} else if (freeDomainDataRemain > Double.parseDouble(total)) {
						l1.add(rs2.getString("pid"));
					} else {
						l3.add(rs2.getString("pid"));
					}
				}
			}
		}

		if (l1.size() != 0) {
			for (int i = 0; i < l1.size(); i++) {
				ResultSet rs2 = stmt.executeQuery(
						"select * from usageinfo where pid='" + l1.get(i) + "'and uid='" + user.getID() + "'");
				while (rs2.next()) {
					int freeDomainDataRemain = rs2.getInt("freeDomainDataRemain");
					String sql = "update usageinfo set freeDomainDataRemain = '"
							+ (freeDomainDataRemain - Double.parseDouble(total)) + "'where pid='" + l1.get(i)
							+ "'and uid='" + user.getID() + "'";
					PreparedStatement pst = con.prepareStatement(sql);// 创建一个Statement对象
					pst.executeUpdate();
					System.out.println(acceptedPhonePackage.get(i));
					System.out.println((freeDomainDataRemain - Double.parseDouble(total)));
					System.out.println("修改免费全国流量余额成功！");
				}
				break;
			}
		} else if (l3.size() != 0) {
			for (int i = 0; i < l3.size(); i++) {
				ResultSet rs2 = stmt.executeQuery(
						"select * from usageinfo where pid='" + l3.get(i) + "'and uid='" + user.getID() + "'");
				while (rs2.next()) {
					int freeDomainDataRemain = rs2.getInt("freeDomainDataRemain");
					int domainDataUsage = rs2.getInt("domainDataUsage");
					String sql = "update usageinfo set freeDomainDataRemain = '" + 0 + "'where pid='" + l3.get(i)
							+ "'and uid='" + user.getID() + "'";
					PreparedStatement pst = con.prepareStatement(sql);// 创建一个Statement对象
					pst.executeUpdate();

					String sql1 = "update usageinfo set domainDataUsage = '"
							+ (domainDataUsage + Double.parseDouble(total) - freeDomainDataRemain) + "'";
					PreparedStatement pst1 = con.prepareStatement(sql1);// 创建一个Statement对象
					pst1.executeUpdate();
					System.out.println("添加全国流量使用成功！");
				}
				break;
			}
		} else if (l2.size() != 0) {
			for (int i = 0; i < l2.size(); i++) {
				ResultSet rs2 = stmt.executeQuery(
						"select * from usageinfo where pid='" + l2.get(i) + "'and uid='" + user.getID() + "'");
				while (rs2.next()) {
					int domainDataUsage = rs2.getInt("domainDataUsage");
					String sql = "update usageinfo set domainDataUsage = '"
							+ (domainDataUsage + Double.parseDouble(total)) + "'where pid='" + l2.get(i) + "'and uid='"
							+ user.getID() + "'";
					PreparedStatement pst = con.prepareStatement(sql);// 创建一个Statement对象
					pst.executeUpdate();
					System.out.println("添加2全国流量使用成功！");
				}
				break;
			}
		}else {//没有任何套餐可以使用
			ResultSet rs2 = stmt.executeQuery("select * from userinfo where uid='"+user.getID()+"'");
			while(rs2.next()) {
				double money=rs2.getDouble("money");
				String sql = "update userinfo set money = '" + (money-Double.parseDouble(total)*5)+"'where uid='"+user.getID()+"'";
				PreparedStatement pst = con.prepareStatement(sql);// 创建一个Statement对象
				pst.executeUpdate();
			}
		}


		System.out.println("执行修改全国流量记录耗时 : " + (System.currentTimeMillis() - c) / 1000f + " 秒 ");
	}

}
