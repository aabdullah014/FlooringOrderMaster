package com.brogrammers.floormaster.dao;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class AuditDaoFileImpl implements AuditDao{
    public static final String AUDIT_FILE = "audit.txt";
    
    @Override
    public void writeAuditEntry(String entry) throws FloorMasterPersistenceException {
        PrintWriter out;
        try {
            out = new PrintWriter(new FileWriter(AUDIT_FILE, true));
        } catch(IOException e) {
            throw new FloorMasterPersistenceException("Could persist audit information.", e);
        }

        LocalDateTime timeStamp = LocalDateTime.now();
        out.println(timeStamp.toString() + " : " + entry);
        out.flush();
        out.close();
    }
}