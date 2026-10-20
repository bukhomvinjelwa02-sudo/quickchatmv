import java.util.regex.Pattern;
import java.util.Random;
import java.util.ArrayList;

/**
 * Message Approach 2 - Streamlined implementation with combined operations
 * Author: Student
 * Module: PROG5121
 */
public class MessageV2 {
    
    private String messageID;
    private int messageNumber;
    private String recipientCell;
    private String messagePayload;
    private String messageHash;
    private boolean messageSent;
    private boolean messageReceived;
    private boolean messageRead;
    
    private static ArrayList<String> sentMessages = new ArrayList<>();
    private static ArrayList<String> storedMessages = new ArrayList<>();
    private static ArrayList<String> disregardedMessages = new ArrayList<>();
    
    /**
     * Constructor - generates ID automatically
     */
    public MessageV2(int msgNumber, String recipient, String payload) {
        this.messageNumber = msgNumber;
        this.recipientCell = recipient;
        this.messagePayload = payload;
        this.messageSent = false;
        this.messageReceived = false;
        this.messageRead = false;
        this.messageID = generateRandomID();
    }
    
    /**
     * Generate 10-digit random ID
     */
    private String generateRandomID() {
        StringBuilder id = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            id.append(rand.nextInt(10));
        }
        return id.toString();
    }
    
    /**
     * Validate message ID - single check
     */
    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10 && 
               messageID.matches("[0-9]+");
    }
    
    /**
     * Validate recipient - combined check
     */
    public String checkRecipientCell() {
        if (recipientCell == null || recipientCell.isEmpty()) {
            return "Cell phone number is incorrectly formatted or does not contain an " +
                   "international code. Please correct the number and try again.";
        }
        
        // Check: starts with +, followed by digits, max 10 digits after code
        boolean isValid = recipientCell.matches("^\\+\\d{8,10}$");
        
        return isValid ? "Cell phone number successfully captured." :
               "Cell phone number is incorrectly formatted or does not contain an " +
               "international code. Please correct the number and try again.";
    }
    
    /**
     * Create message hash in single operation
     * Format: XX:M:FIRSTLAST
     */
    public String createMessageHash() {
        if (messageID == null || messagePayload == null) {
            return "ERROR";
        }
        
        String idPrefix = messageID.substring(0, Math.min(2, messageID.length()));
        String[] words = messagePayload.trim().split("\\s+");
        
        String firstWord = (words.length > 0 ? words[0] : "").replaceAll("[^a-zA-Z0-9]", "");
        String lastWord = (words.length > 0 ? words[words.length - 1] : "").replaceAll("[^a-zA-Z0-9]", "");
        
        messageHash = idPrefix + ":" + messageNumber + ":" + 
                    (firstWord + lastWord).toUpperCase();
        
        return messageHash;
    }
    
    /**
     * Check message length with detailed feedback
     */
    public String checkMessageLength() {
        if (messagePayload == null || messagePayload.isEmpty()) {
            return "Please enter a message.";
        }
        
        int length = messagePayload.length();
        
        if (length > 250) {
            return "Please enter a message of less than 250 characters. " +
                   "Message exceeds 250 characters by " + (length - 250) + ".";
        }
        
        return "Message ready to send.";
    }
    
    /**
     * Handle message action with switch-case
     */
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
    
    /**
     * Get formatted display string
     */
    public String getMessageDisplay() {
        return "Message ID: " + messageID + "\n" +
               "Message Hash: " + messageHash + "\n" +
               "Recipient: " + recipientCell + "\n" +
               "Message: " + messagePayload;
    }
    
    /**
     * Get total messages sent
     */
    public int returnTotalMessages() {
        return sentMessages.size();
    }
    
    /**
     * Print all sent messages
     */
    public String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages sent yet.";
        }
        
        StringBuilder result = new StringBuilder("=== Sent Messages ===\n");
        sentMessages.forEach(msg -> result.append(msg).append("\n"));
        return result.toString();
    }
    
    /**
     * Validate complete message workflow
     */
    public boolean validateCompleteMessage() {
        return checkMessageID() && 
               checkRecipientCell().contains("successfully") && 
               checkMessageLength().equals("Message ready to send.");
    }
    
    // Getters
    public String getMessageID() { return messageID; }
    public int getMessageNumber() { return messageNumber; }
    public String getRecipientCell() { return recipientCell; }
    public String getMessagePayload() { return messagePayload; }
    public String getMessageHash() { return messageHash; }
    public boolean isMessageSent() { return messageSent; }
    
    // Static methods
    public static void clearMessages() {
        sentMessages.clear();
        storedMessages.clear();
        disregardedMessages.clear();
    }
    
    public static ArrayList<String> getSentMessages() { return sentMessages; }
    public static ArrayList<String> getStoredMessages() { return storedMessages; }
    public static ArrayList<String> getDisregardedMessages() { return disregardedMessages; }
}
