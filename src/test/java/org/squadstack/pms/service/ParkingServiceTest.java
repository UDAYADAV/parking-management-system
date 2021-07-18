package org.squadstack.pms.service;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.squadstack.pms.entity.Vehicle;
import org.squadstack.pms.exception.*;
import org.squadstack.pms.repository.SlotRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;


public class ParkingServiceTest {
    public static final String KA_01_HH_1234 = "KA-01-HH-1234";
    public static final String KA_01_HH_1235 = "KA-01-HH-1234";
    public static final String KA_01_HH_1236 = "KA-01-HH-1234";
    public static final String KA_01_HH_1237 = "KA-01-HH-1234";
    private SlotRepository slotRepository;
    private ParkingService parkingService;

    public ParkingServiceTest() {
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        slotRepository = Mockito.mock(SlotRepository.class);
        parkingService = new ParkingServiceImpl(slotRepository);
    }

    /**
     * Test that exception should be thrown if vehicle registration number is invalid
     */
    @Test
    public void issueTicketShouldThrowExceptionIfRegistrationNumberIsInvalid() throws TicketIssueException {
        when(slotRepository.getDetailsForRegistrationNumber("1234")).thenReturn(Optional.empty());
        expectedException.expect(TicketIssueException.class);
        expectedException.expectCause(CoreMatchers.isA(InvalidInputException.class));
        parkingService.issueTicket("1234", 24);
    }

    /**
     * Test that exception should be thrown if driver age is invalid
     */
    @Test
    public void issueTicketShouldThrowExceptionIfDriverAgeIsInvalid() throws TicketIssueException {
        when(slotRepository.getDetailsForRegistrationNumber(KA_01_HH_1234)).thenReturn(Optional.empty());
        expectedException.expect(TicketIssueException.class);
        expectedException.expectCause(CoreMatchers.isA(InvalidInputException.class));
        parkingService.issueTicket(KA_01_HH_1234, -1);
    }

    /**
     * Test that exception should be thrown if vehicle registration number is already parked
     */
    @Test
    public void issueTicketShouldThrowExceptionIfVehicleAlreadyExists() throws TicketIssueException, InvalidInputException {
        when(slotRepository.getDetailsForRegistrationNumber(KA_01_HH_1234)).thenReturn(
                Optional.of(new Vehicle(KA_01_HH_1234, 20)));
        expectedException.expect(TicketIssueException.class);
        expectedException.expectCause(CoreMatchers.isA(VehicleAlreadyParkedException.class));
        parkingService.issueTicket(KA_01_HH_1234, 20);
    }

    /**
     * Test that exception should be thrown if parking lot is full
     */
    @Test
    public void issueTicketShouldThrowExceptionIfParkingLotIsFull() throws TicketIssueException, NoSlotAvailableException {
        doThrow(new NoSlotAvailableException()).when(slotRepository).insert(any());
        expectedException.expect(TicketIssueException.class);
        expectedException.expectCause(CoreMatchers.isA(NoSlotAvailableException.class));
        parkingService.issueTicket(KA_01_HH_1234, 20);
    }

    /**
     * Test that slot details should be returned if a vehicle can be successfully parked
     */
    @Test
    public void issueTicketShouldCallRepository() throws TicketIssueException, NoSlotAvailableException {
        when(slotRepository.getDetailsForRegistrationNumber(KA_01_HH_1234)).thenReturn(
                Optional.empty());
        ArgumentCaptor<Vehicle> argumentCaptor = ArgumentCaptor.forClass(Vehicle.class);
        parkingService.issueTicket(KA_01_HH_1234, 20);

        verify(slotRepository, times(1)).insert(argumentCaptor.capture());
        Vehicle actual = argumentCaptor.getValue();
        assertThat(actual.getRegistrationNumber(), is(KA_01_HH_1234));
        assertThat(actual.getDriverAge(), is(20));
    }

    /**
     * Test that exception should be thrown if slotId is invalid
     */
    @Test
    public void leaveShouldThrowExceptionIfSlotIdCannotBeLeft() throws InvalidSlotIdException {
        doThrow(new InvalidSlotIdException("Test Exception Message")).when(slotRepository).delete(-1);
        expectedException.expect(InvalidSlotIdException.class);
        expectedException.expectMessage("Test Exception Message");
        parkingService.leave(-1);
    }

    /**
     * Test that details should be returned if slotId can be successfully left
     */
    @Test
    public void leaveShouldReturnSlotDetailsIfSuccessful() throws InvalidSlotIdException, InvalidInputException {
        Vehicle vehicle = new Vehicle(KA_01_HH_1234, 20);
        vehicle.setSlotId(5);
        doReturn(vehicle).when(slotRepository).delete(5);
        Vehicle actual = parkingService.leave(5);
        assertThat(actual.getDriverAge(), is(20));
        assertThat(actual.getSlotId(), is(5));
        assertThat(actual.getRegistrationNumber(), is(KA_01_HH_1234));
    }

    /**
     * Test that exception should be thrown if vehicle registration number is not found
     */
    @Test
    public void getSlotNumberShouldThrowExceptionIfRegistrationNumberIsNotFound() throws VehicleDoesNotExistException {
        doReturn(Optional.empty()).when(slotRepository).getDetailsForRegistrationNumber(KA_01_HH_1234);
        expectedException.expect(VehicleDoesNotExistException.class);
        parkingService.getSlotNumber(KA_01_HH_1234);
    }

    /**
     * Test that slotId is returned if vehicle registration number is found
     */
    @Test
    public void getSlotNumberShouldReturnSlotIdIfRegistrationNumberIsFoundFound() throws InvalidInputException, VehicleDoesNotExistException {
        Vehicle vehicle = new Vehicle(KA_01_HH_1234, 20);
        vehicle.setSlotId(5);
        doReturn(Optional.of(vehicle)).when(slotRepository).getDetailsForRegistrationNumber(KA_01_HH_1234);
        int actual = parkingService.getSlotNumber(KA_01_HH_1234);
        assertThat(actual, is(5));
    }

    /**
     * Test that no details should be returned if drivers of age are not found
     */
    @Test
    public void getRegistrationNumbersWithDriverAgeShouldReturnEmptyListIfNoVehiclesAreNotFound() throws InvalidInputException {
//        doReturn(new ArrayList<Vehicle>()).when(slotRepository).getDetailsForDriverAge(20);
        List<String> actual = parkingService.getRegistrationNumbersWithDriverAge(21);
        assertThat(actual.size(), is(0));
    }

    /**
     * Test that details should be returned if drivers of age are found
     */
    @Test
    public void getRegistrationNumbersWithDriverAgeShouldReturnListIfVehiclesAreFound() throws InvalidInputException {
        ArrayList<Vehicle> found = new ArrayList<>();
        found.add(new Vehicle(KA_01_HH_1234, 21));
        found.add(new Vehicle(KA_01_HH_1235, 21));
        found.add(new Vehicle(KA_01_HH_1236, 21));
        found.add(new Vehicle(KA_01_HH_1237, 21));
        for (int i = 0; i < found.size(); i++) {
            found.get(i).setSlotId(i + 1);
        }
        doReturn(found).when(slotRepository).getDetailsForDriverAge(21);
        List<String> actual = parkingService.getRegistrationNumbersWithDriverAge(21);
        assertThat(actual.size(), is(4));

        assertThat(actual, hasItems(KA_01_HH_1234
                , KA_01_HH_1235
                , KA_01_HH_1236
                , KA_01_HH_1237));
    }

    /**
     * Test that no details should be returned if drivers of age are not found
     */
    @Test
    public void getSlotNumbersWithDriverAgeShouldShouldReturnEmptyListIfNoVehiclesAreNotFound() throws InvalidInputException {
        doReturn(new ArrayList<Vehicle>()).when(slotRepository).getDetailsForDriverAge(20);
        List<Integer> actual = parkingService.getSlotNumbersWithDriverAge(20);
        assertThat(actual.size(), is(0));
    }

    /**
     * Test that details should be returned if drivers of age are found
     */
    @Test
    public void getSlotNumbersWithDriverAgeShouldShouldReturnListIfVehiclesAreFound() throws InvalidInputException {
        ArrayList<Vehicle> found = new ArrayList<>();
        found.add(new Vehicle(KA_01_HH_1234, 20));
        found.add(new Vehicle(KA_01_HH_1235, 20));
        found.add(new Vehicle(KA_01_HH_1236, 20));
        found.add(new Vehicle(KA_01_HH_1237, 20));
        for (int i = 0; i < found.size(); i++) {
            found.get(i).setSlotId(i + 1);
        }
        doReturn(found).when(slotRepository).getDetailsForDriverAge(20);
        List<Integer> actual = parkingService.getSlotNumbersWithDriverAge(20);
        assertThat(actual.size(), is(4));
        assertThat(actual, hasItems(1
                , 2
                , 3
                , 4));
    }
}