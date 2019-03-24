package Dao;

import entity.PhonePackage;
import entity.User;

public interface PhonePackageDao {
	public void getAllPhonePackage(User user) throws Exception;
	public void searchAcceptedPhonePackage(User user) throws Exception;
	public void searchUnacceptedPhonePackage(User user) throws Exception;
	public void choosePhonePackage(User user) throws Exception;
	public PhonePackage searchPhonePackage(String pid) throws Exception;
	public void unsubscribePhonePackage(User user) throws Exception;
	
	public void bill(User user) throws Exception;
	public void searchHistoryPackage(User user) throws Exception;
}
