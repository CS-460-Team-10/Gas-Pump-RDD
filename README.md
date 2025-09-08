# Gas Pump Simulation Project

## Overview
This project simulates a collection of hardware components (screen, hub, hose, flowmeter, I/O ports, etc.) and utilities (image loader, credit card reader, image sorting). It demonstrates object-oriented design in Java while integrating multiple device-like classes into a cohesive system.  

## Project Structure
- **`imageLoader.java`** – Utility class for loading and processing images.  
- **`CreditCardReader.java`** – Simulates a credit card reader device.  
- **`Flowmeter.java`** – Models a flowmeter for measuring liquid/gas flow.  
- **`Hose.java`** – Represents a hose object connected to the system.  
- **`hub.java`** – Central hub for connecting and managing multiple devices.  
- **`ioPort.java`** – Simulates input/output ports for device communication.  
- **`screen.java`** – Models a screen for displaying information.  

## Requirements
- **Java 17+**
- **JavaFX**

## How to Compile & Run
1. Clone/download this repository.  
2. Open a terminal in the project root.  
3. Compile all Java files:  
   ```bash
   javac *.java
   ```
4. Run the test program to ensure image loading order is correct (example with `testImageSort`):  
   ```bash
   java testImageSort
   ```
