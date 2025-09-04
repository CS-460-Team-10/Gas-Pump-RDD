

public class CreditCardReader {
    ioPort api;

    public CreditCardReader(int deviceType, int connector) throws Exception {
        api = ioPort.ChooseDevice(deviceType);  
        api.ioport(connector);         
        while (true) {
            String msg = api.get();
            if (msg != null) {
                boolean authorized = readCard(msg);
                if (authorized) {
                    api.send(msg + " Authorized.");
                } else {
                    api.send(msg + " Unauthorized.");
                }
            }
        }
    }

    private static boolean readCard(String cardNumber) {
        return cardNumber != null && !cardNumber.isEmpty();
    }

    public static void main(String[] args) throws Exception {
        new CreditCardReader(1, 3);
    }
}
