import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

/**
 * JUnit Test Class for MessageV2 (Approach 2 - Streamlined)
 * Tests message validation using streamlined methods
 * Author: Student
 * Module: PROG5121
 */
public class MessageV2Test {
    
    private MessageV2 validMessage;
    private MessageV2 invalidMessage;
    
    @Before
    public void setUp() {
        MessageV2.clearMessages();
        validMessage = new MessageV2(1, "+27718693002", 
                                    "Hi Mike, can you join us for dinner tonight?");
        invalidMessage = new MessageV2(2, "08575975889", 
                                      "Hi Keegan, did you receive the payment?");
    }
    
    @After
    public void tearDown() {
        MessageV2.clearMessages();
    }
    
    // ==================== Message ID Tests ====================
    
    @Test
    public void testCheckMessageIDValidV2() {
        boolean result = validMessage.checkMessageID();
        assertTrue("Message ID should be valid", result);
    }
    
    @Test
    public void testMessageIDGeneratedV2() {
        assertNotNull("Message ID should be generated", validMessage.getMessageID());
        assertEquals("Message ID should be 10 digits", 10, validMessage.getMessageID().length());
    }
    
    @Test
    public void testMessageIDIsNumericV2() {
        assertTrue("Message ID should be numeric", 
                   validMessage.getMessageID().matches("[0-9]+"));
    }
    
    // ==================== Recipient Cell Tests ====================
    
    @Test
    public void testCheckRecipientCellValidV2() {
        String result = validMessage.checkRecipientCell();
        assertEquals("Should return success message", 
                    "Cell phone number successfully captured.", 
                    result);
    }
    
    @Test
    public void testCheckRecipientCellInvalidV2() {
        String result = invalidMessage.checkRecipientCell();
        assertTrue("Should contain error message", 
                   result.contains("incorrectly formatted"));
    }
    
    @Test
    public void testCheckRecipientCellNoPlusV2() {
        MessageV2 noPlus = new MessageV2(1, "27718693002", "Test message");
        String result = noPlus.checkRecipientCell();
        assertTrue("Should error for missing +", 
                   result.contains("incorrectly formatted"));
    }
    
    @Test
    public void testCheckRecipientCellTooManyDigitsV2() {
        MessageV2 tooMany = new MessageV2(1, "+27718693002999", "Test message");
        String result = tooMany.checkRecipientCell();
        assertTrue("Should error for too many digits", 
                   result.contains("incorrectly formatted"));
    }
    
    @Test
    public void testCheckRecipientCellWithLettersV2() {
        MessageV2 withLetters = new MessageV2(1, "+27ABC123456", "Test message");
        String result = withLetters.checkRecipientCell();
        assertTrue("Should error for non-numeric digits", 
                   result.contains("incorrectly formatted"));
    }
    
    // ==================== Message Hash Tests ====================
    
    @Test
    public void testCreateMessageHashFormatV2() {
        String hash = validMessage.createMessageHash();
        assertNotNull("Hash should not be null", hash);
        assertTrue("Hash should contain colons", hash.contains(":"));
        
        String[] parts = hash.split(":");
        assertEquals("Hash should have 3 parts", 3, parts.length);
    }
    
    @Test
    public void testCreateMessageHashContentV2() {
        String hash = validMessage.createMessageHash();
        assertEquals("Hash should be uppercase", hash, hash.toUpperCase());
        assertTrue("Hash should contain message number", hash.contains(":1:"));
    }
    
    @Test
    public void testCreateMessageHashFirstAndLastWordsV2() {
        String hash = validMessage.createMessageHash();
        // Message: "Hi Mike, can you join us for dinner tonight?"
        // First: Hi, Last: tonight
        assertTrue("Hash should contain first word (HI)", hash.contains("HI"));
        assertTrue("Hash should contain last word (TONIGHT)", hash.contains("TONIGHT"));
    }
    
    @Test
    public void testCreateMultipleMessageHashesUniqueV2() {
        MessageV2 msg1 = new MessageV2(1, "+27718693002", 
                                      "Hi Mike, can you join us for dinner tonight?");
        MessageV2 msg2 = new MessageV2(2, "+27838884567", 
                                      "Where are you? You are late!");
        
        String hash1 = msg1.createMessageHash();
        String hash2 = msg2.createMessageHash();
        
        assertNotEquals("Hashes should be different", hash1, hash2);
    }
    
    @Test
    public void testCreateMessageHashWithPunctuationV2() {
        MessageV2 msgPunct = new MessageV2(1, "+27718693002", 
                                          "Hello, John! How are you?");
        String hash = msgPunct.createMessageHash();
        assertNotNull("Hash should handle punctuation", hash);
        // Punctuation should be stripped
        assertTrue("Hash should contain clean words", hash.matches(".*:\\d+:[A-Z]+"));
    }
    
    // ==================== Message Length Tests ====================
    
    @Test
    public void testCheckMessageLengthValidV2() {
        String result = validMessage.checkMessageLength();
        assertEquals("Should return ready message", 
                    "Message ready to send.", 
                    result);
    }
    
    @Test
    public void testCheckMessageLengthInvalidV2() {
        String longMessage = "A".repeat(260);
        MessageV2 tooLong = new MessageV2(1, "+27718693002", longMessage);
        String result = tooLong.checkMessageLength();
        
        assertTrue("Should indicate too long", result.contains("exceeds"));
        assertTrue("Should show excess amount", result.contains("10"));
    }
    
    @Test
    public void testCheckMessageLengthBoundaryV2() {
        String boundaryMessage = "A".repeat(250);
        MessageV2 boundary = new MessageV2(1, "+27718693002", boundaryMessage);
        String result = boundary.checkMessageLength();
        assertEquals("Should accept 250 chars", "Message ready to send.", result);
    }
    
    @Test
    public void testCheckMessageLengthEmptyV2() {
        MessageV2 empty = new MessageV2(1, "+27718693002", "");
        String result = empty.checkMessageLength();
        assertTrue("Should require message", result.contains("enter a message"));
    }
    
    @Test
    public void testCheckMessageLengthNullV2() {
        MessageV2 nullMsg = new MessageV2(1, "+27718693002", null);
        String result = nullMsg.checkMessageLength();
        assertTrue("Should handle null", result.contains("enter a message"));
    }
    
    // ==================== Send Message Tests ====================
    
    @Test
    public void testSentMessageSendV2() {
        String result = validMessage.sentMessage(1);
        assertEquals("Should return send success", 
                    "Message successfully sent.", 
                    result);
        assertTrue("Message should be marked sent", validMessage.isMessageSent());
    }
    
    @Test
    public void testSentMessageStoreV2() {
        String result = validMessage.sentMessage(2);
        assertEquals("Should return store success", 
                    "Message successfully stored.", 
                    result);
    }
    
    @Test
    public void testSentMessageDisregardV2() {
        String result = validMessage.sentMessage(0);
        assertEquals("Should return disregard message", 
                    "Press 0 to delete the message.", 
                    result);
    }
    
    @Test
    public void testSentMessageInvalidV2() {
        String result = validMessage.sentMessage(99);
        assertEquals("Should return invalid choice", 
                    "Invalid choice.", 
                    result);
    }
    
    // ==================== Message Counter Tests ====================
    
    @Test
    public void testReturnTotalMessagesV2() {
        MessageV2 msg1 = new MessageV2(1, "+27718693002", "Message 1");
        MessageV2 msg2 = new MessageV2(2, "+27838884567", "Message 2");
        
        msg1.sentMessage(1);
        msg2.sentMessage(1);
        
        assertEquals("Should count 2 sent messages", 2, msg1.returnTotalMessages());
    }
    
    @Test
    public void testMessageCounterIncrementsV2() {
        for (int i = 1; i <= 5; i++) {
            MessageV2 msg = new MessageV2(i, "+27718693002", "Test " + i);
            msg.sentMessage(1);
        }
        
        MessageV2 lastMsg = new MessageV2(1, "+27718693002", "Test");
        assertEquals("Should have 5 messages", 5, lastMsg.returnTotalMessages());
    }
    
    // ==================== Message Display Tests ====================
    
    @Test
    public void testGetMessageDisplayV2() {
        validMessage.createMessageHash();
        String display = validMessage.getMessageDisplay();
        
        assertTrue("Should contain Message ID", display.contains("Message ID:"));
        assertTrue("Should contain Message Hash", display.contains("Message Hash:"));
        assertTrue("Should contain Recipient", display.contains("Recipient:"));
        assertTrue("Should contain Message", display.contains("Message:"));
    }
    
    @Test
    public void testGetMessageDisplayFormatV2() {
        validMessage.createMessageHash();
        String display = validMessage.getMessageDisplay();
        
        String[] lines = display.split("\n");
        assertEquals("Display should have 4 lines", 4, lines.length);
    }
    
    // ==================== Static List Tests ====================
    
    @Test
    public void testSentMessagesListV2() {
        MessageV2 msg = new MessageV2(1, "+27718693002", "Test message");
        msg.sentMessage(1);
        
        assertEquals("Sent list should have 1 item", 
                    1, MessageV2.getSentMessages().size());
        assertTrue("List should contain message", 
                   MessageV2.getSentMessages().contains("Test message"));
    }
    
    @Test
    public void testStoredMessagesListV2() {
        MessageV2 msg = new MessageV2(1, "+27718693002", "Test message");
        msg.sentMessage(2);
        
        assertEquals("Stored list should have 1 item", 
                    1, MessageV2.getStoredMessages().size());
    }
    
    @Test
    public void testDisregardedMessagesListV2() {
        MessageV2 msg = new MessageV2(1, "+27718693002", "Test message");
        msg.sentMessage(0);
        
        assertEquals("Disregarded list should have 1 item", 
                    1, MessageV2.getDisregardedMessages().size());
    }
    
    @Test
    public void testClearMessagesV2() {
        MessageV2 msg1 = new MessageV2(1, "+27718693002", "Test 1");
        MessageV2 msg2 = new MessageV2(2, "+27718693002", "Test 2");
        
        msg1.sentMessage(1);
        msg2.sentMessage(2);
        
        assertTrue("Should have messages", !MessageV2.getSentMessages().isEmpty());
        
        MessageV2.clearMessages();
        
        assertTrue("Sent should be empty", MessageV2.getSentMessages().isEmpty());
        assertTrue("Stored should be empty", MessageV2.getStoredMessages().isEmpty());
    }
    
    // ==================== Complete Message Workflow Tests ====================
    
    @Test
    public void testValidateCompleteMessageV2() {
        boolean result = validMessage.validateCompleteMessage();
        assertFalse("Should fail - hash not yet created", result);
        
        validMessage.createMessageHash();
        result = validMessage.validateCompleteMessage();
        assertTrue("Should pass after hash creation", result);
    }
    
    @Test
    public void testValidateCompleteMessageWithInvalidPhoneV2() {
        boolean result = invalidMessage.validateCompleteMessage();
        assertFalse("Should fail with invalid phone", result);
    }
    
    // ==================== Edge Cases ====================
    
    @Test
    public void testMessageWithSingleWordV2() {
        MessageV2 singleWord = new MessageV2(1, "+27718693002", "Hello");
        String hash = singleWord.createMessageHash();
        assertNotNull("Should handle single word", hash);
        assertTrue("Should work with single word", hash.contains("HELLO"));
    }
    
    @Test
    public void testMessageWithMultipleSpacesV2() {
        MessageV2 multiSpace = new MessageV2(1, "+27718693002", 
                                           "Hello    there    friend");
        String hash = multiSpace.createMessageHash();
        assertNotNull("Should handle multiple spaces", hash);
        assertTrue("Should extract correct words", hash.contains("HELLOFRIEND"));
    }
    
    @Test
    public void testMessageNumberSequenceV2() {
        MessageV2 msg1 = new MessageV2(1, "+27718693002", "Test");
        MessageV2 msg2 = new MessageV2(2, "+27718693002", "Test");
        MessageV2 msg3 = new MessageV2(3, "+27718693002", "Test");
        
        msg1.createMessageHash();
        msg2.createMessageHash();
        msg3.createMessageHash();
        
        assertTrue("Each should have correct number", 
                   msg1.getMessageHash().contains(":1:") &&
                   msg2.getMessageHash().contains(":2:") &&
                   msg3.getMessageHash().contains(":3:"));
    }
}
