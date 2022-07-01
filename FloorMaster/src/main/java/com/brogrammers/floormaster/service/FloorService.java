/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.brogrammers.floormaster.service;

import com.brogrammers.floormaster.dao.FloorMasterPersistenceException;
import com.brogrammers.floormaster.dto.Orders;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Brian
 */
public interface FloorService {
    
    void validateData(Orders order, LocalDate orderDate) throws 
            DataValidationException, 
            FloorMasterPersistenceException;
    
    Orders createOrder(Orders order, LocalDate orderDate) throws 
            FloorMasterPersistenceException,
            DataValidationException,
            InvalidDateException;
    
    List<Orders> getAllOrder(LocalDate date) throws
            FloorMasterPersistenceException,
            DataValidationException,
            InvalidDateException,
            InvalidOrderException;
    
    Orders getOrder(LocalDate date, int orderNumber) throws 
            FloorMasterPersistenceException,
            DataValidationException,
            InvalidDateException,
            InvalidOrderException;
    
    Orders editOrder(int orderNumber, LocalDate date, Orders editedOrder) throws 
            FloorMasterPersistenceException,
            DataValidationException,
            InvalidDateException,
            InvalidOrderException;
    
    Orders deleteOrder(LocalDate date, int orderNumber) throws 
            FloorMasterPersistenceException,
            DataValidationException,
            InvalidDateException,
            InvalidOrderException;
    
    void calculateFields(Orders order) throws
            FloorMasterPersistenceException ;
    
    void exportAllData() throws
            FloorMasterPersistenceException ;
}
