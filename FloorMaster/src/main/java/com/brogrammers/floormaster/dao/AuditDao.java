/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.brogrammers.floormaster.dao;

/**
 *
 * @author Brian
 */
public interface AuditDao {
    void writeAuditEntry(String entry) throws FloorMasterPersistenceException;
}
