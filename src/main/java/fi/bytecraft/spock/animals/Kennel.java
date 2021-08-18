package fi.bytecraft.spock.animals;

import java.util.ArrayList;
import java.util.List;

public class Kennel {

    private Manager manager;
    private List<Dog> reservedDogs;
    private List<Dog> puppies;

    Dog reservePuppy() {
        if (puppies == null || puppies.isEmpty())
            throw new RuntimeException("No puppies to reserve");

        var puppyToReserve = puppies.remove(0);

        if (reservedDogs == null)
            reservedDogs = new ArrayList<>();

        reservedDogs.add(puppyToReserve);

        return puppyToReserve;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public List<Dog> getReservedDogs() {
        return reservedDogs;
    }

    public void setReservedDogs(List<Dog> reservedDogs) {
        this.reservedDogs = reservedDogs;
    }

    public List<Dog> getPuppies() {
        return puppies;
    }

    public void setPuppies(List<Dog> puppies) {
        this.puppies = puppies;
    }
}
