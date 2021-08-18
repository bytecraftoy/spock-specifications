package fi.bytecraft.spock.animals

import spock.lang.Specification

class KennelSpec extends Specification {

    // How long does it take for you to understand all the tested actions here?
    // Can you make it better, groovier and more Spocky
    def "reserving puppy"() {
        when:
            Manager manager = new Manager();
            manager.setName("Kennel Manager Mike");
            manager.setEmail("mike@kennel.com");

            Dog dog = new Dog();
            dog.setAge(1);
            dog.setName("Doggo");
            List<Dog> dogs = new ArrayList<>();
            dogs.add(dog);

            Dog dog2 = new Dog();
            dog2.setAge(0);
            dog2.setName("Doggo Jr");
            Dog dog3 = new Dog();
            dog3.setAge(0);
            dog3.setName("Doggo Jr 2");
            List<Dog> dogs2 = new ArrayList<>();
            dogs2.add(dog2);
            dogs2.add(dog3);

            Kennel kennel = new Kennel();
            kennel.setManager(manager);
            kennel.setReservedDogs(dogs);
            kennel.setPuppies(dogs2);
            kennel.reservePuppy()

        then:
            kennel.getReservedDogs().get(kennel.getReservedDogs().size()-1).getName() == "Doggo Jr"
            kennel.getPuppies().find {it.getName() == "Doggo Jr" } == null
    }

    def "reserving puppy2"() {
        expect:
            Manager manager = new Manager();
            manager.setName("Kennel Manager Mike");
            manager.setEmail("mike@kennel.com");

            Dog dog = new Dog();
            dog.setAge(1);
            dog.setName("Doggo");
            List<Dog> dogs = new ArrayList<>();
            dogs.add(dog);

            Dog dog2 = new Dog();
            dog2.setAge(0);
            dog2.setName("Doggo Jr");
            Dog dog3 = new Dog();
            dog3.setAge(0);
            dog3.setName("Doggo Jr 2");
            List<Dog> dogs2 = new ArrayList<>();
            dogs2.add(dog2);
            dogs2.add(dog3);

            Kennel kennel = new Kennel();
            kennel.setManager(manager);
            kennel.setReservedDogs(dogs);
            kennel.setPuppies(dogs2);
            def dog4 = kennel.reservePuppy()
            dog4.getName() == "Doggo Jr"
    }

    def "reserving puppy3"() {
        when:
            Manager manager = new Manager();
            manager.setName("Kennel Manager Mike");
            manager.setEmail("mike@kennel.com");

            Kennel kennel = new Kennel();
            kennel.setManager(manager);
            kennel.reservePuppy()

        then:
            thrown(RuntimeException)
    }

}
