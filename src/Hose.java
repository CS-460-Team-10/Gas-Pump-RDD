

/* 
 * Looks pretty good. The unknownhost error might an issue with ioPort. Hopefully will fix it by fixing ioPort.
 * 
 * -Alex
*/

public class Hose {
    private boolean attached;
    private final ioPortAPI api;
    private boolean tankFull;

    /**
     * Hose has a boolean just stating that It's attached or not
     * @param connector is just the port #
     */
    public Hose(int connector) {
        this.attached = false;
        this.api = new ioPortAPI();
        this.api.ioport(connector);
    }

    /**
     * updateSenor just send a message to the API whether the sensor
     * is attached or not to the gas tank.
     * @param sensorAttached is boolean sent by the sensor telling the
     *                       status of the hose.
     */
    public void updateSenor(boolean sensorAttached, boolean sensorTankFull) {
        // checks if the hose is attached
        if (sensorAttached && !attached) {
            attached = true;
            api.send("Hose attached");
            System.out.println("Hose attached message sent to API");
        } else if (!sensorAttached && attached) {
            attached = false;
            api.send("Hose detached");
            System.out.println("Hose detached message sent to API");
        }
        // checks if the tank is full
        if (sensorTankFull && !tankFull) {
            tankFull = true;
            api.send("Tank Full");
            System.out.println("Tank Full message sent to API");
        } else if (!sensorTankFull && tankFull) {
            tankFull = false;
            api.send("Tank is not full");
            System.out.println("Tank is not full message was sent to API");
        }
    }

    public boolean isAttached() {
        return attached;
    }

    public boolean isTankFull() {
        return tankFull;
    }
}