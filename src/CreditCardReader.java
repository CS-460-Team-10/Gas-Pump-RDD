import java.util.Scanner;

/* 
 * Looks pretty good. The unknownhost error might an issue with ioPort. Hopefully will fix it by fixing ioPort.
 * 
 * -Alex
*/

public class CreditCardReader {
    private ioPortAPI api;

    /**
     * CreditCardReader just connects the card reader to the main controller.
     * @param connector is the port #
     */
    public CreditCardReader(int connector) {
        api = new ioPortAPI();
        api.ioport(connector);
    }

    /**
     * ReadCard just checks if it retrieved a card# and sends it to main
     * @param cardNumber is the card# received from card reader.
     */
    public void readCard(String cardNumber) {
        if (cardNumber != null && !cardNumber.isEmpty()) {
            api.send("Card read: " + cardNumber);
        }
    }
}
