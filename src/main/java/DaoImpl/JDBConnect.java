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
    	//声明Connection对象
        Connection con = null;
        //驱动程序名
        String driver = "com.mysql.cj.jdbc.Driver";
        //URL指向要访问的数据库名phonephone
        String url = "jdbc:mysql://localhost:3306/phonephone?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT";
        //MySQL配置时的用户名
        String user = "root";
        //MySQL配置时的密码
        String password = "123456";
        //遍历查询结果集
        try {
            //加载驱动程序
            Class.forName(driver);
            //1.getConnection()方法，连接MySQL数据库！！
            con = DriverManager.getConnection(url,user,password);
            if(!con.isClosed()) {
            	System.out.println("Succeeded connecting to the Database!");
            }
            
            
            /*
            
            
            //2.创建statement类对象，用来执行SQL语句！！
            Statement statement = con.createStatement();
          //要执行的SQL语句
            String sql = "select * from userInfo";
            //3.ResultSet类，用来存放获取的结果集！！
            ResultSet rs = statement.executeQuery(sql);
            System.out.println("-----------------");
            System.out.println("执行结果如下所示:");  
            System.out.println("-----------------");  
            System.out.println("id" + "\t" + "姓名");  
            System.out.println("-----------------");  
             
            String id = null;
            String name = null;
            while(rs.next()){
                //获取stuname这列数据
                id = rs.getString("uid");
                //获取stuid这列数据
                name = rs.getString("name");

                //输出结果
                System.out.println(id + "\t" + name);
            }
            rs.close();
            con.close();*/
        } catch(ClassNotFoundException e) {   
            //数据库驱动类异常处理
            System.out.println("Sorry,can`t find the Driver!");   
            e.printStackTrace();   
            } catch(SQLException e) {
            //数据库连接失败异常处理
            e.printStackTrace();  
            }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally{
            System.out.println("数据库数据成功获取！！");
        }
		return con;
      }
}
