package pet.store.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Set;

@Data
@Entity
public class PetStore {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long petStoreID;
  private String petStoreName;
  private String petStoreAddress;
  private String petStoreCity;
  private String petStoreState;
  private String petStoreZip;
  private String petStorePhoneNumber;

  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  @ManyToMany(cascade = CascadeType.PERSIST)
  @JoinTable(name = "customer_pet_store",
      joinColumns = @JoinColumn(name = "pet_store_id"),
      inverseJoinColumns = @JoinColumn(name = "customer_id"))
  private Set<Customer> customers;

  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  @OneToMany(mappedBy = "petStore", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Employee> employees;
}