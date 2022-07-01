/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.brogrammers.floormaster.dao;

import com.brogrammers.floormaster.dto.Tax;

/**
 *
 * @author Brian
 */
public interface TaxDao {
    Tax getState(String stateAbbr) throws FloorMasterPersistenceException;
}
