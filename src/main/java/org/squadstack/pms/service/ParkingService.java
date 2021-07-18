package org.squadstack.pms.service;

import org.squadstack.pms.entity.Vehicle;
import org.squadstack.pms.exception.InvalidSlotIdException;
import org.squadstack.pms.exception.TicketIssueException;
import org.squadstack.pms.exception.VehicleDoesNotExistException;

import java.util.List;

public interface ParkingService {
    /**
     * @param registrationNumber The vehicle registration number
     * @param driverAge          Age of driver
     * @return {@link Vehicle} containing slotId that the vehicle is assigned
     * @throws TicketIssueException If ticket can be assigned either because of invalid registration number, age or slots are full
     */
    Vehicle issueTicket(String registrationNumber, int driverAge) throws TicketIssueException;

    /**
     * @param slotId The slotId that is being marked as free
     * @return {@link Vehicle} Details of the vehicle & slot
     * @throws InvalidSlotIdException If slotId is invalid or no vehicle is parked
     */
    Vehicle leave(int slotId) throws InvalidSlotIdException;

    /**
     * @param registrationNumber The vehicle registration number
     * @return The slot where the vehicle is parked
     * @throws VehicleDoesNotExistException If the vehicle is not found in the system
     */
    int getSlotNumber(String registrationNumber) throws VehicleDoesNotExistException;

    /**
     * @param driverAge Input driver age
     * @return List of vehicle whose drivers age is equal to {@code driverAge}
     * The returned list is empty if no vehicles are found
     */
    List<String> getRegistrationNumbersWithDriverAge(int driverAge);

    /**
     * @param driverAge Input driver age
     * @return List of slots where drivers age is equal to {@code driverAge}
     * The returned list is empty if no vehicles are found
     */
    List<Integer> getSlotNumbersWithDriverAge(int driverAge);
}
