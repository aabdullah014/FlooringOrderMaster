/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.brogrammers.floormaster.dao;

import com.brogrammers.floormaster.dto.Orders;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Brian
 */
public interface OrderDao {
    List<Orders> getAllOrders(LocalDate date) throws FloorMasterPersistenceException;
    
    public Orders getOrder(LocalDate date, int orderNum) throws FloorMasterPersistenceException;
    
    Orders addOrder(Orders order, LocalDate orderDate) throws FloorMasterPersistenceException;
    
    Orders editOrder(LocalDate date, int orderNum, Orders editedOrder) throws FloorMasterPersistenceException;
    
    Orders removeOrder(LocalDate date, int orderNum) throws FloorMasterPersistenceException;
    
    void exportAllData() throws FloorMasterPersistenceException;
}
