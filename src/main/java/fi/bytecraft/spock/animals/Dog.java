package fi.bytecraft.spock.animals;

public class Dog {

    private int age;
    private String name;

    enum Sound {
        WUFF,
        GRRR,
        BARK
    }

    enum Food {
        MEAT,
        PORRIDGE,
        FISH
    }

    Sound reactTo(Food food, int amount){
        if (food == Food.MEAT) {
            return reactToMeat(amount);
        }

        if (food == Food.FISH) {
            return reactToFish(amount);
        }

        return Sound.GRRR;
    }

    private Sound reactToMeat(int amount) {
        boolean enoughFoodForAnyDog = amount >= 5;
        boolean enoughFoodForYoungDog = amount >= 3 && !isOldDog();
        if (enoughFoodForAnyDog || enoughFoodForYoungDog)
            return Sound.WUFF;

        return Sound.BARK;
    }

    private boolean isOldDog() {
        return age > 10;
    }

    private Sound reactToFish(int amount) {
        if (amount >= 2)
            return Sound.WUFF;

        return Sound.BARK;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
