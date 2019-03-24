package DaoImpl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Dao.UserDao;
import service.CommandService;
import serviceImpl.CommandServiceImpl;

public class JDBConnect {
	public static void main(String[] args) throws SQLException {
    	
    }
	public Connection connectSql(){
    	//����Connection����
        Connection con = null;
        //����������
        String driver = "com.mysql.cj.jdbc.Driver";
        //URLָ��Ҫ���ʵ����ݿ���phonephone
        String url = "jdbc:mysql://localhost:3306/phonephone?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT";
        //MySQL����ʱ���û���
        String user = "root";
        //MySQL����ʱ������
        String password = "123456";
        //������ѯ�����
        try {
            //������������
            Class.forName(driver);
            //1.getConnection()����������MySQL���ݿ⣡��
            con = DriverManager.getConnection(url,user,password);
            if(!con.isClosed()) {
            	System.out.println("Succeeded connecting to the Database!");
            }
            
            
            /*
            
            
            //2.����statement���������ִ��SQL��䣡��
            Statement statement = con.createStatement();
          //Ҫִ�е�SQL���
            String sql = "select * from userInfo";
            //3.ResultSet�࣬������Ż�ȡ�Ľ��������
            ResultSet rs = statement.executeQuery(sql);
            System.out.println("-----------------");
            System.out.println("ִ�н��������ʾ:");  
            System.out.println("-----------------");  
            System.out.println("id" + "\t" + "����");  
            System.out.println("-----------------");  
             
            String id = null;
            String name = null;
            while(rs.next()){
                //��ȡstuname��������
                id = rs.getString("uid");
                //��ȡstuid��������
                name = rs.getString("name");

                //������
                System.out.println(id + "\t" + name);
            }
            rs.close();
            con.close();*/
        } catch(ClassNotFoundException e) {   
            //���ݿ��������쳣����
            System.out.println("Sorry,can`t find the Driver!");   
            e.printStackTrace();   
            } catch(SQLException e) {
            //���ݿ�����ʧ���쳣����
            e.printStackTrace();  
            }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally{
            System.out.println("���ݿ����ݳɹ���ȡ����");
        }
		return con;
      }
}
