/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brogrammers.floormaster.dao;

import com.brogrammers.floormaster.dto.Orders;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author EricNguyen
 */
public class OrderDaoFileImplTest {
    
    @Test
    public void testGetAllOrders() throws Exception {
        Orders order2 = new Orders();
        order2.setOrderNumber(2);
        order2.setCustomerName("Doctor Who");
        order2.setState("WA");
        order2.setTaxRate(new BigDecimal("9.25"));
        order2.setProductType("Wood");
        order2.setArea(new BigDecimal("243.00"));
        order2.setCostPerSquareFoot(new BigDecimal("5.15"));
        order2.setLaborCostPerSquareFoot(new BigDecimal("4.75"));
        order2.setMaterialCost(new BigDecimal("1251.45"));
        order2.setLaborCost(new BigDecimal("1154.25"));
        order2.setTax(new BigDecimal("216.51"));
        order2.setTotal(new BigDecimal("2622.21"));
        
        Orders order3 = new Orders();
        order3.setOrderNumber(3);
        order3.setCustomerName("Acme, Inc");
        order3.setState("KY");
        order3.setTaxRate(new BigDecimal("6.00"));
        order3.setProductType("Carpet");
        order3.setArea(new BigDecimal("217.00"));
        order3.setCostPerSquareFoot(new BigDecimal("2.25"));
        order3.setLaborCostPerSquareFoot(new BigDecimal("2.10"));
        order3.setMaterialCost(new BigDecimal("488.25"));
        order3.setLaborCost(new BigDecimal("455.70"));
        order3.setTax(new BigDecimal("56.64"));
        order3.setTotal(new BigDecimal("1000.59"));
        
        LocalDate date = LocalDate.of(2013, 6, 2);
        OrderDao instance = new OrderDaoFileImpl("TestFiles/", "");
        List<Orders> expResult = new ArrayList<>();
        expResult.add(order2);
        expResult.add(order3);
        List<Orders> result = instance.getAllOrders(date);
        
        assertNotNull("List must not be null", result);
        assertEquals("Should have Doctor Who and Acme Inc", expResult, result);
        assertEquals("Should have have 2", 2, result.size());
        assertTrue("Result list should have order 2", result.contains(order2));
        assertTrue("Result list should have order 3", result.contains(order3));
    }
    
    @Test
    public void testGetOrder() throws Exception {
        LocalDate date = LocalDate.of(2013, 6, 2);
        OrderDaoFileImpl instance = new OrderDaoFileImpl("TestFiles/", "");
        int orderNum = 2;
        
        Orders expResult = new Orders();
        expResult.setOrderNumber(2);
        expResult.setCustomerName("Doctor Who");
        expResult.setState("WA");
        expResult.setTaxRate(new BigDecimal("9.25"));
        expResult.setProductType("Wood");
        expResult.setArea(new BigDecimal("243.00"));
        expResult.setCostPerSquareFoot(new BigDecimal("5.15"));
        expResult.setLaborCostPerSquareFoot(new BigDecimal("4.75"));
        expResult.setMaterialCost(new BigDecimal("1251.45"));
        expResult.setLaborCost(new BigDecimal("1154.25"));
        expResult.setTax(new BigDecimal("216.51"));
        expResult.setTotal(new BigDecimal("2622.21"));
        
        Orders result = instance.getOrder(date, orderNum);
        assertEquals("Should be Doctor Who", expResult, result);
    }

    @Test
    public void testAddOrder() throws Exception {
        LocalDate orderDate = LocalDate.of(2022, 6, 30);
        OrderDaoFileImpl instance = new OrderDaoFileImpl("TestFiles/", "");
        
        Orders expResult = new Orders();
        expResult.setCustomerName("Corpo, Inc");
        expResult.setState("PA");
        expResult.setTaxRate(new BigDecimal("7.00"));
        expResult.setProductType("Tile");
        expResult.setArea(new BigDecimal("243.00"));
        expResult.setCostPerSquareFoot(new BigDecimal("2.00"));
        expResult.setLaborCostPerSquareFoot(new BigDecimal("2.10"));
        expResult.setMaterialCost(new BigDecimal("486.00"));
        expResult.setLaborCost(new BigDecimal("1020.60"));
        expResult.setTax(new BigDecimal("105.46"));
        expResult.setTotal(new BigDecimal("1612.06"));
        
        instance.addOrder(expResult, orderDate);
        Orders result = instance.getOrder(orderDate, expResult.getOrderNumber());
        
        assertEquals("Should be the same", expResult, result);
    }

    @Test
    public void testEditOrder() throws Exception {
        LocalDate date = LocalDate.of(2022, 6, 30);
        OrderDaoFileImpl instance = new OrderDaoFileImpl("TestFiles/", "");
        int orderNum = 6;
        
        Orders expResult = new Orders();
        expResult.setOrderNumber(6);
        expResult.setCustomerName("Test Dummy");
        expResult.setState("N/A");
        expResult.setTaxRate(new BigDecimal("0.00"));
        expResult.setProductType("Adamantium");
        expResult.setArea(new BigDecimal("1000.00"));
        expResult.setCostPerSquareFoot(new BigDecimal("100.00"));
        expResult.setLaborCostPerSquareFoot(new BigDecimal("200.00"));
        expResult.setMaterialCost(new BigDecimal("10000.00"));
        expResult.setLaborCost(new BigDecimal("20000.00"));
        expResult.setTax(new BigDecimal("1000.00"));
        expResult.setTotal(new BigDecimal("311000.00"));
        
        Orders result = instance.editOrder(date, orderNum, expResult);
        
        assertEquals(expResult, result);
    }

    @Test
    public void testRemoveOrder() throws Exception {
        LocalDate date = LocalDate.of(2022, 6, 29);
        OrderDaoFileImpl instance = new OrderDaoFileImpl("TestFiles/", "");
        int orderNum = 8;
        
        Orders expResult = new Orders();
        expResult.setOrderNumber(8);
        expResult.setCustomerName("Corpo, Inc");
        expResult.setState("PA");
        expResult.setTaxRate(new BigDecimal("7.00"));
        expResult.setProductType("Tile");
        expResult.setArea(new BigDecimal("243.00"));
        expResult.setCostPerSquareFoot(new BigDecimal("2.00"));
        expResult.setLaborCostPerSquareFoot(new BigDecimal("2.10"));
        expResult.setMaterialCost(new BigDecimal("486.00"));
        expResult.setLaborCost(new BigDecimal("1020.60"));
        expResult.setTax(new BigDecimal("105.46"));
        expResult.setTotal(new BigDecimal("1612.06"));
        
        Orders result = instance.removeOrder(date, orderNum);
        assertEquals(expResult, result);
        
        Orders failRemoved = instance.removeOrder(date, 100);
        assertNull("Should be null", failRemoved);
    }
}
