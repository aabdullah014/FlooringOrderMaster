package com.brogrammers.floormaster.dao;

import com.brogrammers.floormaster.dto.Product;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.springframework.stereotype.Component;

@Component
public class ProductDaoFileImpl implements ProductDao {
    
    private final String PRODUCTS_FILE;
    private final String DATA_FOLDER;
    private static final String DELIMITER = ",";
    
    public ProductDaoFileImpl() {
        PRODUCTS_FILE = "Products.txt";
        DATA_FOLDER = "Data/";
    }
    
    public ProductDaoFileImpl(String fileName, String path) {
        PRODUCTS_FILE = fileName;
        DATA_FOLDER = path;
    }
    /**
     * Check against our list to see if we have the available product, if not then we don't sell it
     * @param productType
     * @return returning the desired product from our current list that we have
     * if not product then return null
     * @throws FloorMasterPersistenceException 
     */
    @Override
    public Product getProduct(String productType) throws FloorMasterPersistenceException {
        // might move outside of loop since we know that we don't have to add anything to Products.txt
        // load products into our list
        List<Product> products = loadProductsFromFile();
        
        if(products == null) {
            return null;
        } else {
            // using lambda function to return the desired product from our list of products
            // not available, then return null
            Product chosenProduct = products.stream()
                                    .filter(p -> p.getProductType().equalsIgnoreCase(productType))
                                    .findFirst().orElse(null);
            return chosenProduct;
        }
    }
    
    /**
     * Read from file, skips first line, and parses it base on the , delimiter
     * @return list of our current available products for our Dao to sift through to return the
     * correct product if available.
     * @throws FloorMasterPersistenceException 
     */
    private List<Product> loadProductsFromFile() throws FloorMasterPersistenceException {
        Scanner scanner;
        List<Product> productList = new ArrayList<>();
        
        try {
            scanner = new Scanner(new BufferedReader(new FileReader(DATA_FOLDER + PRODUCTS_FILE)));
        } catch(FileNotFoundException e) {
            throw new FloorMasterPersistenceException("Could not load product data into memeory.", e);
        }
        
        String currentLine;
        String[] productTokens;
        scanner.nextLine(); // skip the document headers in file
        
        while(scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            productTokens = currentLine.split(DELIMITER);
            
            // in file we get 3 values from a line
            // won't add into our products if the file line was delimited wrong/empty
            if(productTokens.length == 3) {
                Product currentProduct = new Product();
                currentProduct.setProductType(productTokens[0]);
                currentProduct.setCostPerSquareFoot(new BigDecimal(productTokens[1]));
                currentProduct.setLaborCostPerSquareFoot(new BigDecimal(productTokens[2]));
                
                productList.add(currentProduct);
            }
        }
        scanner.close();
        
        if(!productList.isEmpty()) {
            return productList;
        } else {
            return null;
        }
    }
}