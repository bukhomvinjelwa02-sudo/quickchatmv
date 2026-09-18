package progassmentcom.quickchatmv;


import java.util.Random;
import java.util.ArrayList;

public class Message {

    private final int messageNumber;
    private final String recipientCell;
    private final String messagePayload;
    private final String messageID;
    private String messageHash;
    private boolean messageSent;

    private static final ArrayList<String> sentMessages = new ArrayList<>();
    private static final ArrayList<String> storedMessages = new ArrayList<>();
    private static final ArrayList<String> disregardedMessages = new ArrayList<>();

    public Message(int msgNumber, String recipient, String payload) {
        this.messageNumber = msgNumber;
        this.recipientCell = recipient;
        this.messagePayload = payload;
        this.messageSent = false;
        this.messageID = generateRandomID();
    }

    // picks 10 random digits and joins them into the message id
    private String generateRandomID() {
        StringBuilder id = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            id.append(rand.nextInt(10));
        }
        return id.toString();
    }

    // id must be exactly 10 digits
    public boolean checkMessageID() {
        return messageID != null && messageID.length() == 10 && messageID.matches("[0-9]+");
    }

    // number must start with + and have 10 to 12 digits after it
    public String checkRecipientCell() {
        if (recipientCell == null || recipientCell.isEmpty()) {
            return "Cell phone number is incorrectly formatted or does not contain an "
                    + "international code. Please correct the number and try again.";
        }

        boolean isValid = recipientCell.matches("^\\+\\d{10,12}$");

        return isValid ? "Cell phone number successfully captured."
                : "Cell phone number is incorrectly formatted or does not contain an "
                + "international code. Please correct the number and try again.";
    }

    // builds a hash like 12:1:HITONIGHT from the id, message number and first/last words
    public String createMessageHash() {
        if (messageID == null || messagePayload == null) {
            return "ERROR";
        }

        String idPrefix = messageID.substring(0, Math.min(2, messageID.length()));
        String[] words = messagePayload.trim().split("\\s+");

        String firstWord = (words.length > 0 ? words[0] : "").replaceAll("[^a-zA-Z0-9]", "");
        String lastWord = (words.length > 0 ? words[words.length - 1] : "").replaceAll("[^a-zA-Z0-9]", "");

        messageHash = idPrefix + ":" + messageNumber + ":" + (firstWord + lastWord).toUpperCase();

        return messageHash;
    }

    public String checkMessageLength() {
        if (messagePayload == null || messagePayload.isEmpty()) {
            return "Please enter a message.";
        }

        int length = messagePayload.length();

        if (length > 250) {
            return "Please enter a message of less than 250 characters. "
                    + "Message exceeds 250 characters by " + (length - 250) + ".";
        }

        return "Message ready to send.";
    }

    public String sentMessage(int choice) {
        switch (choice) {
            case 1:
                this.messageSent = true;
                sentMessages.add(messagePayload);
                return "Message successfully sent.";
            case 2:
                storedMessages.add(messagePayload);
                return "Message successfully stored.";
            case 0:
                disregardedMessages.add(messagePayload);
                return "Press 0 to delete the message.";
            default:
                return "Invalid choice.";
        }
    }

    public String getMessageDisplay() {
        return "Message ID: " + messageID + "\n"
                + "Message Hash: " + messageHash + "\n"
                + "Recipient: " + recipientCell + "\n"
                + "Message: " + messagePayload;
    }

    public int returnTotalMessages() {
        return sentMessages.size();
    }

    public String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages sent yet.";
        }

        StringBuilder result = new StringBuilder("Sent Messages:\n");
        sentMessages.forEach(msg -> result.append(msg).append("\n"));
        return result.toString();
    }

    // all checks must pass and the hash must already be made
    public boolean validateCompleteMessage() {
        return checkMessageID()
                && checkRecipientCell().contains("successfully")
                && checkMessageLength().equals("Message ready to send.")
                && messageHash != null;
    }

    public String getMessageID() { return messageID; }
    public int getMessageNumber() { return messageNumber; }
    public String getRecipientCell() { return recipientCell; }
    public String getMessagePayload() { return messagePayload; }
    public String getMessageHash() { return messageHash; }
    public boolean isMessageSent() { return messageSent; }

    public static void clearMessages() {
        sentMessages.clear();
        storedMessages.clear();
        disregardedMessages.clear();
    }

    public static ArrayList<String> getSentMessages() { return sentMessages; }
    public static ArrayList<String> getStoredMessages() { return storedMessages; }
    public static ArrayList<String> getDisregardedMessages() { return disregardedMessages; }
}
