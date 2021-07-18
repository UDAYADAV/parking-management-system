package org.squadstack.pms.processor;

import java.util.HashMap;
import java.util.Map;

public class ProcessorFactory {
    private static final Map<String, CommandProcessor> commandProcessorMap = new HashMap<>();

    static {
        commandProcessorMap.put("Create_parking_lot", new ParkingInitializeProcessor());
        commandProcessorMap.put("Park", new TicketIssueProcessor());
        commandProcessorMap.put("Slot_numbers_for_driver_of_age", new SlotNumbersForDriverAgeProcessor());
        commandProcessorMap.put("Slot_number_for_car_with_number", new SlotNumberForRegistrationNumberProcessor());
        commandProcessorMap.put("Leave", new ParkingLotExitProcessor());
        commandProcessorMap.put("Vehicle_registration_number_for_driver_of_age", new VehicleRegistrationNumberForDriverOfAgeProcessor());
    }

    private ProcessorFactory() {
    }

    public static CommandProcessor getProcessor(String command) {
        return ProcessorFactory.commandProcessorMap.computeIfAbsent(command, s -> new DefaultCommandProcessor());
    }
}
