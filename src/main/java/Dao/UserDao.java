package Dao;

import entity.User;

public interface UserDao {
	public String register(String username, String phoneNumber, Double money) throws Exception;
	public User login(String username) throws Exception;
}
