package com.tide.ailab.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tide.ailab.model.Customer;

/**
 * 客户原子操作
 * @author User
 */
@Repository
public interface CustomerDao {

    /**
     * 查询客户列表
     * @param condition
     * @return
     */
    List<Customer> getCustomerList(Customer condition);

    /**
     * 添加客户信息
     * @param customer
     * @return
     */
    int addCustomer(Customer customer);

    /**
     * 更新客户信息
     * @param customer
     * @return
     */
    int updateCustomer(Customer customer);

    /**
     * 删除客户信息
     * @param id
     * @return
     */
    int deleteCustomer(String id);

    /**
     * 判断满足条件的客户是否存在
     * @param customer
     * @return
     */
    int checkCustomerExist(Customer customer);
}
