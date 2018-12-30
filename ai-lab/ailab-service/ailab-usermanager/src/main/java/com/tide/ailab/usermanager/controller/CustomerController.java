package com.tide.ailab.usermanager.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.tide.ailab.common.log.Logger;
import com.tide.ailab.common.model.JsonResult;
import com.tide.ailab.common.model.JsonResultType;
import com.tide.ailab.common.model.Page;
import com.tide.ailab.common.model.SelectOption;
import com.tide.ailab.model.Customer;
import com.tide.ailab.usermanager.service.CustomerService;

import io.swagger.annotations.ApiOperation;

/**
 * 客户管理控制器
 * 
 * @author User
 *
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	/**
	 * 分页查询客户列表
	 * 
	 * @param page
	 * @param customer
	 * @return
	 */
	@ApiOperation(value = "查询客户信息", notes = "查询客户信息")
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String getCustomerList(Page page, Customer customer) {
		JsonResult result = new JsonResult(JsonResultType.SUCCESS);
		PageInfo<Customer> pageInfo = customerService.getCustomerListPage(customer, page);
		result.add("data", pageInfo);
		return result.toJSON();
	}

	/**
	 * 新增客户
	 * 
	 * @param customer
	 * @return
	 */
	@ApiOperation(value = "新增客户信息", notes = "新增客户信息")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String addCustomer(@RequestBody Customer customer) {
		Logger.d("begin addCustomer#name:" + customer.getName());
		JsonResult result = new JsonResult(JsonResultType.SUCCESS);
		customerService.addCustomer(customer);
		Logger.d("end addCustomer#name:" + customer.getName());
		return result.toJSON();
	}

	/**
	 * 修改客户
	 * 
	 * @param customer
	 * @return
	 */
	@ApiOperation(value = "修改客户信息", notes = "修改客户信息")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String updateCustomer(@RequestBody Customer customer) {
		Logger.d("begin updateCustomer#name:" + customer.getName());
		JsonResult result = new JsonResult(JsonResultType.SUCCESS);
		customerService.updateCustomer(customer);
		Logger.d("end updateCustomer#name:" + customer.getName());
		return result.toJSON();
	}

	/**
	 * 根据客户ID删除客户
	 * 
	 * @param customer
	 * @return
	 */
	@ApiOperation(value = "删除客户信息", notes = "删除客户信息")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String deleteCustomer(@RequestBody Customer customer) {
		Logger.d("begin deleteCustomer#id:" + customer.getId());
		JsonResult result = new JsonResult(JsonResultType.SUCCESS);
		customerService.deleteCustomer(customer.getId());
		Logger.d("end deleteCustomer#id:" + customer.getId());
		return result.toJSON();
	}

	@ApiOperation(value = "查询客户下拉列表", notes = "查询客户下拉列表")
	@RequestMapping(value = "/getCustomers", method = RequestMethod.GET)
	public String getCustomers() {
		JsonResult result = new JsonResult(JsonResultType.SUCCESS);

		Customer condition = new Customer();
		List<Customer> customerList = customerService.getCustomerList(condition);
		List<SelectOption> optionList = new ArrayList<SelectOption>();
		for (Customer customer : customerList) {
			SelectOption option = new SelectOption();
			option.setLabel(customer.getName());
			option.setValue(customer.getId());
			optionList.add(option);
		}
		result.add("data", optionList.toArray());
		return result.toJSON();
	}

}
