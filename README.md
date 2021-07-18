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

![Class Diagram](https://github.com/UDAYADAV/parking-management-system/blob/master/docs/ParkingServiceClassDiagram.png)
