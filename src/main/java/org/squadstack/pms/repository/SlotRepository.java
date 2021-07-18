package org.squadstack.pms.repository;

import org.squadstack.pms.entity.Vehicle;
import org.squadstack.pms.exception.InvalidSlotIdException;
import org.squadstack.pms.exception.NoSlotAvailableException;

import java.util.List;
import java.util.Optional;

public interface SlotRepository {

    Vehicle insert(Vehicle vehicle) throws NoSlotAvailableException;

    Vehicle delete(int slotId) throws InvalidSlotIdException;

    Optional<Vehicle> getDetailsForRegistrationNumber(String registrationNumber);

    List<Vehicle> getDetailsForDriverAge(int driverAge);
}
