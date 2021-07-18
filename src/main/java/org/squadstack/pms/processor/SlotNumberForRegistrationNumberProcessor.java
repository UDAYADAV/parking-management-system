package org.squadstack.pms.processor;

import org.squadstack.pms.exception.VehicleDoesNotExistException;
import org.squadstack.pms.repository.SlotRepositoryImpl;
import org.squadstack.pms.service.ParkingService;
import org.squadstack.pms.service.ParkingServiceImpl;

import java.util.Arrays;
import java.util.List;

public class SlotNumberForRegistrationNumberProcessor implements CommandProcessor {
    private final ParkingService parkingService;

    public SlotNumberForRegistrationNumberProcessor() {
        this.parkingService = new ParkingServiceImpl(new SlotRepositoryImpl());
    }

    @Override
    public void process(String inputString) {
        List<String> stringList = Arrays.asList(inputString.split(" "));
        try {
            System.out.println(parkingService.getSlotNumber(stringList.get(1)));
        } catch (VehicleDoesNotExistException e) {
            System.out.println("No parked car matches the query");
        }
    }
}
