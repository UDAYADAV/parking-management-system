package org.squadstack.pms.repository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.squadstack.pms.database.InMemoryDatabase;
import org.squadstack.pms.entity.Vehicle;
import org.squadstack.pms.exception.InvalidInputException;
import org.squadstack.pms.exception.InvalidSlotIdException;
import org.squadstack.pms.exception.NoSlotAvailableException;
import org.squadstack.pms.exception.ParkingLotAlreadyInitializedException;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SlotRepositoryTest {
    private SlotRepository slotRepository;
    public static final String KA_01_HH_1234 = "KA-01-HH-1234";
    public static final String KA_01_HH_1235 = "KA-01-HH-1235";
    public static final String KA_01_HH_1236 = "KA-01-HH-1236";

    @Before
    public void setUp() throws Exception {
        Field field = InMemoryDatabase.class.getDeclaredField("parkingLot");
        field.setAccessible(true);
        field.set(null, null);

        slotRepository = new SlotRepositoryImpl();
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    /**
     * Test that the vehicles should be inserted in the available parking lots
     */
    @Test
    public void insertShouldAssignTheParkingLot() throws ParkingLotAlreadyInitializedException, InvalidInputException, NoSlotAvailableException {
        InMemoryDatabase.initialize(3);
        slotRepository.insert(new Vehicle(KA_01_HH_1234, 20));
        slotRepository.insert(new Vehicle(KA_01_HH_1235, 20));
        List<Vehicle> actual = InMemoryDatabase.getAll();
        assertThat(actual.stream().map(Vehicle::getRegistrationNumber).collect(Collectors.toList()),
                hasItems(KA_01_HH_1234, KA_01_HH_1235));
    }

    /**
     * Test that NoSlotAvailableException should be thrown if slots are full
     */
    @Test
    public void ExceptionShouldBeThrownIfNoSlotsAreAvailable() throws ParkingLotAlreadyInitializedException, InvalidInputException, NoSlotAvailableException {
        InMemoryDatabase.initialize(1);
        slotRepository.insert(new Vehicle(KA_01_HH_1234, 20));
        expectedException.expect(NoSlotAvailableException.class);
        slotRepository.insert(new Vehicle(KA_01_HH_1235, 20));
    }

    /**
     * Test that InvalidSlotIdException should be thrown if slotId is invalid
     */
    @Test
    public void deleteShouldThrowExceptionIfSlotIdIsOutOfBound() throws ParkingLotAlreadyInitializedException, InvalidSlotIdException {
        InMemoryDatabase.initialize(3);
        expectedException.expect(InvalidSlotIdException.class);
        expectedException.expectMessage("Slot 5 is out of range");
        slotRepository.delete(5);
    }

    /**
     * Test that InvalidSlotIdException should be thrown if slotId does not have a vehicle parked
     */
    @Test
    public void deleteShouldThrowExceptionIfSlotIdIsEmpty() throws ParkingLotAlreadyInitializedException, InvalidSlotIdException {
        InMemoryDatabase.initialize(3);
        expectedException.expect(InvalidSlotIdException.class);
        expectedException.expectMessage("No vehicles parked at slot 1");
        slotRepository.delete(1);
    }

    /**
     * Test that Vehicle details is returned if a slot is successfully left
     */
    @Test
    public void deleteShouldReturnSlotDetailsIfSuccessfullyDeleted() throws ParkingLotAlreadyInitializedException, InvalidInputException, InvalidSlotIdException, NoSlotAvailableException {
        InMemoryDatabase.initialize(3);
        InMemoryDatabase.insert(new Vehicle(KA_01_HH_1234, 20));

        Vehicle actual = slotRepository.delete(1);
        assertThat(actual.getSlotId(), is(1));
        assertThat(actual.getRegistrationNumber(), is(KA_01_HH_1234));
        assertThat(actual.getDriverAge(), is(20));
    }

    /**
     * Test that No details should be returned if vehicle registration number is not found
     */
    @Test
    public void getDetailsForRegistrationNumberShouldReturnEmptyIfNotFound() throws ParkingLotAlreadyInitializedException {
        InMemoryDatabase.initialize(3);
        Optional<Vehicle> actual = slotRepository.getDetailsForRegistrationNumber(KA_01_HH_1234);
        assertThat(actual.isPresent(), is(false));
    }

    /**
     * Test that vehicle details should be returned if vehicle registration number is found
     */
    @Test
    public void getDetailsForRegistrationNumberShouldReturnVehicleDetailsIFound() throws ParkingLotAlreadyInitializedException, InvalidInputException, NoSlotAvailableException {
        InMemoryDatabase.initialize(3);
        InMemoryDatabase.insert(new Vehicle(KA_01_HH_1234, 20));

        Optional<Vehicle> actual = slotRepository.getDetailsForRegistrationNumber(KA_01_HH_1234);
        assertThat(actual.isPresent(), is(true));
        assertThat(actual.get().getRegistrationNumber(), is(KA_01_HH_1234));
        assertThat(actual.get().getSlotId(), is(1));
        assertThat(actual.get().getDriverAge(), is(20));
    }

    /**
     * Test that No details should be returned if no entry with driver age is found
     */
    @Test
    public void getDetailsForDriverAgeShouldReturnEmptyINotFound() throws ParkingLotAlreadyInitializedException {
        InMemoryDatabase.initialize(3);
        List<Vehicle> actual = slotRepository.getDetailsForDriverAge(20);
        assertThat(actual.isEmpty(), is(true));
    }

    /**
     * Test that details should be returned if entries with driver age is found
     */
    @Test
    public void getDetailsForDriverAgeShouldReturnIfFound() throws ParkingLotAlreadyInitializedException, InvalidInputException, NoSlotAvailableException {
        InMemoryDatabase.initialize(3);
        InMemoryDatabase.insert(new Vehicle(KA_01_HH_1234, 20));
        InMemoryDatabase.insert(new Vehicle(KA_01_HH_1235, 21));
        InMemoryDatabase.insert(new Vehicle(KA_01_HH_1236, 20));
        List<Vehicle> actual = slotRepository.getDetailsForDriverAge(20);
        assertThat(actual.stream().map(Vehicle::getRegistrationNumber).collect(Collectors.toList()),
                hasItems(KA_01_HH_1234, KA_01_HH_1236));
        assertThat(actual.stream().map(Vehicle::getSlotId).collect(Collectors.toList()),
                hasItems(1, 3));
    }
}