package Dao;

import entity.User;

public interface PackageUseDao {
	public void useCall(User user) throws Exception;
	public void useMail(User user) throws Exception;
	public void useLocalData(User user) throws Exception;
	public void useDomainData(User user) throws Exception;
}
