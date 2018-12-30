package com.tide.ailab.usermanager.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tide.ailab.common.exception.DispatchException;
import com.tide.ailab.common.exception.DispatchExceptionCode;
import com.tide.ailab.common.model.Page;
import com.tide.ailab.common.util.GuidUtil;
import com.tide.ailab.common.util.StringUtil;
import com.tide.ailab.dao.CustomerDao;
import com.tide.ailab.model.Customer;

@Service
public class CustomerService {

	@Autowired
	private CustomerDao customerDao;

	/**
	 * 分页查询客户列表
	 * 
	 * @param condition
	 * @param page
	 * @return
	 */
	public PageInfo<Customer> getCustomerListPage(Customer condition, Page page) {
		PageHelper.startPage(page.getPageNum(), page.getPageSize());
		List<Customer> customerList = customerDao.getCustomerList(condition);
		return new PageInfo<Customer>(customerList);
	}
	
	/**
	 * 分页查询客户列表
	 * 
	 * @param condition
	 * @param page
	 * @return
	 */
	public List<Customer> getCustomerList(Customer condition) {
		List<Customer> customerList = customerDao.getCustomerList(condition);
		return customerList;
	}
	
	  /**
     * 新增客户
     * @param customer
     * @return
     */
    @Transactional
    public int addCustomer(Customer customer) {
        if (StringUtil.isNullOrEmpty(customer.getId())) {
            customer.setId(GuidUtil.getGuidWithoutHyphen());
        }

        // 该账户下已存在同名的客户，则抛出异常
        if (isCustomerExist(customer)) {
            throw new DispatchException(DispatchExceptionCode.ROLE_HAS_EXISTED);
        }

        return customerDao.addCustomer(customer);
    }

    /**
     * 修改客户
     * @param customer
     * @return
     */
    @Transactional
    public int updateCustomer(Customer customer) {
        // 该账户下已存在同名的客户，则抛出异常
        if (isCustomerExist(customer)) {
            throw new DispatchException(DispatchExceptionCode.ROLE_HAS_EXISTED);
        }

        return customerDao.updateCustomer(customer);
    }

    @Transactional
    public int deleteCustomer(String customerId) {
        return customerDao.deleteCustomer(customerId);
    }

    /**
     * 根据条件查询客户是否存在
     * @param customer
     * @return
     */
    private boolean isCustomerExist(Customer customer) {
        List<Customer> customerList = customerDao.getCustomerList(customer);
        if (CollectionUtils.isEmpty(customerList)) {
            return false;
        }

        return !customerList.get(0).getId().equals(customer.getId());
    }

}
