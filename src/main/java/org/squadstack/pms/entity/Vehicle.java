package org.squadstack.pms.entity;

import org.squadstack.pms.exception.InvalidInputException;

import java.util.regex.Pattern;

public class Vehicle {
    private final String registrationNumber;
    private final int driverAge;

    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }

    private int slotId;

    public Vehicle(String registrationNumber, int driverAge) throws InvalidInputException {
        if (driverAge <= 0 || !Pattern.compile("[A-Z]{2}-[0-9]{2}-[A-Z]{2}-[0-9]{4}")
                .matcher(registrationNumber).matches()) {
            throw new InvalidInputException("Registration number or driver age is invalid");
        }
        this.registrationNumber = registrationNumber;
        this.driverAge = driverAge;
    }

    public int getSlotId() {
        return slotId;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public int getDriverAge() {
        return driverAge;
    }
}
