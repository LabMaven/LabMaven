package com.tide.ailab.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tide.ailab.model.Order;

/**
 * 订单原子操作
 * @author User
 *
 */
@Repository
public interface OrderDao {

	/**
	 * 查询订单列表
	 * @param order
	 * @return
	 */
	List<Order> getOrderList(Order order);
	
	/**
	 * 新增订单
	 * @param order
	 */
	void addOrder(Order order);
	
	/**
	 * 修改订单
	 * @param order
	 */
	void updateOrder(Order order);
}
