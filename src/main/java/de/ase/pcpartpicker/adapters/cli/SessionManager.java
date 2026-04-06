package de.ase.pcpartpicker.adapters.cli; 
import de.ase.pcpartpicker.domain.HelperClasses.User;

public class SessionManager {

    private static User currentUser;

    public static User getcurrentUser() {
        return currentUser;
    }
    
    public static void setCurrentUser(User user) {
        currentUser = user; 
    }

    public static boolean isLoggedIn() {
        return currentUser != null; 
    }


}