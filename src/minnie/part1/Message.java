package minnie.part1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Message {

    private String messageID;
    private String recipient;
    private String message;
    private String messageHash;
    private String status;

    private static int totalMessagesSent = 0;
    private static int messageNumber = 0;

    private static final int MAX_MESSAGES = 100;

    private static Message[] sentArray = new Message[MAX_MESSAGES];
    private static Message[] disregardedArray = new Message[MAX_MESSAGES];
    private static Message[] storedArray = new Message[MAX_MESSAGES];

    private static String[] messageHashes = new String[MAX_MESSAGES];
    private static String[] messageIDs = new String[MAX_MESSAGES];

    private static int sentCount = 0;
    private static int disregardedCount = 0;
    private static int storedCount = 0;
    private static int hashCount = 0;
    private static int idCount = 0;

    private static final List<Message> sentMessages = new ArrayList<>();
   

    // Constructor
    public Message(String recipient, String message) {

        Random random = new Random();
        StringBuilder id = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            id.append(random.nextInt(10));
        }

        this.messageID = id.toString();
        this.recipient = recipient;
        this.message = message;
        this.messageHash = createMessageHash();
        this.status = "Pending";
    }

    // Validate recipient cellphone number
    public String checkRecipientCell() {

        if (recipient == null) {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }

        if (recipient.matches("(\\+27\\d{9}|0\\d{9})")) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }

    // Create message hash
    public String createMessageHash() {

        if (message == null || message.trim().isEmpty()) {
            return "";
        }

        String cleanMessage = message.replaceAll("[^a-zA-Z ]", "").trim();

        if (cleanMessage.isEmpty()) {
            return "";
        }

        String[] words = cleanMessage.split("\\s+");

        String firstWord = words[0].toUpperCase();
        String lastWord = words[words.length - 1].toUpperCase();

        return messageID.substring(0, 2) + ":"
                + messageNumber + ":"
                + firstWord + lastWord;
    }

    // Send, disregard or store message
    public String sentMessage(Scanner input) {

        if (message == null || message.trim().isEmpty()) {
            return "Message cannot be empty.";
        }

        if (message.length() > 250) {
            return "Message exceeds 250 characters.";
        }

        if (!checkRecipientCell().equals(
                "Cell phone number successfully captured.")) {
            return checkRecipientCell();
        }

        System.out.println("\nChoose what you want to do:");
        System.out.println("1. Send Message");
        System.out.println("2. Disregard Message");
        System.out.println("3. Store Message");

        System.out.print("Enter your choice: ");

        String choice = input.nextLine();

        switch (choice) {

            case "1":

                if (sentCount >= MAX_MESSAGES) {
                    return "Message limit reached.";
                }

                status = "Sent";

                sentArray[sentCount] = this;
                sentCount++;

                sentMessages.add(this);

                totalMessagesSent++;
                messageNumber++;

                recordMessageDetails();

                return "Message successfully sent.";

            case "2":

                if (disregardedCount >= MAX_MESSAGES) {
                    return "Disregarded message limit reached.";
                }

                status = "Disregarded";

                disregardedArray[disregardedCount] = this;
                disregardedCount++;

                recordMessageDetails();

                return "Message disregarded.";

            case "3":

                if (storedCount >= MAX_MESSAGES) {
                    return "Stored message limit reached.";
                }

                status = "Stored";

                storedArray[storedCount] = this;
                storedCount++;

                recordMessageDetails();
                saveStoredMessagesToFile();

                return "Message successfully stored.";

            default:

                return "Invalid choice.";
        }
    }

    // Record message ID and hash
    public void recordMessageDetails() {

        if (hashCount < MAX_MESSAGES) {
            messageHashes[hashCount] = messageHash;
            hashCount++;
        }

        if (idCount < MAX_MESSAGES) {
            messageIDs[idCount] = messageID;
            idCount++;
        }
    }

    // Save stored messages to JSON file
    public static void saveStoredMessagesToFile() {
        
        System.out.println("Saving messeges to JSON file...");
                

        try (PrintWriter writer = new PrintWriter(
                new FileWriter("stored_messages.json"))) {

            writer.println("[");

            for (int i = 0; i < storedCount; i++) {

                Message msg = storedArray[i];

                writer.println("  {");
                writer.println("    \"messageID\": \"" 
                        + escapeJSON(msg.messageID) + "\",");
                writer.println("    \"recipient\": \"" 
                        + escapeJSON(msg.recipient) + "\",");
                writer.println("    \"message\": \"" 
                        + escapeJSON(msg.message) + "\",");
                writer.println("    \"messageHash\": \"" 
                        + escapeJSON(msg.messageHash) + "\",");
                writer.println("    \"status\": \"" 
                        + escapeJSON(msg.status) + "\"");

                if (i < storedCount - 1) {
                    writer.println("  },");
                } else {
                    writer.println("  }");
                }
            }

            writer.println("]");

        } catch (IOException e) {
            System.out.println("Error saving messages: " + e.getMessage());
        }
    }

    // Escape special JSON characters
    private static String escapeJSON(String text) {

        if (text == null) {
            return "";
        }

        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    // Display sent messages
    public static void displaySentMessages() {

        System.out.println("\n--- SENT MESSAGES ---");

        if (sentCount == 0) {
            System.out.println("No sent messages.");
            return;
        }

        for (int i = 0; i < sentCount; i++) {
            displayMessageDetails(sentArray[i]);
        }
    }
    
    // Display stored messages
    public static void displayStoredMessages() {

        System.out.println("\n--- STORED MESSAGES ---");

        if (storedCount == 0) {
            System.out.println("No stored messages.");
            return;
        }

        for (int i = 0; i < storedCount; i++) {
            displayMessageDetails(storedArray[i]);
        }
    }

    // Search message using ID
    public static Message searchMessageByID(String id) {

        for (int i = 0; i < sentCount; i++) {
            if (sentArray[i].getMessageID().equals(id)) {
                return sentArray[i];
            }
        }

        for (int i = 0; i < storedCount; i++) {
            if (storedArray[i].getMessageID().equals(id)) {
                return storedArray[i];
            }
        }

        for (int i = 0; i < disregardedCount; i++) {
            if (disregardedArray[i].getMessageID().equals(id)) {
                return disregardedArray[i];
            }
        }

        return null;
    }

    // Search messages using recipient
    public static void searchMessagesByRecipient(String recipient) {

        boolean found = false;

        System.out.println("\n--- MESSAGES FOR RECIPIENT ---");

        for (int i = 0; i < sentCount; i++) {

            if (sentArray[i].getRecipient().equals(recipient)) {
                displayMessageDetails(sentArray[i]);
                found = true;
            }
        }

        for (int i = 0; i < storedCount; i++) {

            if (storedArray[i].getRecipient().equals(recipient)) {
                displayMessageDetails(storedArray[i]);
                found = true;
            }
        }

        for (int i = 0; i < disregardedCount; i++) {

            if (disregardedArray[i].getRecipient().equals(recipient)) {
                displayMessageDetails(disregardedArray[i]);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No messages found for this recipient.");
        }
    }

    // Find the longest message
    public static void displayLongestMessage() {

        Message longest = null;

        for (int i = 0; i < sentCount; i++) {

            if (longest == null
                    || sentArray[i].getMessage().length()
                    > longest.getMessage().length()) {

                longest = sentArray[i];
            }
        }

        for (int i = 0; i < storedCount; i++) {

            if (longest == null
                    || storedArray[i].getMessage().length()
                    > longest.getMessage().length()) {

                longest = storedArray[i];
            }
        }

        for (int i = 0; i < disregardedCount; i++) {

            if (longest == null
                    || disregardedArray[i].getMessage().length()
                    > longest.getMessage().length()) {

                longest = disregardedArray[i];
            }
        }

        System.out.println("\n--- LONGEST MESSAGE ---");

        if (longest == null) {
            System.out.println("No messages available.");
        } else {
            displayMessageDetails(longest);
        }
    }

    // Delete stored message using its hash
    public static boolean deleteStoredMessage(String hash) {

        for (int i = 0; i < storedCount; i++) {

            if (storedArray[i].getMessageHash().equals(hash)) {

                for (int j = i; j < storedCount - 1; j++) {
                    storedArray[j] = storedArray[j + 1];
                }

                storedArray[storedCount - 1] = null;
                storedCount--;

                saveStoredMessagesToFile();

                System.out.println("Stored message deleted successfully.");
                return true;
            }
        }

        System.out.println("Message hash not found.");
        return false;
    }

    // Display message report
    public static void displayReport() {

        System.out.println("\n========== MESSAGE REPORT ==========");

        if (sentCount == 0
                && storedCount == 0
                && disregardedCount == 0) {

            System.out.println("No messages available for the report.");
            return;
        }

        System.out.println("\n--- SENT MESSAGES ---");

        for (int i = 0; i < sentCount; i++) {
            displayMessageDetails(sentArray[i]);
        }

        System.out.println("\n--- STORED MESSAGES ---");

        for (int i = 0; i < storedCount; i++) {
            displayMessageDetails(storedArray[i]);
        }

        System.out.println("\n--- DISREGARDED MESSAGES ---");

        for (int i = 0; i < disregardedCount; i++) {
            displayMessageDetails(disregardedArray[i]);
        }

        System.out.println("\nTotal messages sent: " + totalMessagesSent);
    }

    // Display individual message details
    private static void displayMessageDetails(Message msg) {

        if (msg == null) {
            return;
        }

        System.out.println("-------------------------");
        System.out.println("Message ID: " + msg.getMessageID());
        System.out.println("Message Hash: " + msg.getMessageHash());
        System.out.println("Recipient: " + msg.getRecipient());
        System.out.println("Message: " + msg.getMessage());
        System.out.println("Status: " + msg.getStatus());
    }

    // Return total messages sent
    public static int returnTotalMessages() {
        return totalMessagesSent;
    }

    // Getters
    public String getMessageID() {
        return messageID;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }
}