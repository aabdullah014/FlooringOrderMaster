package com.brogrammers.floormaster.dao;

public class FloorMasterPersistenceException extends Exception{
    public FloorMasterPersistenceException(String message){
        super(message);
    }

    public FloorMasterPersistenceException(String message, Throwable cause){
        super(message, cause);
    }
}