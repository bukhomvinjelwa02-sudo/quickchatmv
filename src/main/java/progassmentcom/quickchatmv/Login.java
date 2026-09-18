package progassmentcom.quickchatmv;


import java.util.ArrayList;

public class Login {
    private static final ArrayList<Login> users = new ArrayList<>();

    private final String username;
    private final String password;
    private final String cellPhoneNumber;
    private final String userFirstName;
    private final String userLastName;
    private boolean registered;

    public Login(String username, String password, String cellPhoneNumber,
                 String userFirstName, String userLastName) {
        this.username = username;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
    }

    // must have an underscore and be 5 characters or less
    public boolean checkUserName() {
        return username != null && username.length() <= 5 && username.contains("_");
    }

    // must be 8+ characters and include a capital, a number and a special character
    public boolean checkPasswordComplexity() {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasCapital = true;
            } else if (Character.isDigit(c)) {
                hasNumber = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecial = true;
            }
        }

        return hasCapital && hasNumber && hasSpecial;
    }

    // must start with + and have 10 to 12 digits after it
    public boolean checkCellPhoneNumber() {
        return cellPhoneNumber != null && cellPhoneNumber.matches("^\\+\\d{10,12}$");
    }

    public String registerUser() {
        if (!checkUserName()) {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }

        if (!checkPasswordComplexity()) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }

        if (!checkCellPhoneNumber()) {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        }

        if (findUser(username) != null) {
            return "Username already exists. Please choose another username.";
        }

        registered = true;
        users.add(this);
        return "Username and password successfully captured. Cell phone number successfully added.";
    }

    public boolean isRegistered() {
        return registered;
    }

    public boolean loginUser(String enteredUsername, String enteredPassword) {
        return username != null
                && username.equals(enteredUsername)
                && password != null
                && password.equals(enteredPassword);
    }

    public String returnLoginStatus(boolean loginSuccess) {
        if (loginSuccess) {
            return "Welcome " + userFirstName + ", " + userLastName + " it is great to see you again.";
        }

        return "Username or password incorrect, please try again.";
    }

    // looks through the registered users for a matching username
    public static Login findUser(String username) {
        if (username == null) {
            return null;
        }

        for (Login user : users) {
            if (username.equals(user.username)) {
                return user;
            }
        }

        return null;
    }

    public static void clearUsers() {
        users.clear();
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getCellPhoneNumber() { return cellPhoneNumber; }
    public String getUserFirstName() { return userFirstName; }
    public String getUserLastName() { return userLastName; }
}
