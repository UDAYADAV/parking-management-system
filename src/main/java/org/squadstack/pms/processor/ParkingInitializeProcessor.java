package org.squadstack.pms.processor;

import org.squadstack.pms.database.InMemoryDatabase;
import org.squadstack.pms.exception.ParkingLotAlreadyInitializedException;

import java.util.Arrays;
import java.util.List;

public class ParkingInitializeProcessor implements CommandProcessor {
    @Override
    public void process(String inputString) {
        List<String> stringList = Arrays.asList(inputString.split(" "));
        int parkingLotSize = Integer.parseInt(stringList.get(1));
        try {
            InMemoryDatabase.initialize(parkingLotSize);
            System.out.printf("Created parking of %s slots%n", parkingLotSize);
        } catch (ParkingLotAlreadyInitializedException e) {
            System.out.println("Parking lot is already initialized");
        }
    }
}
