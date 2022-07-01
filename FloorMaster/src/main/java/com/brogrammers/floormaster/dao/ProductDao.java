/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.brogrammers.floormaster.dao;

import com.brogrammers.floormaster.dto.Product;

/**
 *
 * @author Brian
 */
public interface ProductDao {
    Product getProduct(String productType) throws FloorMasterPersistenceException;
}
