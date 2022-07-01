/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.brogrammers.floormaster.ui;

import com.brogrammers.floormaster.dto.Orders;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 *
 * @author Brian
 */

@Component
public class FloorMasterView {
    
    private final UserIO io;

    @Autowired
    public FloorMasterView(UserIO io) {
        
        this.io = io;
        
    }
    
    
    public int printMenuGetChoice(){
        
        this.delineater();
        io.print("* <<Flooring Program>>");
        io.print("* 1. Display Orders");
        io.print("* 2. Add an Order");
        io.print("* 3. Edit an Order");
        io.print("* 4. Remove an Order");
        io.print("* 5. Export all Data");
        io.print("* 6. Quit");
        this.delineater();
            
        return io.readInt("Please Select from the numbers above.", 1, 6);
        
    }
    
    public LocalDate getOrderDate() {
        boolean validInput = false;
        String orderDate;
        
        do{
            orderDate = io.readString("Please enter your desired date of delivery in YYYY-MM-DD format.");
            
            // make sure the length is 10
            if (orderDate.length() != 10) {
                continue;
            }
            
            int year = Integer.parseInt(orderDate.substring(0,4));
            int month = Integer.parseInt(orderDate.substring(5,7));
            int day = Integer.parseInt(orderDate.substring(8));
            // make sure the release date is a possible data  in correct format
            if (orderDate.substring(4,5).equals("-") && orderDate.substring(7,8).equals("-")
                    && day < 32 && day > 0 && month < 13 && month > 0 && year > 0){
                validInput = true;
            }
        } while (!validInput);
        
        return LocalDate.parse(orderDate);
        
    }
    
    public Orders getNewOrderInfo(){
        
        String customerName = io.readString("Please enter your name.");
        
        boolean validAnswer = false;
        String state;
        do{
            state = io.readString("Please enter your state abbreviation.");
            
            if (state.length() == 2) {
                validAnswer = true;
            }
            
        } while (!validAnswer);
        
        String productType = io.readString("Please enter the type of product.");
        BigDecimal area = io.readBigDecimal("Please enter the desired area.");
        
        Orders order = new Orders();
        order.setCustomerName(customerName);
        order.setArea(area);
        order.setProductType(productType
                    .replace(productType.substring(0,1), productType.substring(0,1).toUpperCase()));
        order.setState(state.toUpperCase());
        
        return order;
        
    }
    
    public void displayOrders(List<Orders> orderList) {
        
        if (orderList.size() == 0) {
            this.delineater();
            io.print("No orders for this date!");
            return;
        }
        
        for (Orders o: orderList) {
            
            io.print("# " + o.getOrderNumber() + 
                    " | Name: " + o.getCustomerName() +
                    " | State: " + o.getState() + 
                    " | Product: " + o.getProductType() +
                    " | Area: " + o.getArea() + 
                    " | Total: " + o.getTotal());
            
        }
        
    }
    
    public String previewAddOrder(Orders order, LocalDate orderDate) {
        
        io.print("You are about to add an order.");
        return this.previewOrder(order, orderDate);
        
    }
    
    public Orders editOrder(Orders order){
        // return edits user wants to change
        
        Orders editedOrder = new Orders();
        editedOrder.setCustomerName(order.getCustomerName());
        editedOrder.setState(order.getState());
        editedOrder.setProductType(order.getProductType());
        editedOrder.setArea(order.getArea());
        editedOrder.setOrderNumber(order.getOrderNumber());
        
        
        String customerName = io.readString("Enter Customer Name: (" + order.getCustomerName() + ")");
        if (!customerName.equals("")) {
            editedOrder.setCustomerName(customerName);
        }
    
        String state;
        
        boolean validAnswer = false;
        do{
            state = io.readString("Please enter your state abbreviation.");
            
            if (state.length() == 2 || state.length() == 0) {
                validAnswer = true;
            }
            
        } while (!validAnswer);
        
        if (!state.equals("")) {
            editedOrder.setState(state);
        }
        
        String productType = io.readString("Enter Product: (" + order.getProductType() + ")");
        if (!productType.equals("")) {
            editedOrder.setProductType(productType);
        }
        
        BigDecimal area = io.readBigDecimal("Enter Area: (" + order.getArea().toString() + ")");
        if (area.compareTo(new BigDecimal("0")) != 0) {
            editedOrder.setArea(area);
        }
        
        return editedOrder;
    }
    
    public String previewEditOrder(Orders order, LocalDate orderDate){
        // Show a summary of the order once the calculations are completed and 
        // prompt the user as to whether they want to edit the order (Y/N).
        
        io.print("You are about to edit an order.");
        return this.previewOrder(order, orderDate);
        
    }
    
    public Orders removeOrder(Orders order) {
        
        LocalDate orderDate = this.getExistingOrderDate();
        this.getExistingOrderNumber();
        
        return order;
        
    }
    
    public String previewDeleteOrder(Orders order, LocalDate orderDate){
        // Show a summary of the order once the calculations are completed and 
        // prompt the user as to whether they want to delete the order (Y/N).
        
        io.print("You are about to delete an order.");
        return this.previewOrder(order, orderDate);
        
    }
    
     public String previewOrder(Orders order, LocalDate orderDate){
        // Show a summary of the order once the calculations are completed and 
        // prompt the user as to whether they want to place the order (Y/N).
        
        io.print("Name: " + order.getCustomerName());
        io.print("State: " + order.getState().toUpperCase());
        String productType = order.getProductType();
        io.print("Product: " + productType
                .replace(productType.substring(0,1), productType.substring(0,1).toUpperCase()));
        io.print("Desired delivery date: " + String.valueOf(orderDate));
        io.print("Area: " + order.getArea().toString());
        io.print("Material cost: " + order.getMaterialCost().toString());
        io.print("Labor cost: " + order.getLaborCost().toString());
        io.print("Tax: " + order.getTax().toString());
        io.print("Total: " + order.getTotal().toString());
        
        boolean validAnswer = false;
        String answer;
        do{
            answer = io.readString("Would you like to proceed? Y/N");
            
            if (answer.equals("y") || answer.equals("Y") 
                    || answer.equals("n") || answer.equals("N")){
                validAnswer = true;
            }
            
        } while (!validAnswer);
        
        return answer;
    }
     
     public void previewFulfilledOrder(Orders order, LocalDate orderDate){
        // Show a summary of the order once the calculations are completed and 
        // prompt the user as to whether they want to place the order (Y/N).
        
        io.print("Your order number is #: " + order.getOrderNumber());
        io.print("Congrats your order has been placed!");
    }
    
    public LocalDate getExistingOrderDate() {

        return io.readLocalDate("What is the order date?");
        
    }
    
    public int getExistingOrderNumber() {
       
        return io.readInt("What is the order number?");
        
    }
    
    public void delineater(){
        
        io.print(" * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        
    }
    
    public void errorMessage(String e) {
        
        this.delineater();
        io.print(e);
        this.delineater();
        
    }
    
    public void successMessage() {
        
        this.delineater();
        io.print("SUCCESS");
        
    }
    
    public void exitBanner() {
        
        this.delineater();
        io.print("Goodbye!");
        
    }
    
}