
// this is the bank server class
// only role is to get from hub authorize and charge request and reply back with Approved/declined or charged/charge-failed
public class TheBank {
    ioPort api; // communcation api

    public TheBank(int deviceType, int connector) throws Exception {
        // open the device on port
        api = ioPort.ChooseDevice(deviceType);
        api.ioport(connector);
        // start the loop for bank logic
        authorization();
    }

    public void authorization() throws Exception {
        // main loop keep running
        while (true) {
            // get messages from hub
            String msg = api.get();
            if (msg != null) {
                System.out.println("Bank Received: " + msg);

                // if hub ask to authorize card
                if (msg.startsWith("authorize")) {
                    api.send("approved");
                    System.out.println("Bank approved");
                } 
                // if hub ask to charge the card with money amount
                else if (msg.startsWith("charge-request")) {
                    String inside = msg.substring(msg.indexOf("(") + 1, msg.length() - 1);
                    String[] prts = inside.split(",");
                    if (prts.length == 2) {
                        String amount = prts[1].trim();
                        if (!amount.equals("0")) {
                            api.send("charged(" + amount + ")");
                            System.out.println("Bank charged " + amount);
                        } else {
                            api.send("charge-failed");
                            System.out.println("Bank charge-failed");
                        }
                    }
                }
            }
            Thread.sleep(50); // small delay not to spin
        }
    }

    public static void main(String[] args) throws Exception {
        // start the bank on connector 5 status port
        new TheBank(3, 5);
    }
}

