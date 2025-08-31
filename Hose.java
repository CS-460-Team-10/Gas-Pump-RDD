public class Hose {
    private boolean attached;
    private final ioPortAPI api;

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
    public void updateSenor(boolean sensorAttached) {
        if (sensorAttached && !attached) {
            attached = true;
            api.send("Hose attached");
            System.out.println("Hose attached message sent to API");
        } else if (!sensorAttached && attached) {
            attached = false;
            api.send("Hose detached");
            System.out.println("Hose detached message sent to API");
        }
    }

    public boolean isAttached() {
        return attached;
    }
}