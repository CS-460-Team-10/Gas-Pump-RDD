public class main {
    private final ioPort api = ioPort.ChooseDevice(1);

    public static void main(String[] args) {
        main m = new main();

        String thing = "t01/s1B/f1/c4/\"Welcome!\":t23/s3R/f1/c4/\"Use the card reader to begin your transaction.\":t45/s1B/f1/c4/\"*\""; // Welcome screen

        while(true){
            m.sendData(thing);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        }
    }

    private void sendData(String thing){
        api.send(thing);
    }
}
