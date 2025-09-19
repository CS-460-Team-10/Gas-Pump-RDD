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
     * this is the action that will be used for gas pump controller to
     * that will turn on the pump.
     */
    public synchronized void pumpOn() {
        if(!pumping) {
            pumping = true;
            System.out.println("Pump is On - pumping gas");
            api.send("Pump ON");
        }
    }

    /**
     * Action that will be used for gas pump controller
     * when the gas pump is off
     */
    public synchronized void pumpOff() {
        if (pumping) {
            pumping = false;
            System.out.println("Pump is OFF - not pumping");
            api.send("Pump OFF");
        }
    }

    /**
     * updates the pump sensor status and sends messages ti the API.
     * @param sensorPumping boolean that indicates if pump is on or off
     */
    public void updateSensor(boolean sensorPumping) {
        if (sensorPumping && !pumping) {
            pumpOn();
        } else if (!sensorPumping && pumping) {
            pumpOff();
        }
    }

    public boolean isPumping() {
        return pumping;
    }
}
