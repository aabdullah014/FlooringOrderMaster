package com.brogrammers.floormaster.dao;

import com.brogrammers.floormaster.dto.Tax;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.springframework.stereotype.Component;

@Component
public class TaxDaoFileImpl implements TaxDao {
    
    private final String TAXES_FILE;
    private final String DATA_FOLDER;
    private static final String DELIMITER = ",";
    
    public TaxDaoFileImpl() {
        TAXES_FILE = "Taxes.txt";
        DATA_FOLDER = "Data/";
    }
    
    public TaxDaoFileImpl(String fileName, String path) {
        TAXES_FILE = fileName;
        DATA_FOLDER = path;
    }
    
    /**
     * Checks against our list to see if we service a certain state, otherwise we don't service that state
     * @param stateAbbr - state that the order is coming from
     * @return null if we don't service that state or will give the correct object associated w/
     * the state and return its necessary data (state and tax rate)
     * @throws FloorMasterPersistenceException 
     */
    @Override
    public Tax getState(String stateAbbr) throws FloorMasterPersistenceException {
        List<Tax> statesAndTax = loadTaxStatesFromFile();
        
        if(statesAndTax == null) {
            return null;
        } else {
            Tax chosenStateAndTax = statesAndTax.stream()
                                    .filter(sT -> sT.getStateAbbreviation().equalsIgnoreCase(stateAbbr))
                                    .findFirst().orElse(null);
            return chosenStateAndTax;
        }
    }
    
    /**
     * Read from file, skips first line, and parses it base on the , delimiter
     * format: State, StateName, TaxRate
     * @return a List of Tax objects that was read from the file
     * @throws FloorMasterPersistenceException 
     */
    private List<Tax> loadTaxStatesFromFile() throws FloorMasterPersistenceException {
        Scanner scanner;
        List<Tax> statesAndTaxesList = new ArrayList<>();
        
        try {
            scanner = new Scanner(new BufferedReader(new FileReader(DATA_FOLDER + TAXES_FILE)));
        } catch(FileNotFoundException e) {
            throw new FloorMasterPersistenceException("Could not load tax/state data into memeory.", e);
        }
        
        String currentLine;
        String[] currentTokens;
        scanner.nextLine(); // skip the document headers in file
        
        while(scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentTokens = currentLine.split(DELIMITER);
            
            // in file we get 3 values from a line
            // won't add into our states/taxes if the file line was delimited wrong/empty
            // format: State, StateName, TaxRate
            if(currentTokens.length == 3) {
                Tax currentStateAndTax = new Tax();
                currentStateAndTax.setStateAbbreviation(currentTokens[0]);
                currentStateAndTax.setStateName(currentTokens[1]);
                currentStateAndTax.setTaxRate(new BigDecimal(currentTokens[2]));
                
                statesAndTaxesList.add(currentStateAndTax);
            }
        }
        scanner.close();
        
        if(!statesAndTaxesList.isEmpty()) {
            return statesAndTaxesList;
        } else {
            return null;
        }
    }
}