/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.brogrammers.floormaster.service;

import com.brogrammers.floormaster.dao.FloorMasterOrderDaoStubImpl;
import com.brogrammers.floormaster.dao.FloorMasterPersistenceException;
import com.brogrammers.floormaster.dao.FloorMasterProductDaoStubImpl;
import com.brogrammers.floormaster.dao.FloorMasterTaxDaoStubImpl;
import com.brogrammers.floormaster.dao.OrderDao;
import com.brogrammers.floormaster.dao.ProductDao;
import com.brogrammers.floormaster.dao.TaxDao;
import com.brogrammers.floormaster.dto.Orders;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author abdulrahman
 */
public class FloorServiceImplTest {
    
    private FloorService testService;
    
    public FloorServiceImplTest() {
        
        OrderDao orderDao = new FloorMasterOrderDaoStubImpl();
        ProductDao productDao = new FloorMasterProductDaoStubImpl();
        TaxDao taxDao = new FloorMasterTaxDaoStubImpl();
        
        this.testService = new FloorServiceImpl(orderDao, productDao, taxDao);
        
    }
    
    @Test
    public void testCreateValidOrder() throws Exception {
        
        Orders testOrder = new Orders("John", "TX", "Cedar", new BigDecimal("100"));
        testOrder.setOrderNumber(2);
        
        testService.createOrder(testOrder, LocalDate.of(2023, 6, 30));
        testService.calculateFields(testOrder);
        
        assertEquals("Material cost must be 150", testOrder.getMaterialCost(), new BigDecimal("150.00"));
        assertEquals("Labor cost must be 175", testOrder.getLaborCost(), new BigDecimal("175.00"));
        assertEquals("Tax", testOrder.getTaxRate(), new BigDecimal("7"));
        assertEquals("Tax should be 22.75", testOrder.getTax(), new BigDecimal("22.75"));
        assertEquals("Total should be 347.75", testOrder.getTotal(), new BigDecimal("347.75"));
    }
    
    @Test
    public void testCreateInvalidOrder() throws Exception {
        Orders testOrder = new Orders("John", "AZ", "Cedar", new BigDecimal("100"));
        testOrder.setOrderNumber(2);
        
        //ACT
        try {
            
            testService.createOrder(testOrder, LocalDate.of(2023, 6, 30));
            fail("Something went wrong. That should not have worked.");
            
        } catch ( FloorMasterPersistenceException e ) {
            
            fail("Something went wrong. That's the wrong exception.");
            
        } catch ( DataValidationException e ) {
            
            return;
            
        }
    }
    
    @Test
    public void testGetAllOrders() throws Exception {
        // ARRANGE
        Orders testOrder = new Orders("John", "TX", "Cedar", new BigDecimal("100"));
        testOrder.setOrderNumber(2);
        
        testService.createOrder(testOrder, LocalDate.of(2023, 6, 30));
        
        // ACT & ASSERT
        assertEquals("Order stored under order 2 should be John.", 1, testService.getAllOrder(LocalDate.of(2023, 6, 30)).size());
        assertTrue("The one order should be John", testService.getAllOrder(LocalDate.of(2023, 6, 30)).contains(testOrder));
        
    }
    
    @Test
    public void testGetOrder() throws Exception {
        // ARRANGE
        Orders testOrder = new Orders("John", "TX", "Cedar", new BigDecimal("100"));
        testOrder.setOrderNumber(2);
        
        testService.createOrder(testOrder, LocalDate.of(2023, 6, 30));
        
        // ACT & ASSERT
        Orders shouldBeJohn = testService.getOrder(LocalDate.of(2023, 6, 30), 2);
        assertNotNull("Getting order 2 should not be null.", shouldBeJohn);
        assertEquals("Order stored under order 2 should be John.", testOrder, shouldBeJohn);
        
        
        try {
            
            Orders shouldBeNull = testService.getOrder(LocalDate.of(2023, 6, 30), 3);
            fail("Something went wrong. That should not have worked.");
            
        } catch ( FloorMasterPersistenceException e ) {
            
            fail("Something went wrong. That's the wrong exception.");
            
        } catch ( InvalidOrderException e ) {
            
            return;
            
        }
        
    }
    
    @Test
    public void editOrder() throws Exception{
        
        Orders testOrder = new Orders("John", "TX", "Cedar", new BigDecimal("100"));
        testOrder.setOrderNumber(2);
        
        testService.createOrder(testOrder, LocalDate.of(2023, 6, 30));
        
        Orders testEditOrder = new Orders("James", "NY", "Maple", new BigDecimal("200"));
        
        testOrder = testService.editOrder(2, LocalDate.of(2023, 6, 30), testEditOrder);
        
        testService.calculateFields(testOrder);
        
        assertEquals("Customer name must be James", testOrder.getCustomerName(), "James");
        assertEquals("State should be NY", testOrder.getState(), "NY");
        assertEquals("Product Type should be Maple", testOrder.getProductType(), "Maple");
        assertEquals("Area should be 200", testOrder.getArea(), new BigDecimal("200"));
        assertEquals("Material cost must be 300.00", testOrder.getMaterialCost(), new BigDecimal("300.00"));
        assertEquals("Labor cost must be 350.00", testOrder.getLaborCost(), new BigDecimal("350.00"));
        assertEquals("Tax should be 65.00", testOrder.getTax(), new BigDecimal("65.00"));
        assertEquals("Total should be 715.00", testOrder.getTotal(), new BigDecimal("715.00"));
        
    }
    
    @Test
    public void deleteOrder() throws Exception {
        
        Orders testOrder = new Orders("John", "TX", "Cedar", new BigDecimal("100"));
        testOrder.setCostPerSquareFoot(new BigDecimal("1.5"));
        testOrder.setLaborCostPerSquareFoot(new BigDecimal("1.75"));
        testOrder.setTaxRate(new BigDecimal("7"));
        testOrder.setOrderNumber(2);
        
        testService.createOrder(testOrder, LocalDate.of(2023, 6, 30));
        testService.deleteOrder(LocalDate.of(2023, 6, 30), 2);
        
        
        assertTrue("John should be removed", testService.getAllOrder(LocalDate.of(2023, 6, 30)).size() == 0);
        
    }
    
}
