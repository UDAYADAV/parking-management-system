package org.squadstack.pms.processor;

import org.squadstack.pms.repository.SlotRepositoryImpl;
import org.squadstack.pms.service.ParkingService;
import org.squadstack.pms.service.ParkingServiceImpl;

import java.util.Arrays;
import java.util.List;

public class SlotNumbersForDriverAgeProcessor implements CommandProcessor {
    private final ParkingService parkingService;

    public SlotNumbersForDriverAgeProcessor() {
        this.parkingService = new ParkingServiceImpl(new SlotRepositoryImpl());
    }

    @Override
    public void process(String inputString) {
        List<String> stringList = Arrays.asList(inputString.split(" "));
        int driverAge = Integer.parseInt(stringList.get(1));
        List<Integer> slotNumbers = parkingService.getSlotNumbersWithDriverAge(driverAge);

        if (slotNumbers.isEmpty()) {
            System.out.println("No parked car matches the query");
        } else {
            System.out.println(slotNumbers);
        }
    }
}
