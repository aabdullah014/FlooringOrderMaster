/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.brogrammers.floormaster.ui;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 * @author Brian
 */
public interface UserIO {
    
    void print(String msg);
    
    int readInt(String prompt);
    
    int readInt(String prompt, int min, int max);
    
    LocalDate readLocalDate(String prompt);
    
    BigDecimal readBigDecimal(String prompt);
    
    BigDecimal readBigDecimal(String prompt, BigDecimal min, BigDecimal max);
    
    String readString(String prompt);
    
}
