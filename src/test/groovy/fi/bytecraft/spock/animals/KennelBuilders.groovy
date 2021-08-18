package fi.bytecraft.spock.animals

class KennelBuilders {

    def javaStyleKennel() {
        Manager managerMike = new Manager();
        managerMike.setName("Kennel Manager Mike");
        managerMike.setEmail("mike@kennel.com");

        Dog reservedDog = new Dog();
        reservedDog.setAge(1);
        reservedDog.setName("Doggo");
        List<Dog> reservedDogs = new ArrayList<>();
        reservedDogs.add(reservedDog);

        Dog firstPuppy = new Dog();
        firstPuppy.setAge(0);
        firstPuppy.setName("Doggo Jr");
        Dog secondPuppy = new Dog();
        secondPuppy.setAge(0);
        secondPuppy.setName("Doggo Jr 2");
        List<Dog> puppyDogs = new ArrayList<>();
        puppyDogs.add(firstPuppy);
        puppyDogs.add(secondPuppy);

        Kennel kennel = new Kennel();
        kennel.setManager(managerMike);
        kennel.setReservedDogs(reservedDogs);
        kennel.setPuppies(puppyDogs);

        return kennel;
    }

    def javaStyleWithExtractMethod() {
        Kennel kennel = new Kennel();
        kennel.setManager(createManager()); // extract method
        kennel.setReservedDogs(createReservedDogs());
        kennel.setPuppies(createPuppies());

        return kennel;
    }

    private static Manager createManager() {
        Manager managerMike = new Manager();
        managerMike.setName("Kennel Manager Mike");
        managerMike.setEmail("mike@kennel.com");

        return managerMike;
    }

    private static List<Dog> createReservedDogs() {
        Dog reservedDog = new Dog();
        reservedDog.setAge(1);
        reservedDog.setName("Doggo");
        List<Dog> reservedDogs = new ArrayList<>();
        reservedDogs.add(reservedDog);

        return reservedDogs;
    }

    private static List<Dog> createPuppies() {
        Dog firstPuppy = new Dog();
        firstPuppy.setAge(0);
        firstPuppy.setName("Doggo Jr");

        Dog secondPuppy = new Dog();
        secondPuppy.setAge(0);
        secondPuppy.setName("Doggo Jr 2");
        List<Dog> puppyDogs = new ArrayList<>();
        puppyDogs.add(firstPuppy);
        puppyDogs.add(secondPuppy);

        return puppyDogs;
    }

    def buildKennelWithTap() {
        // tap is a closure that enables operating on the referenced object
        // and then returning the object
        def kennel = new Kennel().tap {
            manager = new Manager().tap {
                name = "Kennel Manager Mike" // 'name =' uses the groovy shorthand assignment for manager.setName()
                email = "mike@kennel.com"
            }
            reservedDogs = [
                    new Dog().tap {
                        age = 1
                        name = "Doggo"
                    }
            ]
            puppies = [
                    new Dog().tap {
                        age = 0
                        name = "Doggo Jr"
                    },
                    new Dog().tap {
                        age = 0
                        name = "Doggo Jr 2"
                    }
            ]
        }
    }

    def buildKennelWithNamedArgsConstructor() {
        def kennel = new Kennel(
                manager: new Manager( // groovy injects non-final classes with a map-based constructor, that can be
                                      // used for named arguments in the constructor
                        name: "Manager Mary",
                        email: "mary@kennel.com"
                ),
                reservedDogs: [
                        new Dog(
                                age: 1,
                                name: "Good boy"
                        )
                ],
                puppies: [
                        new Dog(
                                age: 0,
                                name: "Good boy jr"
                        )
                ]
        )
    }

    def buildWithFactories() {
        def defaultKennel = new KennelFactory().build()

        def emptyKennel = new KennelFactory(
                reservedDogs: [], // we can override the wanted properties of the object, but otherwise use the defaults
                                  // defined in the factory class
                puppies: []
        ).build()
    }

}
