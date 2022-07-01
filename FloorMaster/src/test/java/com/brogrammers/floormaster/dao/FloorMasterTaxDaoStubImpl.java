/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.brogrammers.floormaster.dao;
import com.brogrammers.floormaster.dto.Tax;
import java.math.BigDecimal;

/**
 *
 * @author abdulrahman
 */
public class FloorMasterTaxDaoStubImpl implements TaxDao {

    public Tax tax1;
    public Tax tax2;
    
    public FloorMasterTaxDaoStubImpl() {
        
        tax1 = new Tax();
        tax1.setStateAbbreviation("TX");
        tax1.setStateName("Texas");
        tax1.setTaxRate(new BigDecimal("7"));
        
        tax2 = new Tax();
        tax2.setStateAbbreviation("NY");
        tax2.setStateName("New York");
        tax2.setTaxRate(new BigDecimal("10"));
        
    }
    
    public FloorMasterTaxDaoStubImpl(Tax testTax1, Tax testTax2) {
        
        this.tax1 = testTax1;
        this.tax2 = testTax2;
        
    }
    
    @Override
    public Tax getState(String stateAbbr) throws FloorMasterPersistenceException {
        
        if (stateAbbr.equals(tax1.getStateAbbreviation())) {
            
            return tax1;
            
        } else if (stateAbbr.equals(tax2.getStateAbbreviation())) {
            
            return tax2;
            
        }
        
        return null;
        
    }

}
