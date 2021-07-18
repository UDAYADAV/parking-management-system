package org.squadstack.pms.processor;

import org.squadstack.pms.entity.Vehicle;
import org.squadstack.pms.exception.TicketIssueException;
import org.squadstack.pms.repository.SlotRepositoryImpl;
import org.squadstack.pms.service.ParkingService;
import org.squadstack.pms.service.ParkingServiceImpl;

import java.util.Arrays;
import java.util.List;

public class TicketIssueProcessor implements CommandProcessor {
    private final ParkingService parkingService;

    public TicketIssueProcessor() {
        this.parkingService = new ParkingServiceImpl(new SlotRepositoryImpl());
    }

    @Override
    public void process(String inputString) {
        List<String> stringList = Arrays.asList(inputString.split(" "));
        String registrationNumber = stringList.get(1);
        int driverAge = Integer.parseInt(stringList.get(3));
        try {
            Vehicle vehicle = parkingService.issueTicket(registrationNumber, driverAge);
            System.out.printf("Car with vehicle registration number \"%s\" has been parked at slot number %d%n", vehicle.getRegistrationNumber(), vehicle.getSlotId());
        } catch (TicketIssueException e) {
            System.out.println("Cannot park vehicle with registration number: " + registrationNumber);
        }
    }
}
