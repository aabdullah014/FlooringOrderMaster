/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.brogrammers.floormaster.dao;

import com.brogrammers.floormaster.dto.Orders;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author abdulrahman
 */
public class FloorMasterOrderDaoStubImpl implements OrderDao {

    private Orders onlyOrder;
    private LocalDate onlyOrderDate = LocalDate.of(2023, 6, 30);
    private int orderNumb = 1;
    
    private List<Orders> orderList = new ArrayList<>();
    
    public FloorMasterOrderDaoStubImpl() {
        
        onlyOrder = new Orders("Acme, Inc.", "TX", "Cedar", new BigDecimal("100.0"));
        
    }
    
    public FloorMasterOrderDaoStubImpl(Orders testOrder) {
        
        this.onlyOrder = testOrder;
        
    }
    
    @Override
    public List<Orders> getAllOrders(LocalDate date) throws FloorMasterPersistenceException {
        
        return orderList;
        
    }

    @Override
    public Orders getOrder(LocalDate date, int orderNum) throws FloorMasterPersistenceException {
        
        Orders order = null;
        
        if(onlyOrderDate.equals(date) && orderNum == 1) {
            
            order = onlyOrder;
            orderList.remove(order);
            return order;
            
        }
        
        if(onlyOrderDate.equals(date) && orderNum == 2) {
            
            order = orderList.get(0);
            return order;
            
        }
        
        return null;
    }

    @Override
    public Orders addOrder(Orders order, LocalDate orderDate) throws FloorMasterPersistenceException {
        if(orderDate.equals(onlyOrderDate)) {
            
            orderList.add(order);
            return order;
            
        }
        
        return null;
    }

    @Override
    public Orders editOrder(LocalDate date, int orderNum, Orders editedOrder) throws FloorMasterPersistenceException {
        if(onlyOrderDate.equals(date) && orderNum == 1) {
            
            return onlyOrder;
            
        }
        
        if(onlyOrderDate.equals(date) && orderNum == 2) {
            
            Orders order = this.getOrder(date, orderNum);
            order.setCustomerName(editedOrder.getCustomerName());
            order.setState(editedOrder.getState());
            order.setProductType(editedOrder.getProductType());
            order.setArea(editedOrder.getArea());
            return order;
            
        }
        
        return null;
    }

    @Override
    public Orders removeOrder(LocalDate date, int orderNum) throws FloorMasterPersistenceException {
        
        if(onlyOrderDate.equals(date) && (orderNum == 1 || orderNum == 2)) {
            
            Orders order = this.getOrder(date, orderNum);
            orderList.remove(order);
            return order;
            
        }
        
        return null;
    }
    
    @Override
    public void exportAllData() throws FloorMasterPersistenceException {
    }
}
