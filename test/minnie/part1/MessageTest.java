package minnie.part1;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MessageTest {

    private Message validMessage;

    @Before
    public void setUp() {
        validMessage = new Message(
                "+27761234567",
                "Hi Mike, can you join us tonight?"
        );
    }

    @Test
    public void testMessageLength() {
        assertTrue(validMessage.getMessage().length() <= 250);
    }

    @Test
    public void testRecipientSuccess() {
        assertEquals(
                "Cell phone number successfully captured.",
                validMessage.checkRecipientCell()
        );
    }

    @Test
    public void testRecipientFailure() {
        Message invalidMessage = new Message(
                "12345",
                "Hello"
        );

        assertEquals(
                "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
                invalidMessage.checkRecipientCell()
        );
    }

    @Test
    public void testMessageHash() {
        assertNotNull(validMessage.getMessageHash());
        assertFalse(validMessage.getMessageHash().isEmpty());
    }
}