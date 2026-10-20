package progassmentcom.quickchatmv;
import java.util.Scanner;
import java.util.ArrayList;

public class QuickChat {

    private static final Scanner scanner = new Scanner(System.in);
    private static Login currentUser = null;
    private static final ArrayList<Message> allMessages = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Welcome to QuickChat");

        // keep looping until the user picks exit
        boolean running = true;
        while (running) {
            running = currentUser == null ? showAuthenticationMenu() : showMainMenu();
        }

        System.out.println("Thank you for using QuickChat. Goodbye!");
        scanner.close();
    }

    // menu shown before someone logs in
    private static boolean showAuthenticationMenu() {
        System.out.println();
        System.out.println("1. Register New Account");
        System.out.println("2. Login to Existing Account");
        System.out.println("3. Exit");
        System.out.print("Select option (1-3): ");

        int choice = getIntInput();

        switch (choice) {
            case 1:
                registerNewAccount();
                break;
            case 2:
                loginExistingAccount();
                break;
            case 3:
                return false;
            default:
                System.out.println("Invalid option. Please try again.");
        }

        return true;
    }

    private static void registerNewAccount() {
        System.out.println();
        System.out.println("Create New Account");

        System.out.print("First Name: ");
        String firstName = scanner.nextLine().trim();

        System.out.print("Last Name: ");
        String lastName = scanner.nextLine().trim();

        System.out.print("Username (contains _, max 5 chars): ");
        String username = scanner.nextLine().trim();

        System.out.print("Password (8+ chars, capital, number, special char): ");
        String password = scanner.nextLine().trim();

        System.out.print("Cell Phone (+27xxxxxxxxxx): ");
        String cellPhone = scanner.nextLine().trim();

        Login newUser = new Login(username, password, cellPhone, firstName, lastName);
        String result = newUser.registerUser();

        System.out.println();
        System.out.println(result);

        if (newUser.isRegistered()) {
            System.out.println("Account created. You can now log in.");
        }
    }

    private static void loginExistingAccount() {
        System.out.println();
        System.out.println("Login");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        // look up the account by username first, then check the password
        Login user = Login.findUser(username);

        if (user == null) {
            System.out.println();
            System.out.println("No account found with that username.");
            return;
        }

        boolean success = user.loginUser(username, password);
        System.out.println();
        System.out.println(user.returnLoginStatus(success));

        if (success) {
            currentUser = user;
        }
    }

    // menu shown after someone logs in
    private static boolean showMainMenu() {
        System.out.println();
        System.out.println("1. Send Messages");
        System.out.println("2. View Message Report");
        System.out.println("3. Logout");
        System.out.print("Select option (1-3): ");

        int choice = getIntInput();

        switch (choice) {
            case 1:
                handleSendMessages();
                break;
            case 2:
                displayMessageReport();
                break;
            case 3:
                currentUser = null;
                System.out.println("Logged out successfully.");
                return true;
            default:
                System.out.println("Invalid option.");
        }

        return true;
    }

    private static void handleSendMessages() {
        System.out.println();
        System.out.print("How many messages? (1-10): ");
        int count = getIntInput();

        if (count <= 0 || count > 10) {
            System.out.println("Enter between 1-10 messages.");
            return;
        }

        for (int i = 1; i <= count; i++) {
            processMessage(i, count);
        }
    }

    private static void processMessage(int current, int total) {
        System.out.println();
        System.out.println("Message " + current + " of " + total);

        System.out.print("Recipient (+27xxxxxxxxxx): ");
        String recipient = scanner.nextLine().trim();

        System.out.print("Message (max 250 chars): ");
        String payload = scanner.nextLine().trim();

        Message msg = new Message(current, recipient, payload);

        // stop early if the recipient number is not valid
        String recipientStatus = msg.checkRecipientCell();
        if (!recipientStatus.contains("successfully")) {
            System.out.println(recipientStatus);
            return;
        }

        // stop early if the message is too long
        String lengthStatus = msg.checkMessageLength();
        if (!lengthStatus.equals("Message ready to send.")) {
            System.out.println(lengthStatus);
            return;
        }

        msg.createMessageHash();
        System.out.println(msg.getMessageDisplay());

        System.out.println();
        System.out.println("1. Send Message");
        System.out.println("2. Store Message");
        System.out.println("0. Delete Message");
        System.out.print("Select option: ");

        int choice = getIntInput();
        System.out.println(msg.sentMessage(choice));

        allMessages.add(msg);
    }

    private static void displayMessageReport() {
        if (allMessages.isEmpty()) {
            System.out.println();
            System.out.println("No messages to display.");
            return;
        }

        System.out.println();
        System.out.println("Total Messages: " + allMessages.size());
        System.out.println("Sent Messages: " + Message.getSentMessages().size());
        System.out.println();

        for (int i = 0; i < allMessages.size(); i++) {
            System.out.println("Message " + (i + 1) + ":");
            System.out.println(allMessages.get(i).getMessageDisplay());
            System.out.println();
        }
    }

    // reads a line and turns it into a number, -1 if it's not a valid number
    private static int getIntInput() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
