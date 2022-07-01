/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.brogrammers.floormaster.dao;

import com.brogrammers.floormaster.dto.Product;
import java.math.BigDecimal;

/**
 *
 * @author abdulrahman
 */
public class FloorMasterProductDaoStubImpl implements ProductDao {

    public Product product1;
    public Product product2;
    
    public FloorMasterProductDaoStubImpl() {
        
        product1 = new Product();
        product1.setProductType("Cedar");
        product1.setCostPerSquareFoot(new BigDecimal("1.5"));
        product1.setLaborCostPerSquareFoot(new BigDecimal("1.75"));
        
        product2 = new Product();
        product2.setProductType("Maple");
        product2.setCostPerSquareFoot(new BigDecimal("1.5"));
        product2.setLaborCostPerSquareFoot(new BigDecimal("1.75"));
        
    }
    
    public FloorMasterProductDaoStubImpl(Product testProduct1, Product testProduct2) {
        
        this.product1 = testProduct1;
        this.product2 = testProduct2;
        
    }
    
    @Override
    public Product getProduct(String productType) throws FloorMasterPersistenceException {
        
        if (productType.equals(product1.getProductType())) {
            
            return product1;
            
        } else if (productType.equals(product2.getProductType())) {
            
            return product2;
            
        }
        
        return null;
        
    }
    
}
