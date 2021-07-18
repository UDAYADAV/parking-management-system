package org.squadstack.pms.processor;

import org.squadstack.pms.repository.SlotRepositoryImpl;
import org.squadstack.pms.service.ParkingService;
import org.squadstack.pms.service.ParkingServiceImpl;

import java.util.Arrays;
import java.util.List;

public class VehicleRegistrationNumberForDriverOfAgeProcessor implements CommandProcessor {
    private final ParkingService parkingService;

    public VehicleRegistrationNumberForDriverOfAgeProcessor() {
        this.parkingService = new ParkingServiceImpl(new SlotRepositoryImpl());
    }

    @Override
    public void process(String inputString) {
        List<String> stringList = Arrays.asList(inputString.split(" "));
        int driverAge = Integer.parseInt(stringList.get(1));
        List<String> registrationNumbersWithDriverAge = parkingService.getRegistrationNumbersWithDriverAge(driverAge);
        if (registrationNumbersWithDriverAge.isEmpty()) {
            System.out.println("No parked car matches the query");
        } else {
            System.out.println(registrationNumbersWithDriverAge);
        }
    }
}
