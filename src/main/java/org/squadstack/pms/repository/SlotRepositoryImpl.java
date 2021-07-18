package org.squadstack.pms.repository;

import org.squadstack.pms.database.InMemoryDatabase;
import org.squadstack.pms.entity.Vehicle;
import org.squadstack.pms.exception.InvalidSlotIdException;
import org.squadstack.pms.exception.NoSlotAvailableException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SlotRepositoryImpl implements SlotRepository {

    @Override
    public Vehicle insert(Vehicle vehicle) throws NoSlotAvailableException {
        return InMemoryDatabase.insert(vehicle);
    }

    @Override
    public Vehicle delete(int slotId) throws InvalidSlotIdException {
        return InMemoryDatabase.delete(slotId);
    }

    @Override
    public Optional<Vehicle> getDetailsForRegistrationNumber(String registrationNumber) {
        return InMemoryDatabase.getAll()
                .stream()
                .filter(vehicle -> vehicle.getRegistrationNumber().equals(registrationNumber))
                .findFirst();
    }

    @Override
    public List<Vehicle> getDetailsForDriverAge(int driverAge) {
        return InMemoryDatabase.getAll()
                .stream()
                .filter(vehicle -> vehicle.getDriverAge() == driverAge)
                .collect(Collectors.toList());
    }
}
