package service;

import entity.User;

public interface CommandService {
	public void ini() throws Exception;
	public void user(String state) throws Exception;
	public void chooseService(String choice,User user) throws Exception;
	public void useService(String choice,User user) throws Exception;
	
}
