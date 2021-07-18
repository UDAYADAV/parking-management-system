package org.squadstack.pms.processor;

import org.squadstack.pms.entity.Vehicle;
import org.squadstack.pms.exception.InvalidSlotIdException;
import org.squadstack.pms.repository.SlotRepositoryImpl;
import org.squadstack.pms.service.ParkingService;
import org.squadstack.pms.service.ParkingServiceImpl;

import java.util.Arrays;
import java.util.List;

public class ParkingLotExitProcessor implements CommandProcessor {
    private final ParkingService parkingService;

    public ParkingLotExitProcessor() {
        this.parkingService = new ParkingServiceImpl(new SlotRepositoryImpl());
    }

    @Override
    public void process(String inputString) {
        List<String> stringList = Arrays.asList(inputString.split(" "));
        int slotId = Integer.parseInt(stringList.get(1));
        try {
            Vehicle vehicle = parkingService.leave(slotId);
            System.out.printf("Slot number %d vacated, the car with vehicle registration number \"%s\" left the space, the driver of the car was of age %d%n",
                    vehicle.getSlotId(), vehicle.getRegistrationNumber(), vehicle.getDriverAge());
        } catch (InvalidSlotIdException e) {
            System.out.printf("Invalid Slot %d%n", slotId);
        }
    }
}
