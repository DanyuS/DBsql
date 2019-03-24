package serviceImpl;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import Dao.PackageUseDao;
import Dao.PhonePackageDao;
import Dao.UserDao;
import DaoImpl.PackageUseDaoImpl;
import DaoImpl.PhonePackageDaoImpl;
import DaoImpl.UserDaoImpl;
import entity.User;
import service.CommandService;

public class CommandServiceImpl implements CommandService{

	PhonePackageDao phonePackageDao=new PhonePackageDaoImpl();
	PackageUseDao packageUseDao=new PackageUseDaoImpl();
	
	public void ini() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("��ӭ�������ʽӪҵ����");
        System.out.println("ע�����������1");
        System.out.println("��¼����������2");
        System.out.println("�˳�����������#");
        
        System.out.println("���ѡ����:"); 
        
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); 
        String str = null; 
        str = br.readLine(); 
        
        
        
        if(str.equals("1")||str.equals("2")) {
        	user(str);
        }else if(str.equals("#")) {
        	System.out.println("Byebye~");
        	System.out.println("---------------------------");
        	ini();
        }else {
        	System.out.println("���벻�Ϸ�������������");
        	ini();
        }
	}

	public void user(String state) throws Exception {
		if(state.equals("1")) {//register
			System.out.println("�������û�����");
			BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in)); 
	        String str1 = null; 
	        str1 = br1.readLine();
			System.out.println("������绰���룺");
			BufferedReader br2 = new BufferedReader(new InputStreamReader(System.in)); 
	        String str2 = null; 
	        str2 = br2.readLine();
	        System.out.println("�������");
			BufferedReader br3 = new BufferedReader(new InputStreamReader(System.in)); 
	        String str3 = null; 
	        str3 = br3.readLine();
	        UserDao userDao=new UserDaoImpl();
	    	userDao.register(str1, str2, Double.valueOf(str3));
	    	User user=userDao.login(str1);
	    	
	    	System.out.println("��ѯ�����ײͷ���������1");
	        System.out.println("���ķ���������2");
	        System.out.println("ʹ�÷���������3");
	        System.out.println("ʹ�÷���������4");
	        System.out.println("�������˵�������5");
	        System.out.println("��ѯ��ʷ������¼������6");
	        System.out.println("�˳�����������#");
	        
	        System.out.println("���ѡ����:"); 
	        BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); 
	        String str = null; 
	        str = br.readLine();
	        chooseService(str,user);
	    	
		}else if(state.equals("2")) {//login
			System.out.println("�������û�����");
	    	BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in)); 
	        String str1 = null; 
	        str1 = br1.readLine();
	    	UserDao userDao=new UserDaoImpl();
	    	User user=userDao.login(str1);
	    	
	    	System.out.println("��ѯ�����ײͷ���������1");
	        System.out.println("���ķ���������2");
	        System.out.println("�˶�����������3");
	        System.out.println("ʹ�÷���������4");
	        System.out.println("�������˵�������5");
	        System.out.println("��ѯ��ʷ������¼������6");
	        System.out.println("�˳�����������#");
	        
	        System.out.println("���ѡ����:"); 
	        BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); 
	        String str = null; 
	        str = br.readLine();
	        //System.out.println("aaaaaaa"+user.getID());
	        chooseService(str,user);
		}
	}

	public void chooseService(String choice,User user) throws Exception {
		// TODO Auto-generated method stub
		if(choice.equals("1")) {
			phonePackageDao.getAllPhonePackage(user);
			ini();
		}else if(choice.equals("2")) {
			phonePackageDao.choosePhonePackage(user);
			ini();
		}else if(choice.equals("3")) {
			phonePackageDao.unsubscribePhonePackage(user);
			ini();
		}else if(choice.equals("4")) {
			System.out.println("����绰������1");
	        System.out.println("���Ͷ���������2");
	        System.out.println("��������������3");
	        System.out.println("ȫ������������4");
	        System.out.println("�˳�����������#");
	        
	        System.out.println("���ѡ����:"); 
	        BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); 
	        String str = null; 
	        str = br.readLine();
	        useService(str,user);
		}else if(choice.equals("5")) {
			phonePackageDao.bill(user);
			ini();
		}else if(choice.equals("6")) {
			phonePackageDao.searchHistoryPackage(user);
			ini();
		}else if(choice.equals("#")) {
			ini();
		}
		
	}

	public void useService(String choice, User user) throws Exception {
		// TODO Auto-generated method stub
		if(choice.equals("1")) {
			packageUseDao.useCall(user);
			ini();
		}else if(choice.equals("2")) {
			packageUseDao.useMail(user);
			ini();
		}else if(choice.equals("3")) {
			packageUseDao.useLocalData(user);
			ini();
		}else if(choice.equals("4")) {
			packageUseDao.useDomainData(user);
			ini();
		}else if(choice.equals("#")) {
			ini();
		}
	}
	
	

}
