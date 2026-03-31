package de.ase.pcpartpicker.part_assembly;
import de.ase.pcpartpicker.domain.HelperClasses.User;

public class Config{
    private final int id;
    private final User user;
    private Computer computer;
    public Config(int id, User user, Computer computer) {
        this.id = id;
        this.user = user;
        this.computer = computer;
    }

    public int getId() {
        return id;
    }
    public User getUser() {
        return user;
    }
    public Computer getComputer() {
        return computer;
    }
    
}
