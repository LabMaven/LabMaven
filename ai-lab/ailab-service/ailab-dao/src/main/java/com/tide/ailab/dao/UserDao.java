package com.tide.ailab.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tide.ailab.model.User;

/**
 * 用户原子操作
 * 
 * @author User
 */
@Repository
public interface UserDao {

	void addUser(User user);

	User getUserInfo(User user);

	List<User> getUserList(User condition);

	/**
	 * 查询表中所有的用户名信息
	 * 
	 * @param
	 * @return List<String>
	 */
	List<String> getUserNameList();

	int deleteUser(User user);

	void deleteUserById(String id);

	void updateUser(User user);

	/**
	 * 查询用户是否存在
	 * 
	 * @param user
	 * @return
	 */
	int checkUserExist(User user);

}
