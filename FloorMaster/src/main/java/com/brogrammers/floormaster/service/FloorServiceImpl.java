/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.brogrammers.floormaster.service;

import com.brogrammers.floormaster.dao.FloorMasterPersistenceException;
import com.brogrammers.floormaster.dao.OrderDao;
import com.brogrammers.floormaster.dao.ProductDao;
import com.brogrammers.floormaster.dao.TaxDao;
import com.brogrammers.floormaster.dto.Orders;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author abdulrahman
 */

@Component
public class FloorServiceImpl implements FloorService{

    private OrderDao orderDao;
    private ProductDao productDao;
    private TaxDao taxDao;

    @Autowired
    public FloorServiceImpl(OrderDao orderDao, ProductDao productDao, TaxDao taxDao) {
        this.orderDao = orderDao;
        this.productDao = productDao;
        this.taxDao = taxDao;
    }
    
    
    @Override
    public void validateData(Orders order, LocalDate orderDate) throws 
            DataValidationException,
            FloorMasterPersistenceException {
        
        // make sure user doesn't input invalid fields
        if (order.getCustomerName() == null
                || order.getCustomerName().trim().length() == 0
                || order.getCustomerName().equals("")
                || this.containsIllegals(order.getCustomerName())
                || order.getState() == null 
                || order.getState().trim().length() == 0
                || order.getState().equals("")
                || !orderDate.isAfter(LocalDate.now())
                || taxDao.getState(order.getState()) == null
                || productDao.getProduct(order.getProductType()) == null
                || order.getArea().compareTo(new BigDecimal("100")) == -1) {
            throw new DataValidationException (
                    "ERROR: There was an error with your information!"
            );
        }
        
    }

    @Override
    public Orders createOrder(Orders order, LocalDate orderDate) throws 
            FloorMasterPersistenceException,
            DataValidationException, 
            InvalidDateException {
        
        this.validateData(order, orderDate);
        
        return orderDao.addOrder(order, orderDate);
        
    }

    @Override
    public List<Orders> getAllOrder(LocalDate date) throws 
            FloorMasterPersistenceException,
            InvalidOrderException {
        
        List<Orders> orderList = orderDao.getAllOrders(date);
        
        if (orderList == null) {
            throw new InvalidOrderException("No orders for this date!");
        }
        
        return orderList;
        
    }
    
    @Override
    public Orders getOrder(LocalDate date, int orderNumber) throws 
            FloorMasterPersistenceException,
            DataValidationException, 
            InvalidDateException, 
            InvalidOrderException {
        
        Orders order = null;
        
        try {
            
            order = orderDao.getOrder(date, orderNumber);
            
            if (order == null) {
            
                throw new InvalidOrderException("The requested order does not exist!");
            
            }
            
        } catch (FloorMasterPersistenceException e) {
            throw new FloorMasterPersistenceException("Failed to get order.");
        }
        
        
        return order;
    
    }

    @Override
    public Orders editOrder(int orderNumber, LocalDate date, Orders editedOrder) throws 
            FloorMasterPersistenceException,
            DataValidationException, 
            InvalidDateException, 
            InvalidOrderException {
        
        Orders order = null;
        
        try{
            
            order = this.getOrder(date, orderNumber);
            this.validateData(editedOrder, date);
            
        } catch (FloorMasterPersistenceException | DataValidationException e) {
            
            throw new FloorMasterPersistenceException("Failed to get order.");
            
        }
        
        if (order == null) {
            
            throw new InvalidOrderException("The requested order does not exist!");
            
        }
        
        order = orderDao.editOrder(date, orderNumber, editedOrder);
        
        return order;
    
    }

    @Override
    public Orders deleteOrder(LocalDate date, int orderNumber) throws 
            FloorMasterPersistenceException,
            DataValidationException, 
            InvalidDateException, 
            InvalidOrderException {
        
        
        Orders order = null;
        
        try{
            
            order = this.getOrder(date, orderNumber);
            
        } catch (FloorMasterPersistenceException e) {
            
            throw new FloorMasterPersistenceException("Failed to get order.");
            
        }
    
        if (order == null) {
            
            throw new InvalidOrderException("The requested order does not exist!");
            
        }
        
        return orderDao.removeOrder(date, orderNumber);
    }
    
    private boolean containsIllegals(String toExamine) {
        String[] arr = toExamine.split("[~#@*+%{}<>\\[\\]|\"\\_^]", 2);
        return arr.length > 1;
    }
    
    @Override
    public void calculateFields(Orders order) throws
            FloorMasterPersistenceException {
        
        order.setCostPerSquareFoot(productDao.getProduct(order.getProductType()).getCostPerSquareFoot());
        order.setLaborCostPerSquareFoot(productDao.getProduct(order.getProductType()).getLaborCostPerSquareFoot());
        order.setTaxRate(taxDao.getState(order.getState()).getTaxRate());
        
        order.setMaterialCost(order.getCostPerSquareFoot().multiply(order.getArea()).setScale(2, RoundingMode.HALF_UP));
        order.setLaborCost(order.getLaborCostPerSquareFoot().multiply(order.getArea()).setScale(2, RoundingMode.HALF_UP));
        BigDecimal subTotal = order.getLaborCost().add(order.getMaterialCost().setScale(2, RoundingMode.HALF_UP));
        order.setTax(order.getTaxRate().multiply(subTotal).setScale(2, RoundingMode.HALF_UP).divide(new BigDecimal("100")));
        
        order.setTotal(subTotal.add(order.getTax()));
        
    }
    
    @Override
    public void exportAllData() throws
            FloorMasterPersistenceException {
        orderDao.exportAllData();
    }
    
}
