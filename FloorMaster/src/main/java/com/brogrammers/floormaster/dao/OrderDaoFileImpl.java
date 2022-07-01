package com.brogrammers.floormaster.dao;

import com.brogrammers.floormaster.dto.Orders;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.springframework.stereotype.Component;

@Component
public class OrderDaoFileImpl implements OrderDao {
    
    // Read Orders from file and use last order number in there to keep
    // program up todate on current order number
    private int lastOrderNumber = 0;
    private static final String DELIMITER = ",";
    // for helping the program know where to read/write files from
    private final String ordersFolder;
    private final String dataBackUpFolder;
    // header to write to each new Orders_#######.txt file
    private static final String HEADER = "OrderNumber,CustomerName,State,TaxRate,ProductType,"
                                        +"Area,CostPerSquareFoot,LaborCostPerSquareFoot,"
                                        +"MaterialCost,LaborCost,Tax,Total";

    public OrderDaoFileImpl() {
        ordersFolder = "Orders/";
        dataBackUpFolder = "Backup/";
    }
    
    public OrderDaoFileImpl(String path, String backUpPath) {
        ordersFolder = path;
        dataBackUpFolder = backUpPath;
    }
    
    @Override
    public List<Orders> getAllOrders(LocalDate date) throws FloorMasterPersistenceException {
        return loadOrders(date);
    }
    
    // should i have separate getOrder method or have service layer call getAllOrders
    // and have it do the lambda function here to get the single order?
    @Override
    public Orders getOrder(LocalDate date, int orderNum) throws FloorMasterPersistenceException {
        List<Orders> ordersList = loadOrders(date);
        
        Orders chosenOrder = ordersList.stream()
                            .filter(o -> o.getOrderNumber() == orderNum)
                            .findFirst().orElse(null);
        return chosenOrder;
    }

    // Once we call this method, then we are sure we are adding this order to our file of orders
    @Override
    public Orders addOrder(Orders order, LocalDate orderDate) throws FloorMasterPersistenceException {
        // getting last order number from a file that we wrote to 
        // persist data through opening and closing this application
        readLastOrderNumber();
        lastOrderNumber++;
        
        order.setOrderNumber(lastOrderNumber);
        // writing to file to persist it through out application uses
        writeLastOrderNumber(lastOrderNumber);
        
        List<Orders> ordersList = loadOrders(orderDate);
        ordersList.add(order);
        
        // write ordersList to file w/ orderDate
        writeOrders(ordersList, orderDate);
        return order;
    }

    @Override
    public Orders editOrder(LocalDate date, int orderNum, Orders editedOrder) throws FloorMasterPersistenceException {
        List<Orders> ordersList = loadOrders(date);
        Orders chosenOrder = ordersList.stream()
                            .filter(o -> o.getOrderNumber() == orderNum)
                            .findFirst().orElse(null);
        
        if(chosenOrder != null) {
            int index = ordersList.indexOf(chosenOrder);
            // the edited order passed into here should be verified to just replace the 
            // last order object there and have all the fields set
            ordersList.set(index, editedOrder);
            writeOrders(ordersList, date);
            return editedOrder;
        } else {
            return null;
        }
    }

    // load all the orders w/ date into a list
    // filter list w/ orderNumber to get order you want to remove
    // remove it from list and write list back to file to overwrite it and have
    // the order removed
    // return removedOrder or null if it doesn't exist
    @Override
    public Orders removeOrder(LocalDate date, int orderNum) throws FloorMasterPersistenceException {
        List<Orders> ordersList = loadOrders(date);
        Orders removedOrder = ordersList.stream()
                            .filter(o -> o.getOrderNumber() == orderNum)
                            .findFirst().orElse(null);
        
        if(removedOrder != null) {
            ordersList.remove(removedOrder);
            writeOrders(ordersList, date);
            return removedOrder;
        } else {
            return null;
        }
    }
    
    @Override
    public void exportAllData() throws FloorMasterPersistenceException {
        PrintWriter out;
        
        try {
            out = new PrintWriter(new FileWriter(dataBackUpFolder + "DataExport.txt"));
        } catch(IOException e) {
            throw new FloorMasterPersistenceException("Could not export data.", e);
        }
        
        out.println(HEADER + "OrderDate");
        
        // hold a map key: date to value: list of orders from date
        Map<String, List<Orders>> exportData = new LinkedHashMap<>();
        
        // filter grabs all files w/ a . in it meaning it's not a directory
        FilenameFilter filter = (dir, fileName) -> {
            return fileName.contains("Orders_");
        };
        // get a list of all names in Orders/ directory that contain a . in it
        String[] allOrderFiles = new File(ordersFolder).list(filter);
        
        // filter through list to get date from file names
        for(String orderFile : allOrderFiles) {
            // Split file name as : Order,MMddyyyy,txt
            // regex splits file name based on an _ or a . (needs \\ escape character)
            String[] fileNameTokens = orderFile.split("_|\\.");
            String date = fileNameTokens[1];
            
            // need to make date into local date format
            String dateHyphen = date.substring(4, 8) + "-" + date.substring(0, 2) + "-" + date.substring(2, 4);
            
            // get all orders from file associated w/ date and put into list
            List<Orders> allOrdersFromCurrentFile = loadOrders(LocalDate.parse(dateHyphen));
            // put list into map to associate dates w/ all its orders
            exportData.put(dateHyphen, allOrdersFromCurrentFile);
        }
        
        String exportDataAsText;
        // make the list of orders as text to write data to file
        for(String date : exportData.keySet()) {
            List<Orders> currentOrderList = exportData.get(date);
            for(Orders currentOrder : currentOrderList) {
                exportDataAsText = currentOrder.getOrderNumber() + DELIMITER +
                                   currentOrder.getCustomerName() + DELIMITER +
                                   currentOrder.getState() + DELIMITER +
                                   currentOrder.getTaxRate() + DELIMITER +
                                   currentOrder.getProductType() + DELIMITER +
                                   currentOrder.getArea() + DELIMITER +
                                   currentOrder.getCostPerSquareFoot() + DELIMITER +
                                   currentOrder.getLaborCostPerSquareFoot() + DELIMITER +
                                   currentOrder.getMaterialCost() + DELIMITER +
                                   currentOrder.getLaborCost() + DELIMITER +
                                   currentOrder.getTax() + DELIMITER +
                                   currentOrder.getTotal() + DELIMITER +
                                   date;
                out.println(exportDataAsText);
                out.flush();
            }
        }
        out.close();
    }
    
    private List<Orders> loadOrders(LocalDate date) throws FloorMasterPersistenceException {
        
        Scanner scanner;
        // converts the date format into MMddyyyy
        String fileDate = date.format(DateTimeFormatter.ofPattern("MMddyyyy"));
        
        // program should be getting file from orders/Orders_#######.txt
        File f = new File(String.format(ordersFolder + "Orders_%s.txt", fileDate));
        
        List<Orders> ordersList = new ArrayList<>();
        
        if(f.isFile()) {
            try {
                scanner = new Scanner(new BufferedReader(new FileReader(f)));
            } catch(FileNotFoundException e) {
                throw new FloorMasterPersistenceException("Could not load order data into memory from Orders folder.", e);
            }
            
            String currentLine;
            String[] orderTokens;
            scanner.nextLine(); // skips header data
            
            while(scanner.hasNextLine()) {
                currentLine = scanner.nextLine();
                orderTokens = currentLine.split(DELIMITER);
                
                Orders currentOrder = new Orders();
                int i = 1;
                currentOrder.setOrderNumber(Integer.parseInt(orderTokens[0]));

                // name field has commas in them
                if(orderTokens.length > 12) {
                    int numOfCommas = 11;
                    int sizeDiff = orderTokens.length - numOfCommas;
                    StringBuilder name = new StringBuilder();
                    // will concatenate strings together in a StringBuilder to set the customer name
                    // i will be the offset for the other setters
                    for(i = 1; i <= sizeDiff; i++){
                        name.append(orderTokens[i]);
                        if(i < sizeDiff){
                            name.append(",");
                        }
                    }
                    currentOrder.setCustomerName(name.toString());
                    // reset by 1 because of for loop increasing after last iteration to check to continue loop
                    i--;
                } else if(orderTokens.length == 12) {
                    // if length of tokens is 12 then the line of info. is normal
                    currentOrder.setCustomerName(orderTokens[i]);
                }

                currentOrder.setState(orderTokens[i + 1]);
                currentOrder.setTaxRate(new BigDecimal(orderTokens[i + 2]));
                currentOrder.setProductType(orderTokens[i + 3]);
                currentOrder.setArea(new BigDecimal(orderTokens[i + 4]));
                currentOrder.setCostPerSquareFoot(new BigDecimal(orderTokens[i + 5]));
                currentOrder.setLaborCostPerSquareFoot(new BigDecimal(orderTokens[i + 6]));
                currentOrder.setMaterialCost(new BigDecimal(orderTokens[i + 7]));
                currentOrder.setLaborCost(new BigDecimal(orderTokens[i + 8]));
                currentOrder.setTax(new BigDecimal(orderTokens[i + 9]));
                currentOrder.setTotal(new BigDecimal(orderTokens[i + 10]));
                ordersList.add(currentOrder);
            }
            
            scanner.close();
            return ordersList;
        } else {
            return ordersList; // will return empty list since .isFile() return false
        }
    }
    
    private void writeOrders(List<Orders> ordersList, LocalDate orderDate) throws FloorMasterPersistenceException {
        PrintWriter out;
        String fileDate = orderDate.format(DateTimeFormatter.ofPattern("MMddyyyy"));
        File f = new File(String.format(ordersFolder + "Orders_%s.txt", fileDate));
        
        try {
            out = new PrintWriter(new FileWriter(f));
        } catch(IOException e) {
            throw new FloorMasterPersistenceException("Could not save order data.", e);
        }
        
        out.println(HEADER);
        for(Orders currentOrder : ordersList) {
            out.println(currentOrder.getOrderNumber() + DELIMITER +
                        currentOrder.getCustomerName() + DELIMITER +
                        currentOrder.getState() + DELIMITER +
                        currentOrder.getTaxRate() + DELIMITER +
                        currentOrder.getProductType() + DELIMITER +
                        currentOrder.getArea() + DELIMITER +
                        currentOrder.getCostPerSquareFoot() + DELIMITER +
                        currentOrder.getLaborCostPerSquareFoot() + DELIMITER +
                        currentOrder.getMaterialCost() + DELIMITER +
                        currentOrder.getLaborCost() + DELIMITER +
                        currentOrder.getTax() + DELIMITER +
                        currentOrder.getTotal());
            out.flush();
        }
        out.close();
    }
    
    public void readLastOrderNumber() throws FloorMasterPersistenceException {
        Scanner scanner;
        
        try {
            scanner = new Scanner(new BufferedReader(new FileReader(ordersFolder + "LastOrderNumber.txt")));
        } catch (FileNotFoundException e) {
            throw new FloorMasterPersistenceException("Could not load order number into memory", e);
        }
        
        this.lastOrderNumber = Integer.parseInt(scanner.nextLine());
        scanner.close();
    }
    
    private void writeLastOrderNumber(int lastOrderNumber) throws FloorMasterPersistenceException {
        PrintWriter out;
        
        try {
            out = new PrintWriter(new FileWriter(ordersFolder + "LastOrderNumber.txt"));
        } catch(IOException e){
            throw new FloorMasterPersistenceException("Could not save order number to fi;e", e);
        }
        
        out.println(lastOrderNumber);
        out.flush();
        out.close();
    }
}