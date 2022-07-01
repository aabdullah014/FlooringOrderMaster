/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.brogrammers.floormaster;

import com.brogrammers.floormaster.controller.FloorMasterController;
import com.brogrammers.floormaster.dao.FloorMasterPersistenceException;
import com.brogrammers.floormaster.service.DataValidationException;
import com.brogrammers.floormaster.service.InvalidDateException;
import com.brogrammers.floormaster.service.InvalidOrderException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 *
 * @author Brian
 */
public class App {

    public static void main(String[] args) throws DataValidationException, FloorMasterPersistenceException, InvalidDateException, InvalidOrderException {
        
        AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext();
        appContext.scan("com.brogrammers.floormaster");
        appContext.refresh();

        FloorMasterController controller = appContext.getBean("floorMasterController", FloorMasterController.class);
        controller.run();
        
    }
}
