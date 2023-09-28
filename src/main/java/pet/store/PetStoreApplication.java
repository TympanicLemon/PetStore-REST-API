package pet.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PetStoreApplication {
  public static void main(String[] args) {
    SpringApplication.run(PetStoreApplication.class, args);
    System.out.println("Hi there sir, what are you goin got do today then huh?");
  }
}