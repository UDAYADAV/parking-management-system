## Building & Running

This project using Maven as build tool as such it needs to be installed.

* Installing Maven
    * Prerequisite:
        * Ensure Java is installed and is in the classpath.
    * Installing Maven on Ubuntu
        * Run `apt-cache search maven` to get all available maven packages
        * Run `sudo apt-get install maven` to install maven
        * Run `mvn -version` to verify that maven is installed
    * Installing Maven on macOS
        * Ensure you have homebrew installed
        * Run `brew install maven` to install maven
        * Run `mvn -version` to verify that maven is installed

* Building
    * Go to the root of the repository
    * Run `mvn clean package`. It will generate a `target` folder
        * Open `target/site/jacoco/index.html` to view coverage report
        * From inside target directory
          Run `java -Dinput.file.path="PATH_TO_INPUT_FILE" -jar ./parking-management-system-1.0-SNAPSHOT.jar` to run the
          program

## Generating Javadocs
* Run `mvn javadoc:javadoc` from project root
* Open `target/site/apidocs/index.html`
* Click on `org.squadstack.pms.service` in package
* Click on `ParkingService` interface

## Design Docs

### InMemoryDatabase
* Static parkingLot : To hold all the vehicles that are currently in parked 
* Static availableSlotIds : to hold all the available parking slots - This is a priority queue and getting the next available slot is O(1) as opposed to scanning in a List whcih takes O(N). Insertion/deletion takes O(LogN). Whenever a new car is to be parked, We first get the availble slot from availableSlotIds, the details is then inserted in parkingLot and the alloted slotId is removed from availableSlotIds to denote that the slot is not available. Similarly, When a car leaves, the entry is removed from parkingLot and the slotId is inserted in availableSlotIds to mark it as available.

The class exposes below methods:

1. initialize(int lotSize) -> This initialized the parking lot of size lotSize and marks all slot Ids as available by inserting them in the availableSlotIds. The next attempt to initialize will throw an ParkingLotAlreadyInitializedException.
2. insert(Vehicle vehicle) -> This inserts the vehicle in the parkingLot in the nearest available slot. The slot is then removed from availableSlotIds to denote that it is not free. If no slots are available, NoSlotAvailableException is thrown.
3. delete(int id) -> Deleted the entry for slotId id. It removes the entry from parkingLot and inserts the slotId in availableSlotIds to denote it's now available. If the supplied slot id is not valid (<=0 OR more than the parkinglot size), or, the slotId doesnot have any vehicle parked, InvalidSlotIdException is thrown.
4. getAll() -> Returns all the list of vehicles currently parked. The caller can filter this as per the requirement.

### SlotRepository
This class provides an abstraction over the DB. Additional functionalities like getting details of particular aged drivers are provided by this. It calls the DB to update/query.

The class exposes below methods:

1. insert(Vehicle vehicle) -> Tries to Inserts the vehicle in the DB. The DB layer will return the slotId that it has assigned. If no slots are availble, NoSlotAvailableException is thrown for the caller to handle.
2. Vehicle delete(int slotId) -> Tries to delete the vehicle at slotId. If the DB cannot freeup the slotId, InvalidSlotIdException is thrown for the caller to handle.
3. Optional<Vehicle> getDetailsForRegistrationNumber(String registrationNumber) -> Calls InMemoryDatabase.getAll() and filters the result to return the details of registration number. The Optional return type signifies that the result of this function may/,aynot exist. The return type has been made Optional to avoid returning null in cases where the registration number is not found. If the registration number is found, the details is wrapped in Optional and returned.
4. List<Vehicle> getDetailsForDriverAge(int driverAge)  -> Calls InMemoryDatabase.getAll() and filters the result to return the details of driver age. Since, the there could be multiple entries for a particular age, a list of Vehicle is returned. If no results are found, the returned list is empty(not null). All other abstraction like getting slots ids & registration nummber for a age group is built on top of this api.

### ParkingService

This is the business API which delegates to SlotRepository for any DB related operations.

This class expose below methods:

1. issueTicket(String registrationNumber, int driverAge) -> It first checkes if registrationNumber is alredy present in the DB, if yes TicketIssueException is thrown, the exception message is appropriately populated. It then creates a Vehicle object with registrationNumber and driverAge and passes to SlotRepository to insert in the DB. If the operation is sucessfull, Vehicle details & slotId is returned to the caller. If the operation fails, appropriate message wrapped inside TicketIssueException is thrown.
2. leave(int slotId) -> Calls the SlotRepository to free up the slotId. If operation fails, InvalidSlotIdException is thrown else the details are returned to the caller.
3. getSlotNumber(String registrationNumber) -> Calls SlotRepository getDetailsForDriverAge and filters for registrationNumber. If result is found, it is returned, else VehicleDoesNotExistException is thrown to the caller top notify of failure.
4. List<String> getRegistrationNumbersWithDriverAge(int driverAge) -> Calls SlotRepository getDetailsForDriverAge and filters on the driverAge. Extracts the registation number if the vehicle and returns the result or empty list if not found.
5. List<Integer> getSlotNumbersWithDriverAge -> Calls SlotRepository getDetailsForDriverAge and filters on the driverAge. Extracts the slotId and returns the result or empty list if not found.

### Processor Package:

* CommandProcessor interface is created to handle how each commands are processed. Each valid command implements the CommandProcessor interface and overrides the process() method. All of these processors are inserted in a Map with command as the key. This approach helps get rid of multiple conditional statements and each processor can modified independently. In the future, if there is a need of adding additional command, all We need to do is add a implementation for that command without modifying anything else.
* CommandProcessor implementation just does the input conversion an delegates the calls to ParkingService for the business logic.
* ProducerFactory : Returns the appropriate implementation of the CommandProcessor based on the command being passed.

![Class Diagram](https://github.com/UDAYADAV/parking-management-system/blob/master/docs/ParkingServiceClassDiagram.png)
