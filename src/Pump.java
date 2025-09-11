import java.io.IOException;

public class Pump {
    // This is going to be True = on, False = off
    private boolean pumping;
    private final ioPort api;

    /**
     * Constructor to initialize the pump device.
     * @param deviceType Type of the device to choose
     * @param connector Connector/port number to connect the device
     * @throws IOException if the device initialization fails
     */
    public Pump(int deviceType, int connector) throws IOException {
        this.pumping = false;
        this.api = ioPort.ChooseDevice(deviceType);
        api.ioport(connector);
        System.out.println("Pump is connected on port: " + connector);
    }

    /**
     * updates the pump sensor status and sends messages ti the API.
     * @param sensorPumping boolean that indicates if pump is on or off
     */
    public void updateSensor(boolean sensorPumping) {
        if (sensorPumping && !pumping) {
            pumping = true;
            System.out.println("Pump is ON - pumping gas");
            api.send("Pump ON");
        } else if (!sensorPumping && pumping) {
            pumping = false;
            System.out.println("Pump is OFF - not pumping");
            api.send("Pump OFF");
        }
    }
}
