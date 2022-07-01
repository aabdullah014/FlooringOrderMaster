/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.brogrammers.floormaster.ui;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import org.springframework.stereotype.Component;

/**
 *
 * @author Brian
 */

@Component
public class UserIOConsoleImpl implements UserIO{

    final private Scanner console = new Scanner(System.in);
    
    @Override
    public void print(String msg) {
        System.out.println(msg);
    }
    
    @Override
    public String readString(String prompt) {
        System.out.println(prompt);
        return console.nextLine();
    }
    
    @Override
    public int readInt(String prompt) {
        boolean invalidInput = true;
        int num = 0;
        
        while(invalidInput){
            try{
                String stringVal = this.readString(prompt);
                num = Integer.parseInt(stringVal);
                invalidInput = false;
            } catch (NumberFormatException e){
                this.print("Input Error. Please try again.");
            }
        }
        
        return num;
    }

    @Override
    public LocalDate readLocalDate(String prompt) {
        
        LocalDate date = null;
        boolean invalidInput = true;
        
        while(invalidInput){
            try{
                String stringVal = this.readString(prompt);
                date = LocalDate.parse(stringVal);
                invalidInput = false;
            } catch (DateTimeParseException e){
                this.print("Input Error. Please try again.");
            }
        }
        
        return date;
        
    }
    
    @Override
    public int readInt(String prompt, int min, int max) {
        int result;
        
        do{
            result = this.readInt(prompt);
        } while (result < min || result > max);
        
        return result;
        
    }
    
    @Override
    public BigDecimal readBigDecimal(String prompt) {
        boolean invalidInput = true;
        BigDecimal num = new BigDecimal("0.0");
        
        while(invalidInput){
            try{
                String stringVal = this.readString(prompt);
                num = BigDecimal.valueOf(Double.parseDouble(stringVal)).setScale(2, RoundingMode.HALF_UP);
                invalidInput = false;
            } catch (NumberFormatException e){
                this.print("Input Error. Please try again.");
            }
        }
        
        return num;
    }

    @Override
    public BigDecimal readBigDecimal(String prompt, BigDecimal min, BigDecimal max) {
        BigDecimal result;
        int greaterThanMin = 0;
        int lessThanMax = 0;
        
        
        do{
            result = BigDecimal.valueOf(Double.parseDouble(prompt));
            greaterThanMin = result.compareTo(min);
            lessThanMax = result.compareTo(max);
        } while (greaterThanMin != 1 || lessThanMax != -1);
        
        return result;
    }
    
}