package fi.bytecraft.spock.animals;

public class Cat {

    enum Sound {
        MIAU,
        HSSSSHHSSGGRRSSS
    }

    public Sound respondsTo(Dog.Sound sound) {
        Sound catRespond;

        if (sound == Dog.Sound.GRRR) {
            catRespond = Sound.HSSSSHHSSGGRRSSS;
        } else {
            catRespond = Sound.MIAU;
        }

        return catRespond;
    }

}
