/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brogrammers.floormaster.dao;

import com.brogrammers.floormaster.dto.Tax;
import java.math.BigDecimal;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author EricNguyen
 */
public class TaxDaoFileImplTest {

    /**
     * Test of getState method, of class TaxDaoFileImpl.
     */
    @Test
    public void testGetState() throws Exception {
        String stateAbbr = "WA";
        TaxDaoFileImpl instance = new TaxDaoFileImpl("testTaxes.txt", "TestFiles/");
        Tax expResult = new Tax("WA", "Washington", new BigDecimal("9.25"));
        Tax result = instance.getState(stateAbbr);
        assertEquals("Should be Washington", expResult, result);
    }
    
}
