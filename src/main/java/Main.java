import service.CommandService;
import serviceImpl.CommandServiceImpl;

public class Main {
	public static void main(String[] args) throws Exception {
		CommandService cs=new CommandServiceImpl();
        cs.ini();
	}
}