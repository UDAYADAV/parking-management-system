package org.squadstack.pms.service;

import org.squadstack.pms.entity.Vehicle;
import org.squadstack.pms.exception.*;
import org.squadstack.pms.repository.SlotRepository;

import java.util.List;
import java.util.stream.Collectors;

public class ParkingServiceImpl implements ParkingService {
    private final SlotRepository slotRepository;

    public ParkingServiceImpl(SlotRepository slotRepository) {
        this.slotRepository = slotRepository;
    }

    @Override
    public Vehicle issueTicket(String registrationNumber, int driverAge) throws TicketIssueException {
        try {
            checkIfVehicleAlreadyExists(registrationNumber);
            return slotRepository.insert(new Vehicle(registrationNumber, driverAge));
        } catch (InvalidInputException e) {
            throw new TicketIssueException(
                    String.format("Registration Number (%s) or Driver Age (%d) is Invalid", registrationNumber, driverAge), e);
        } catch (VehicleAlreadyParkedException e) {
            throw new TicketIssueException(String.format("Vehicle (%s) is already parked", registrationNumber), e);
        } catch (NoSlotAvailableException e) {
            throw new TicketIssueException("No available Slots", e);
        }
    }

    @Override
    public Vehicle leave(int slotId) throws InvalidSlotIdException {
        return slotRepository.delete(slotId);
    }

    @Override
    public int getSlotNumber(String registrationNumber) throws VehicleDoesNotExistException {
        return slotRepository.getDetailsForRegistrationNumber(registrationNumber)
                .orElseThrow(() -> new VehicleDoesNotExistException(String.format("Vehicle %s does not exist", registrationNumber)))
                .getSlotId();
    }

    @Override
    public List<String> getRegistrationNumbersWithDriverAge(int driverAge) {
        return slotRepository.getDetailsForDriverAge(driverAge)
                .stream()
                .map(Vehicle::getRegistrationNumber)
                .collect(Collectors.toList());
    }

    @Override
    public List<Integer> getSlotNumbersWithDriverAge(int driverAge) {
        return slotRepository.getDetailsForDriverAge(driverAge)
                .stream()
                .map(Vehicle::getSlotId)
                .collect(Collectors.toList());
    }

    private void checkIfVehicleAlreadyExists(String registrationNumber) throws VehicleAlreadyParkedException {
        if (slotRepository.getDetailsForRegistrationNumber(registrationNumber).isPresent()) {
            throw new VehicleAlreadyParkedException(String.format("Vehicle (%s) is already parked", registrationNumber));
        }
    }
}
