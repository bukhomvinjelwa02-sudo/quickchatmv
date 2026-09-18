package progassmentcom.quickchatmv;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

public class LoginTest {

    private Login login;

  @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        Login.clearUsers();
        login = new Login("kt_p", "Ch@ncem8", "+27718693002", "Kate", "Peters");
    }
    //  checkUserName 

    @Test
    void checkUserName_validUsername_returnsTrue() {
        assertTrue(login.checkUserName());
    }

    @Test
    void checkUserName_noUnderscore_returnsFalse() {
        Login bad = new Login("kate1", "Ch@ncem8", "+27718693002", "Kate", "Peters");
        assertFalse(bad.checkUserName());
    }

    @Test
    void checkUserName_tooLong_returnsFalse() {
        Login bad = new Login("kt_peters", "Ch@ncem8", "+27718693002", "Kate", "Peters");
        assertFalse(bad.checkUserName());
    }

    @Test
    void checkUserName_null_returnsFalse() {
        Login bad = new Login(null, "Ch@ncem8", "+27718693002", "Kate", "Peters");
        assertFalse(bad.checkUserName());
    }

    //  checkPasswordComplexity 

    @Test
    void checkPasswordComplexity_validPassword_returnsTrue() {
        assertTrue(login.checkPasswordComplexity());
    }

    @Test
    void checkPasswordComplexity_tooShort_returnsFalse() {
        Login bad = new Login("kt_p", "Ch@1", "+27718693002", "Kate", "Peters");
        assertFalse(bad.checkPasswordComplexity());
    }

    @Test
    void checkPasswordComplexity_noCapital_returnsFalse() {
        Login bad = new Login("kt_p", "ch@ncem8", "+27718693002", "Kate", "Peters");
        assertFalse(bad.checkPasswordComplexity());
    }

    @Test
    void checkPasswordComplexity_noNumber_returnsFalse() {
        Login bad = new Login("kt_p", "Ch@ncemx", "+27718693002", "Kate", "Peters");
        assertFalse(bad.checkPasswordComplexity());
    }

    @Test
    void checkPasswordComplexity_noSpecialChar_returnsFalse() {
        Login bad = new Login("kt_p", "Chancem8", "+27718693002", "Kate", "Peters");
        assertFalse(bad.checkPasswordComplexity());
    }

    //  checkCellPhoneNumber 

    @Test
    void checkCellPhoneNumber_valid_returnsTrue() {
        assertTrue(login.checkCellPhoneNumber());
    }

    @Test
    void checkCellPhoneNumber_missingPlus_returnsFalse() {
        Login bad = new Login("kt_p", "Ch@ncem8", "27718693002", "Kate", "Peters");
        assertFalse(bad.checkCellPhoneNumber());
    }

    @Test
    void checkCellPhoneNumber_tooFewDigits_returnsFalse() {
        // Only 7 digits after the +, needs 10-12
        Login bad = new Login("kt_p", "Ch@ncem8", "+1234567", "Kate", "Peters");
        assertFalse(bad.checkCellPhoneNumber());
    }

    @Test
    void checkCellPhoneNumber_tooManyDigits_returnsFalse() {
        // 13 digits after the +, needs 10-12
        Login bad = new Login("kt_p", "Ch@ncem8", "+1234567890123", "Kate", "Peters");
        assertFalse(bad.checkCellPhoneNumber());
    }

    // registerUser 

    @Test
    void registerUser_allValid_returnsSuccessMessage() {
        String result = login.registerUser();
        assertTrue(result.contains("successfully"));
        assertTrue(login.isRegistered());
    }

    @Test
    void registerUser_badUsername_returnsUsernameError() {
        Login bad = new Login("kate1", "Ch@ncem8", "+27718693002", "Kate", "Peters");
        String result = bad.registerUser();
        assertTrue(result.contains("Username is not correctly formatted"));
        assertFalse(bad.isRegistered());
    }

    @Test
    void registerUser_badPassword_returnsPasswordError() {
        Login bad = new Login("kt_p", "chancem8", "+27718693002", "Kate", "Peters");
        String result = bad.registerUser();
        assertTrue(result.contains("Password is not correctly formatted"));
    }

    @Test
    void registerUser_badCellPhone_returnsCellPhoneError() {
        Login bad = new Login("kt_p", "Ch@ncem8", "0718693002", "Kate", "Peters");
        String result = bad.registerUser();
        assertTrue(result.contains("Cell phone number incorrectly formatted"));
    }

    @Test
    void registerUser_duplicateUsername_returnsAlreadyExistsError() {
        login.registerUser();
        Login duplicate = new Login("kt_p", "Diff3rent!", "+27821234567", "Sam", "Jones");
        String result = duplicate.registerUser();
        assertTrue(result.contains("Username already exists"));
        assertFalse(duplicate.isRegistered());
    }

    // isRegistered 

    @Test
    void isRegistered_beforeRegistering_isFalse() {
        assertFalse(login.isRegistered());
    }

    // loginUser / returnLoginStatus 

    @Test
    void loginUser_correctCredentials_returnsTrue() {
        assertTrue(login.loginUser("kt_p", "Ch@ncem8"));
    }

    @Test
    void loginUser_wrongPassword_returnsFalse() {
        assertFalse(login.loginUser("kt_p", "wrongPass1!"));
    }

    @Test
    void loginUser_wrongUsername_returnsFalse() {
        assertFalse(login.loginUser("wrong_", "Ch@ncem8"));
    }

    @Test
    void returnLoginStatus_success_returnsWelcomeMessage() {
        String status = login.returnLoginStatus(true);
        assertEquals("Welcome Kate, Peters it is great to see you again.", status);
    }

    @Test
    void returnLoginStatus_failure_returnsErrorMessage() {
        String status = login.returnLoginStatus(false);
        assertEquals("Username or password incorrect, please try again.", status);
    }

    // findUser / clearUsers 

    @Test
    void findUser_afterRegistering_returnsSameUser() {
        login.registerUser();
        assertSame(login, Login.findUser("kt_p"));
    }

    @Test
    void findUser_unknownUsername_returnsNull() {
        assertNull(Login.findUser("no_one"));
    }

    @Test
    void clearUsers_removesRegisteredUsers() {
        login.registerUser();
        Login.clearUsers();
        assertNull(Login.findUser("kt_p"));
    }
}