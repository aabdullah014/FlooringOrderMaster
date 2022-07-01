/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brogrammers.floormaster.dao;

import com.brogrammers.floormaster.dto.Product;
import java.math.BigDecimal;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author EricNguyen
 */
public class ProductDaoFileImplTest {

    /**
     * Test of getProduct method, of class ProductDaoFileImpl.
     */
    @Test
    public void testGetProduct() throws Exception {
        String productType = "Carpet";
        ProductDao instance = new ProductDaoFileImpl("testProducts.txt", "TestFiles/");
        Product expResult = new Product("Carpet", new BigDecimal("2.25"), new BigDecimal("2.10"));;
        Product result = instance.getProduct(productType);
        assertEquals("Should be Carpet.", expResult, result);
    }
    
}
