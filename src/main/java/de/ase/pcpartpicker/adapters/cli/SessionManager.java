package de.ase.pcpartpicker.adapters.cli; 

import de.ase.pcpartpicker.adapters.sqlite.repositories.*;
import de.ase.pcpartpicker.domain.HelperClasses.User;

public class SessionManager {
    private static BaseRepository<User> userRepository; 
    private static User currentUser; 

    public static User getCurrentUser() {
        // Wie kriege ich die Id des aktuellen Users.
        currentUser = userRepository.findById(1);
        return currentUser;
    }


}