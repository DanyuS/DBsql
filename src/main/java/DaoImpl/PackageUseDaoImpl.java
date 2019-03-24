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
		System.out.println("��ӭʹ�õ绰����");
		long b = System.currentTimeMillis();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("select * from consumptiondetail");

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// �������ڸ�ʽ
		String st = df.format(new Date(b));// new Date()Ϊ��ȡ��ǰϵͳʱ��
		System.out.println("��ʼʱ��Ϊ" + st);

		// String et="";
		System.out.println("���������ʱ��");

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String total = null;
		total = br.readLine();
		double time = Double.valueOf(total);
		System.out.println("����ʱ��Ϊ" + time + "min");
		/*
		 * SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 * 
		 * Date d1 = (Date) df2.parse(et); Date d2 = (Date) df2.parse(st);
		 * 
		 * long diff = d1.getTime() - d2.getTime();//�����õ��Ĳ�ֵ�Ǻ��뼶�� long days = diff /
		 * (1000 * 60 * 60 * 24);
		 * 
		 * long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60); long minutes
		 * = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
		 * System.out.println(""+days+"��"+hours+"Сʱ"+minutes+"��");
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
			pst.setString(3, "ͨ��");
			pst.setString(4, st);
			pst.setDouble(5, time);
			pst.executeUpdate();
			System.out.println("ͨ����ɣ�");
			break;
		}
		System.out.println("ִ�в���绰��¼��ʱ : " + (System.currentTimeMillis() - a) / 1000f + " �� ");

		// �޸�usage
		long c = System.currentTimeMillis();
		List<String> acceptedPhonePackage = searchAcceptedPackage(user);

		for (int k = 0; k < acceptedPhonePackage.size(); k++) {
			System.out.println("                  " + acceptedPhonePackage.get(k));
		}

		List<String> l1 = new ArrayList<String>();// �������
		List<String> l2 = new ArrayList<String>();// ���븶��
		List<String> l3 = new ArrayList<String>();// ������Ѳ��ָ���

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
					PreparedStatement pst = con.prepareStatement(sql);// ����һ��Statement����
					pst.executeUpdate();
					System.out.println(acceptedPhonePackage.get(i));
					System.out.println((freeCallRemain - Integer.parseInt(total)));
					System.out.println("�޸���ѵ绰���ɹ���");
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
					PreparedStatement pst = con.prepareStatement(sql);// ����һ��Statement����
					pst.executeUpdate();

					String sql1 = "update usageinfo set callUsage = '"
							+ (callUsage + Integer.parseInt(total) - freeCallRemain) + "'";
					PreparedStatement pst1 = con.prepareStatement(sql1);// ����һ��Statement����
					pst1.executeUpdate();
					System.out.println("��ӵ绰ʹ�óɹ���");
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
					PreparedStatement pst = con.prepareStatement(sql);// ����һ��Statement����
					pst.executeUpdate();
					System.out.println("���2�绰ʹ�óɹ���");
				}
				break;
			}
		}else {//û���κ��ײͿ���ʹ��
			ResultSet rs2 = stmt.executeQuery("select * from userinfo where uid='"+user.getID()+"'");
			while(rs2.next()) {
				double money=rs2.getDouble("money");
				String sql = "update userinfo set money = '" + (money-Integer.parseInt(total)*0.5)+"'where uid='"+user.getID()+"'";
				PreparedStatement pst = con.prepareStatement(sql);// ����һ��Statement����
				pst.executeUpdate();
			}
		}

		System.out.println("ִ���޸ĵ绰��¼��ʱ : " + (System.currentTimeMillis() - c) / 1000f + " �� ");
	}

	public List<String> searchAcceptedPackage(User user) throws SQLException {
		List<String> acceptedPhonePackage = new ArrayList<String>();// �ѽ����ײ�id
		String uid = user.getID();

		Timestamp currentTime = new Timestamp(System.currentTimeMillis());

		Statement stmt = con.createStatement();
		// ���ܵ��ײ�id
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
		System.out.println("��ӭʹ�ö��ŷ���");
		long b = System.currentTimeMillis();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("select * from consumptiondetail");

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// �������ڸ�ʽ
		String st = df.format(new Date(b));// new Date()Ϊ��ȡ��ǰϵͳʱ��
		System.out.println("��ʼʱ��Ϊ" + st);

		// String et="";
		System.out.println("�����뷢�Ͷ�������");

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String total = null;
		total = br.readLine();
		double time = Double.valueOf(total);
		System.out.println("���Ͷ�������ΪΪ" + time + "��");

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
			pst.setString(3, "����");
			pst.setString(4, st);
			pst.setDouble(5, time);
			pst.executeUpdate();
			System.out.println("���Ͷ�����ɣ�");
			break;
		}
		System.out.println("ִ�в�����ż�¼��ʱ : " + (System.currentTimeMillis() - a) / 1000f + " �� ");

		// �޸�usage
		long c = System.currentTimeMillis();
		List<String> acceptedPhonePackage = searchAcceptedPackage(user);

		for (int k = 0; k < acceptedPhonePackage.size(); k++) {
			System.out.println("                  " + acceptedPhonePackage.get(k));
		}

		List<String> l1 = new ArrayList<String>();// �������
		List<String> l2 = new ArrayList<String>();// ���븶��
		List<String> l3 = new ArrayList<String>();// ������Ѳ��ָ���

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
					PreparedStatement pst = con.prepareStatement(sql);// ����һ��Statement����
					pst.executeUpdate();
					System.out.println(acceptedPhonePackage.get(i));
					System.out.println((freeMailRemain - Integer.parseInt(total)));
					System.out.println("�޸���Ѷ������ɹ���");
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
					PreparedStatement pst = con.prepareStatement(sql);// ����һ��Statement����
					pst.executeUpdate();

					String sql1 = "update usageinfo set MailUsage = '"
							+ (MailUsage + Integer.parseInt(total) - freeMailRemain) + "'";
					PreparedStatement pst1 = con.prepareStatement(sql1);// ����һ��Statement����
					pst1.executeUpdate();
					System.out.println("��Ӷ���ʹ�óɹ���");
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
					PreparedStatement pst = con.prepareStatement(sql);// ����һ��Statement����
					pst.executeUpdate();
					System.out.println("���2����ʹ�óɹ���");
				}
				break;
			}
		}else {//û���κ��ײͿ���ʹ��
			ResultSet rs2 = stmt.executeQuery("select * from userinfo where uid='"+user.getID()+"'");
			while(rs2.next()) {
				double money=rs2.getDouble("money");
				String sql = "update userinfo set money = '" + (money-Integer.parseInt(total)*0.1)+"'where uid='"+user.getID()+"'";
				PreparedStatement pst = con.prepareStatement(sql);// ����һ��Statement����
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
		 * con.prepareStatement(sql);// ����һ��Statement���� pst.executeUpdate();
		 * System.out.println(acceptedPhonePackage.get(i));
		 * System.out.println((freeMailRemain-Integer.parseInt(total)));
		 * System.out.println("�޸���Ѷ������ɹ���"); break; }else if(freeMailRemain==0) {
		 * String sql =
		 * "update usageinfo set MailUsage = '"+(MailUsage+Integer.parseInt(total))
		 * +"'where pid='" + acceptedPhonePackage.get(i) +
		 * "'and uid='"+user.getID()+"'"; PreparedStatement pst =
		 * con.prepareStatement(sql);// ����һ��Statement���� pst.executeUpdate();
		 * System.out.println("��Ӷ���ʹ�óɹ���"); break; }else { String sql =
		 * "update usageinfo set freeMailRemain = '"+0+"'where pid='" +
		 * acceptedPhonePackage.get(i) + "'and uid='"+user.getID()+"'";
		 * PreparedStatement pst = con.prepareStatement(sql);// ����һ��Statement����
		 * pst.executeUpdate();
		 * 
		 * 
		 * String sql1 =
		 * "update usageinfo set MailUsage = '"+(MailUsage+Integer.parseInt(total)-
		 * freeMailRemain)+"'"; PreparedStatement pst1 = con.prepareStatement(sql1);//
		 * ����һ��Statement���� pst1.executeUpdate(); System.out.println("��Ӷ���ʹ�óɹ���"); break;
		 * } } break; } }
		 */
		System.out.println("ִ���޸Ķ��ż�¼��ʱ : " + (System.currentTimeMillis() - c) / 1000f + " �� ");
	}

	public void useLocalData(User user) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("��ӭʹ�õ�����������");
		long b = System.currentTimeMillis();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("select * from consumptiondetail");

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// �������ڸ�ʽ
		String st = df.format(new Date(b));// new Date()Ϊ��ȡ��ǰϵͳʱ��
		System.out.println("��ʼʱ��Ϊ" + st);

		// String et="";
		System.out.println("������ʹ����������");

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String total = null;
		total = br.readLine();
		double time = Double.valueOf(total);
		System.out.println("ʹ����������Ϊ" + time + "M");

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
			pst.setString(3, "��������");
			pst.setString(4, st);
			pst.setDouble(5, time);
			pst.executeUpdate();
			System.out.println("ʹ�õ���������ɣ�");
			break;
		}
		System.out.println("ִ�в��뵱��������¼��ʱ : " + (System.currentTimeMillis() - a) / 1000f + " �� ");

		// �޸�usage
		long c = System.currentTimeMillis();
		List<String> acceptedPhonePackage = searchAcceptedPackage(user);

		for (int k = 0; k < acceptedPhonePackage.size(); k++) {
			System.out.println("                  " + acceptedPhonePackage.get(k));
		}

		List<String> l1 = new ArrayList<String>();// �������
		List<String> l2 = new ArrayList<String>();// ���븶��
		List<String> l3 = new ArrayList<String>();// ������Ѳ��ָ���

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
					PreparedStatement pst = con.prepareStatement(sql);// ����һ��Statement����
					pst.executeUpdate();
					System.out.println(acceptedPhonePackage.get(i));
					System.out.println((freeLocalDataRemain - Double.parseDouble(total)));
					System.out.println("�޸���ѵ����������ɹ���");
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
					PreparedStatement pst = con.prepareStatement(sql);// ����һ��Statement����
					pst.executeUpdate();

					String sql1 = "update usageinfo set LocalDataUsage = '"
							+ (LocalDataUsage + Double.parseDouble(total) - freeLocalDataRemain) + "'";
					PreparedStatement pst1 = con.prepareStatement(sql1);// ����һ��Statement����
					pst1.executeUpdate();
					System.out.println("��ӵ�������ʹ�óɹ���");
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
					PreparedStatement pst = con.prepareStatement(sql);// ����һ��Statement����
					pst.executeUpdate();
					System.out.println("���2��������ʹ�óɹ���");
				}
				break;
			}
		}else {//û���κ��ײͿ���ʹ��
			ResultSet rs2 = stmt.executeQuery("select * from userinfo where uid='"+user.getID()+"'");
			while(rs2.next()) {
				double money=rs2.getDouble("money");
				String sql = "update userinfo set money = '" + (money-Double.parseDouble(total)*3)+"'where uid='"+user.getID()+"'";
				PreparedStatement pst = con.prepareStatement(sql);// ����һ��Statement����
				pst.executeUpdate();
			}
		}

		System.out.println("ִ���޸ĵ���������¼��ʱ : " + (System.currentTimeMillis() - c) / 1000f + " �� ");
	}

	public void useDomainData(User user) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("��ӭʹ��ȫ����������");
		long b = System.currentTimeMillis();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("select * from consumptiondetail");

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// �������ڸ�ʽ
		String st = df.format(new Date(b));// new Date()Ϊ��ȡ��ǰϵͳʱ��
		System.out.println("��ʼʱ��Ϊ" + st);

		// String et="";
		System.out.println("������ʹ����������");

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String total = null;
		total = br.readLine();
		double time = Double.valueOf(total);
		System.out.println("ʹ����������Ϊ" + time + "M");

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
			pst.setString(3, "ȫ������");
			pst.setString(4, st);
			pst.setDouble(5, time);
			pst.executeUpdate();
			System.out.println("ʹ��ȫ��������ɣ�");
			break;
		}
		System.out.println("ִ�в���ȫ��������¼��ʱ : " + (System.currentTimeMillis() - a) / 1000f + " �� ");

		// �޸�usage
		long c = System.currentTimeMillis();
		List<String> acceptedPhonePackage = searchAcceptedPackage(user);

		for (int k = 0; k < acceptedPhonePackage.size(); k++) {
			System.out.println("                  " + acceptedPhonePackage.get(k));
		}

		List<String> l1 = new ArrayList<String>();// �������
		List<String> l2 = new ArrayList<String>();// ���븶��
		List<String> l3 = new ArrayList<String>();// ������Ѳ��ָ���

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
					PreparedStatement pst = con.prepareStatement(sql);// ����һ��Statement����
					pst.executeUpdate();
					System.out.println(acceptedPhonePackage.get(i));
					System.out.println((freeDomainDataRemain - Double.parseDouble(total)));
					System.out.println("�޸����ȫ���������ɹ���");
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
					PreparedStatement pst = con.prepareStatement(sql);// ����һ��Statement����
					pst.executeUpdate();

					String sql1 = "update usageinfo set domainDataUsage = '"
							+ (domainDataUsage + Double.parseDouble(total) - freeDomainDataRemain) + "'";
					PreparedStatement pst1 = con.prepareStatement(sql1);// ����һ��Statement����
					pst1.executeUpdate();
					System.out.println("���ȫ������ʹ�óɹ���");
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
					PreparedStatement pst = con.prepareStatement(sql);// ����һ��Statement����
					pst.executeUpdate();
					System.out.println("���2ȫ������ʹ�óɹ���");
				}
				break;
			}
		}else {//û���κ��ײͿ���ʹ��
			ResultSet rs2 = stmt.executeQuery("select * from userinfo where uid='"+user.getID()+"'");
			while(rs2.next()) {
				double money=rs2.getDouble("money");
				String sql = "update userinfo set money = '" + (money-Double.parseDouble(total)*5)+"'where uid='"+user.getID()+"'";
				PreparedStatement pst = con.prepareStatement(sql);// ����һ��Statement����
				pst.executeUpdate();
			}
		}


		System.out.println("ִ���޸�ȫ��������¼��ʱ : " + (System.currentTimeMillis() - c) / 1000f + " �� ");
	}

}
