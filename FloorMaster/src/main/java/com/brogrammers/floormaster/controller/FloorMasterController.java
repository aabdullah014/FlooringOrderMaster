package com.brogrammers.floormaster.controller;

import com.brogrammers.floormaster.dao.FloorMasterPersistenceException;
import com.brogrammers.floormaster.dto.Orders;
import com.brogrammers.floormaster.service.DataValidationException;
import com.brogrammers.floormaster.service.FloorService;
import com.brogrammers.floormaster.service.InvalidDateException;
import com.brogrammers.floormaster.service.InvalidOrderException;
import com.brogrammers.floormaster.ui.FloorMasterView;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FloorMasterController {

    private FloorService serviceLayer;
    private FloorMasterView view;

    @Autowired
    public FloorMasterController(FloorMasterView view, FloorService service) {
        this.view = view;
        this.serviceLayer = service;
    }

    public void run() throws DataValidationException, FloorMasterPersistenceException, InvalidDateException, InvalidOrderException {
        boolean keepGoing = true;
        int menuChoice;

        while (keepGoing) {
            try {
                menuChoice = printMenuGetChoice();

                switch (menuChoice) {
                    case 1: //displayOrders
                        displayOrders();
                        break;
                    case 2: //addOrders
                        addOrders();
                        break;
                    case 3: //editOrders
                        editOrders();
                        break;
                    case 4: //remove
                        removeOrders();
                        break;
                    case 5: //in case we want to do Export
                        exportData();
                        break;
                    case 6: //exit
                        keepGoing = false;
                        break;
                    default: //error if anything except the choices
                        errorMessage();
                }

            } catch (FloorMasterPersistenceException e) {
                view.errorMessage(e.getMessage());
            }
        }
        exitMessage();
    }

    //get Menu Choice
    private int printMenuGetChoice() {
        return view.printMenuGetChoice();
    }

    //use orderDate to getAllOrders from service layer
    private void displayOrders() throws FloorMasterPersistenceException, DataValidationException, InvalidDateException, InvalidOrderException {
        try {
            LocalDate orderDate = view.getOrderDate();
            List<Orders> ordersList = serviceLayer.getAllOrder(orderDate);
            view.displayOrders(ordersList);

            if (ordersList.size() > 0) {
                view.successMessage();
            }
        } catch (Exception e) {
            view.errorMessage(e.getMessage());
        }
    }

    //get order date, then call getNewOrderInfo to get information. 
    //service layer validates data of order, makes sure order Date is in the future
    //service layer calculates fields (taxes, etc)
    //view lets user preview order, confirm Y to continue with creation in service layer
    private void addOrders() throws DataValidationException, FloorMasterPersistenceException, InvalidDateException {
        try {
            LocalDate orderDate = view.getOrderDate();
            String confirmAddOrder;
            Orders order = view.getNewOrderInfo();
            serviceLayer.validateData(order, orderDate);
            serviceLayer.calculateFields(order);
            confirmAddOrder = view.previewOrder(order, orderDate);
            if (confirmAddOrder.equalsIgnoreCase("Y")) {

                serviceLayer.createOrder(order, orderDate);
                view.previewFulfilledOrder(order, orderDate);
                view.successMessage();
            }
        } catch (Exception e) {
            view.errorMessage(e.getMessage());
        }
    }

    //get order date and number, get the order from service
    //inside of view, you would create temp editedOrder object
    //use editedOrder to calculateFields and preview
    //if preview returns yes, call service.editOrder with date, order # and editedOrder as parameters
    //change original object with edited object's parameters
    private void editOrders() throws FloorMasterPersistenceException, DataValidationException, InvalidDateException, InvalidOrderException {
        try {
            LocalDate orderDate = view.getExistingOrderDate();
            int orderNumber = view.getExistingOrderNumber();
            Orders tempOrder = serviceLayer.getOrder(orderDate, orderNumber);
            tempOrder = view.editOrder(tempOrder);
            serviceLayer.calculateFields(tempOrder);

            String confirmEditOrder = view.previewOrder(tempOrder, orderDate);
            if (confirmEditOrder.equalsIgnoreCase("Y")) {
                serviceLayer.editOrder(orderNumber, orderDate, tempOrder);
                view.successMessage();
            }
        } catch (Exception e) {
            view.errorMessage(e.getMessage());
        }
    }

    //use order date and number to get order from service layer
    //preview order, return Y to confirm removal from service layer
    private void removeOrders() throws FloorMasterPersistenceException, DataValidationException, InvalidDateException, InvalidOrderException {
        try {
            LocalDate orderDate = view.getExistingOrderDate();
            int orderNumber = view.getExistingOrderNumber();
            Orders tempOrder = serviceLayer.getOrder(orderDate, orderNumber);
            String confirmRemoveOrder = view.previewOrder(tempOrder, orderDate);
            if (confirmRemoveOrder.equalsIgnoreCase("Y")) {
                serviceLayer.deleteOrder(orderDate, orderNumber);
                view.successMessage();
            }
        } catch (Exception e) {
            view.errorMessage(e.getMessage());
        }
    }

    private void exportData() throws FloorMasterPersistenceException {
        serviceLayer.exportAllData();
        view.successMessage();
    }

    private void errorMessage() {
        view.errorMessage("ERROR");
    }

    private void exitMessage() {
        view.exitBanner();
    }
}
