package fi.bytecraft.spock.animals

class KennelFactory {
    Manager manager = new ManagerFactory().build()
    List<Dog> reservedDogs = [
            new DogFactory().build()
    ]
    List<Dog> puppies = [
            new DogFactory(age: 0, name: "Doggo Jr").build()
    ]

    Kennel build() {
        new Kennel(
                manager: manager,
                reservedDogs: reservedDogs,
                puppies: puppies
        )
    }
}

class DogFactory {
    int age = 1
    String name = "Default Dog Name"

    Dog build() {
        new Dog(
                age: age,
                name: name
        )
    }
}

class ManagerFactory {
    String name = "Default Manager Name"
    String email = "manager@kennel.com"

    Manager build() {
        new Manager(
                name: name,
                email: email
        )
    }
}
