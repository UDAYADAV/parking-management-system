package org.squadstack.pms.database;

import org.squadstack.pms.entity.Vehicle;
import org.squadstack.pms.exception.InvalidSlotIdException;
import org.squadstack.pms.exception.NoSlotAvailableException;
import org.squadstack.pms.exception.ParkingLotAlreadyInitializedException;

import java.util.*;

public class InMemoryDatabase {
    private static Map<Integer, Vehicle> parkingLot;
    private static PriorityQueue<Integer> availableSlotIds;
    private static int parkingLotSize;

    private InMemoryDatabase() {
    }

    public static void initialize(int size) throws ParkingLotAlreadyInitializedException {
        if (InMemoryDatabase.parkingLot != null) {
            throw new ParkingLotAlreadyInitializedException();
        }
        InMemoryDatabase.parkingLotSize = size;
        InMemoryDatabase.parkingLot = new HashMap<>(size);
        InMemoryDatabase.availableSlotIds = new PriorityQueue<>(size);
        for (int i = 1; i <= size; i++) {
            InMemoryDatabase.availableSlotIds.add(i);
        }
    }

    public static Optional<Integer> getAvailableSlot() {
        return Optional.ofNullable(availableSlotIds.peek());
    }

    public static Vehicle insert(Vehicle vehicle) throws NoSlotAvailableException {
        vehicle.setSlotId(getAvailableSlot().orElseThrow(NoSlotAvailableException::new));
        parkingLot.put(vehicle.getSlotId(), vehicle);
        availableSlotIds.remove(vehicle.getSlotId());
        return vehicle;
    }

    public static Vehicle delete(int id) throws InvalidSlotIdException {
        if (id <= 0 || id > InMemoryDatabase.parkingLotSize) {
            throw new InvalidSlotIdException(String.format("Slot %d is out of range", id));
        }
        Vehicle vehicle = parkingLot.get(id);
        if (vehicle == null) {
            throw new InvalidSlotIdException(String.format("No vehicles parked at slot %d", id));
        }

        parkingLot.remove(id);
        availableSlotIds.add(id);
        return vehicle;
    }

    public static List<Vehicle> getAll() {
        return new ArrayList<>(parkingLot.values());
    }
}
