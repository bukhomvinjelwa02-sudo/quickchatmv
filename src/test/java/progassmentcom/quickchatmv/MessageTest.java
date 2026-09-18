package progassmentcom.quickchatmv;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

public class MessageTest {

    private Message message;
 @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        
        Message.clearMessages();
        message = new Message(1, "+27718693002", "Hi Mike, can you join us for lunch today");
    }

    //  checkMessageID 

    @Test
    void checkMessageID_generatedID_isValid() {
        // messageID is auto-generated as a 10 digit string in the constructor
        assertTrue(message.checkMessageID());
        assertNotNull(message.getMessageID());
        assertEquals(10, message.getMessageID().length());
    }

    //  checkRecipientCell 

    @Test
    void checkRecipientCell_validNumber_returnsSuccessMessage() {
        assertEquals("Cell phone number successfully captured.", message.checkRecipientCell());
    }

    @Test
    void checkRecipientCell_missingPlusSign_returnsErrorMessage() {
        Message msg = new Message(1, "27718693002", "Hello there");
        String result = msg.checkRecipientCell();
        assertTrue(result.contains("incorrectly formatted"));
    }

    @Test
    void checkRecipientCell_tooFewDigits_returnsErrorMessage() {
        // Only 7 digits after the +, needs 10-12
        Message msg = new Message(1, "+1234567", "Hello there");
        String result = msg.checkRecipientCell();
        assertTrue(result.contains("incorrectly formatted"));
    }

    @Test
    void checkRecipientCell_tooManyDigits_returnsErrorMessage() {
        // 13 digits after the +, needs 10-12
        Message msg = new Message(1, "+1234567890123", "Hello there");
        String result = msg.checkRecipientCell();
        assertTrue(result.contains("incorrectly formatted"));
    }

    @Test
    void checkRecipientCell_empty_returnsErrorMessage() {
        Message msg = new Message(1, "", "Hello there");
        String result = msg.checkRecipientCell();
        assertTrue(result.contains("incorrectly formatted"));
    }

    // createMessageHash 

    @Test
    void createMessageHash_followsExpectedPattern() {
        String hash = message.createMessageHash();
        String idPrefix = message.getMessageID().substring(0, 2);
        // Pattern: <first 2 digits of ID>:<message number>:<FIRSTWORD LAST WORD UPPERCASE, no spaces>
        assertEquals(idPrefix + ":1:HITODAY", hash);
    }

    @Test
    void createMessageHash_singleWordMessage_usesWordTwice() {
        Message msg = new Message(2, "+27718693002", "Hello");
        String hash = msg.createMessageHash();
        String idPrefix = msg.getMessageID().substring(0, 2);
        assertEquals(idPrefix + ":2:HELLOHELLO", hash);
    }

    //  checkMessageLength 

    @Test
    void checkMessageLength_underLimit_returnsReadyMessage() {
        assertEquals("Message ready to send.", message.checkMessageLength());
    }

    @Test
    void checkMessageLength_empty_returnsPromptMessage() {
        Message msg = new Message(1, "+27718693002", "");
        assertEquals("Please enter a message.", msg.checkMessageLength());
    }

    @Test
    void checkMessageLength_overLimit_reportsExcessCharacters() {
        String longMessage = "a".repeat(255);
        Message msg = new Message(1, "+27718693002", longMessage);
        String result = msg.checkMessageLength();
        assertTrue(result.contains("exceeds 250 characters by 5"));
    }

    // sentMessage 

    @Test
    void sentMessage_choiceOne_addsToSentMessages() {
        String result = message.sentMessage(1);
        assertEquals("Message successfully sent.", result);
        assertTrue(message.isMessageSent());
        assertEquals(1, Message.getSentMessages().size());
    }

    @Test
    void sentMessage_choiceTwo_addsToStoredMessages() {
        String result = message.sentMessage(2);
        assertEquals("Message successfully stored.", result);
        assertEquals(1, Message.getStoredMessages().size());
    }

    @Test
    void sentMessage_choiceZero_addsToDisregardedMessages() {
        String result = message.sentMessage(0);
        assertEquals("Press 0 to delete the message.", result);
        assertEquals(1, Message.getDisregardedMessages().size());
    }

    @Test
    void sentMessage_invalidChoice_returnsInvalidMessage() {
        assertEquals("Invalid choice.", message.sentMessage(9));
    }

    //  returnTotalMessages / printMessages 

    @Test
    void returnTotalMessages_afterOneSent_returnsOne() {
        message.sentMessage(1);
        assertEquals(1, message.returnTotalMessages());
    }

    @Test
    void printMessages_noneSent_returnsPlaceholderMessage() {
        assertEquals("No messages sent yet.", message.printMessages());
    }

    @Test
    void printMessages_afterSending_includesPayload() {
        message.sentMessage(1);
        String report = message.printMessages();
        assertTrue(report.startsWith("Sent Messages:"));
        assertTrue(report.contains("Hi Mike, can you join us for lunch today"));
    }

    //  getMessageDisplay 
    @Test
    void getMessageDisplay_containsAllFields() {
        message.createMessageHash();
        String display = message.getMessageDisplay();
        assertTrue(display.contains(message.getMessageID()));
        assertTrue(display.contains(message.getMessageHash()));
        assertTrue(display.contains(message.getRecipientCell()));
        assertTrue(display.contains(message.getMessagePayload()));
    }

    //  validateCompleteMessage 

    @Test
    void validateCompleteMessage_allValidAndHashed_returnsTrue() {
        message.createMessageHash();
        assertTrue(message.validateCompleteMessage());
    }

    @Test
    void validateCompleteMessage_hashNotCreatedYet_returnsFalse() {
        // createMessageHash() was never called, so messageHash is still null
        assertFalse(message.validateCompleteMessage());
    }

    @Test
    void validateCompleteMessage_invalidRecipient_returnsFalse() {
        Message msg = new Message(1, "0718693002", "Hi there");
        msg.createMessageHash();
        assertFalse(msg.validateCompleteMessage());
    }

    @Test
    void validateCompleteMessage_messageTooLong_returnsFalse() {
        Message msg = new Message(1, "+27718693002", "a".repeat(300));
        msg.createMessageHash();
        assertFalse(msg.validateCompleteMessage());
    }
}