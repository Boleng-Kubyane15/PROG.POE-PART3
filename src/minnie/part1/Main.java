package minnie.part1;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        // Registration
        Login registeredUser = null;

        while (registeredUser == null) {
            registeredUser = Registration.registerUser(input);
        }

        // Login
        System.out.println("\n--- QuickChat Login ---");

        boolean loggedIn = false;

        while (!loggedIn) {

            System.out.print("Enter username: ");
            String username = input.nextLine();

            System.out.print("Enter password: ");
            String password = input.nextLine();

            registeredUser.setEnteredUsername(username);
            registeredUser.setEnteredPassword(password);

            loggedIn = registeredUser.loginUser();

            if (loggedIn) {
                System.out.println(registeredUser.returnLoginStatus());
            } else {
                System.out.println(registeredUser.returnLoginStatus());
                System.out.println("Please try again.\n");
            }
        }

        // Main menu
        boolean quit = false;

        while (!quit) {

            System.out.println("\n--- Main Menu ---");
            System.out.println("1) Send Messages");
            System.out.println("2) Show Recently Sent Messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");
            System.out.print("Choose an option: ");

            String menuChoice = input.nextLine();

            switch (menuChoice) {

                // Send messages
                case "1":

                    System.out.print("How many messages would you like to send? ");

                    int numMessages;

                    try {
                        numMessages = Integer.parseInt(input.nextLine());

                        if (numMessages <= 0) {
                            System.out.println("Please enter a number greater than 0.");
                            break;
                        }

                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a valid number.");
                        break;
                    }

                    for (int i = 0; i < numMessages; i++) {

                        System.out.println("\nMessage " + (i + 1));

                        System.out.print("Enter recipient (+27...): ");
                        String recipient = input.nextLine();

                        System.out.print("Enter message: ");
                        String messageText = input.nextLine();

                        Message msg = new Message(recipient, messageText);

                        System.out.println(msg.sentMessage(input));

                        if (msg.getStatus().equals("Sent")) {

                            System.out.println("Message ID: " + msg.getMessageID());
                            System.out.println("Message Hash: " + msg.getMessageHash());
                            System.out.println("Recipient: " + msg.getRecipient());
                            System.out.println("Message: " + msg.getMessage());
                        }
                    }

                    System.out.println("\nTotal messages sent: "
                            + Message.returnTotalMessages());

                    break;

                // Display sent messages
                case "2":

                    Message.displaySentMessages();

                    break;

                // Quit
                case "3":

                    quit = true;

                    System.out.println("Thank you for using QuickChat. Goodbye!");

                    break;

                // Stored Messages menu
                case "4":

                    storedMessagesMenu(input);

                    break;

                default:

                    System.out.println("Invalid option. Please choose 1, 2, 3 or 4.");
            }
        }

        input.close();
    }

    // Stored Messages Menu
    public static void storedMessagesMenu(Scanner input) {

        boolean back = false;

        while (!back) {

            System.out.println("\n--- Stored Messages Menu ---");
            System.out.println("1) Display Stored Messages");
            System.out.println("2) Search Message by ID");
            System.out.println("3) Search Messages by Recipient");
            System.out.println("4) Delete Stored Message");
            System.out.println("5) Display Message Report");
            System.out.println("6) Display Longest Message");
            System.out.println("7) Back to Main Menu");

            System.out.print("Choose an option: ");

            String choice = input.nextLine();

            switch (choice) {

                // Display stored messages
                case "1":

                    Message.displayStoredMessages();

                    break;

                // Search by message ID
                case "2":

                    System.out.print("Enter Message ID: ");
                    String id = input.nextLine();

                    Message foundMessage = Message.searchMessageByID(id);

                    if (foundMessage != null) {

                        System.out.println("\nMessage found!");

                        System.out.println("Message ID: "
                                + foundMessage.getMessageID());

                        System.out.println("Message Hash: "
                                + foundMessage.getMessageHash());

                        System.out.println("Recipient: "
                                + foundMessage.getRecipient());

                        System.out.println("Message: "
                                + foundMessage.getMessage());

                        System.out.println("Status: "
                                + foundMessage.getStatus());

                    } else {

                        System.out.println("Message ID not found.");
                    }

                    break;

                // Search by recipient
                case "3":

                    System.out.print("Enter recipient cellphone number: ");
                    String recipient = input.nextLine();

                    Message.searchMessagesByRecipient(recipient);

                    break;

                // Delete stored message
                case "4":

                    System.out.print("Enter Message Hash of the message to delete: ");
                    String hash = input.nextLine();

                    Message.deleteStoredMessage(hash);

                    break;

                // Display report
                case "5":

                    Message.displayReport();

                    break;

                // Display longest message
                case "6":

                    Message.displayLongestMessage();

                    break;

                // Return to main menu
                case "7":

                    back = true;

                    break;

                default:

                    System.out.println("Invalid option. Please choose between 1 and 7.");
            }
        }
    }
}