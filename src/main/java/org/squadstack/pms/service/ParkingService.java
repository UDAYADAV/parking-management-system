package org.squadstack.pms.service;

import org.squadstack.pms.entity.Vehicle;
import org.squadstack.pms.exception.InvalidSlotIdException;
import org.squadstack.pms.exception.TicketIssueException;
import org.squadstack.pms.exception.VehicleDoesNotExistException;

import java.util.List;

public interface ParkingService {
    Vehicle issueTicket(String registrationNumber, int driverAge) throws TicketIssueException;

    Vehicle leave(int slotId) throws InvalidSlotIdException;

    int getSlotNumber(String registrationNumber) throws VehicleDoesNotExistException;

    List<String> getRegistrationNumbersWithDriverAge(int driverAge);

    List<Integer> getSlotNumbersWithDriverAge(int driverAge);
}
