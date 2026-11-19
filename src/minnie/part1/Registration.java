package minnie.part1;

import java.util.Scanner;

public class Registration {

    public static Login registerUser(Scanner input) {

        System.out.println("\n--- User Registration ---");

        System.out.print("Enter first name: ");
        String firstName = input.nextLine();

        System.out.print("Enter last name: ");
        String lastName = input.nextLine();

        System.out.print("Enter username: ");
        String username = input.nextLine();

        System.out.print("Enter password: ");
        String password = input.nextLine();

        System.out.print("Enter cell phone number (+27...): ");
        String cellPhone = input.nextLine();

        // Create Login object using registered details
        Login newUser = new Login(
                username, password, cellPhone, firstName, lastName
        );

        // Validation checks
        String userCheck = newUser.checkUserName();
        String passCheck = newUser.checkPasswordComplexity();
        String cellCheck = newUser.checkCellPhoneNumber();

        // Display validation results
        System.out.println(userCheck);
        System.out.println(passCheck);
        System.out.println(cellCheck);

        // Check if registration was successful
        if (userCheck.equals("Username successfully captured.")
                && passCheck.equals("Password successfully captured.")
                && cellCheck.equals("Cell phone number successfully captured.")) {

            System.out.println("\nRegistration successful!");
            return newUser;

        } else {
            System.out.println("\nRegistration failed. Please try again.");
            return null;
        }
    }
}