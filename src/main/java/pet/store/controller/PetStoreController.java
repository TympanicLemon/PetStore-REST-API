package pet.store.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pet.store.controller.model.PetStoreCustomer;
import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreEmployee;
import pet.store.service.PetStoreService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pet_store")
@Slf4j
public class PetStoreController {
  @Autowired
  PetStoreService petStoreService;

  @PostMapping
  @ResponseStatus(code = HttpStatus.CREATED)
  public PetStoreData insertPetStore(@RequestBody PetStoreData petStoreData) {
    log.info("Creating pet store {}", petStoreData);
    return petStoreService.savePetStore(petStoreData);
  }

  @PutMapping("/{petStoreId}")
  public PetStoreData updatePetStore(@PathVariable Long petStoreId, @RequestBody PetStoreData petStoreData) {
    petStoreData.setPetStoreID(petStoreId);
    log.info("Updating a pet store {} with ID={}", petStoreData, petStoreId);
    return petStoreService.savePetStore(petStoreData);
  }

  @PostMapping("/{petStoreId}/employee")
  @ResponseStatus(code = HttpStatus.CREATED)
  public PetStoreEmployee insertEmployee(@PathVariable Long petStoreId, @RequestBody PetStoreEmployee petStoreEmployee) {
    log.info("Inserting an employee {} into pet store with ID={}", petStoreEmployee, petStoreId);
    return petStoreService.saveEmployee(petStoreId, petStoreEmployee);
  }

  @PostMapping("/{petStoreId}/customer")
  @ResponseStatus(code = HttpStatus.CREATED)
  public PetStoreCustomer insertCustomer(@PathVariable Long petStoreId, @RequestBody PetStoreCustomer petStoreCustomer) {
    log.info("Inserting customer " + petStoreCustomer + " into pet store with ID=" + petStoreId);
    return petStoreService.saveCustomer(petStoreId, petStoreCustomer);
  }

  @GetMapping
  public List<PetStoreData> retrieveAllPetStores() {
    log.info("Retrieving all pet stores.");
    return petStoreService.retrieveAllPetStores();
  }

  @GetMapping("/{petStoreId}")
  public PetStoreData retrievePetStoreById(@PathVariable Long petStoreId) {
    log.info("Retrieving pet store with ID={}", petStoreId);
    return petStoreService.retrievePetStoreById(petStoreId);
  }

  @DeleteMapping("/{petStoreId}")
  public Map<String, String> deletePetStoreById(@PathVariable Long petStoreId) {
    log.info("Deleteing pet store with ID={}", petStoreId);
    petStoreService.deletePetStoreById(petStoreId);
    return Map.of("Message", "Deletion of pet store with ID=" + petStoreId + " was successful");
  }
}
