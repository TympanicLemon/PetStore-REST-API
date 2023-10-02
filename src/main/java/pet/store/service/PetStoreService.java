package pet.store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pet.store.controller.model.PetStoreData;
import pet.store.dao.PetStoreDao;
import pet.store.entity.PetStore;

import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class PetStoreService {

  @Autowired
  PetStoreDao petStoreDao;

  public PetStoreData savePetStore(PetStoreData petStoreData) {
    PetStore petStore = findOrCreatePetStore(petStoreData.getPetStoreID());
    copyPetStoreFields(petStore, petStoreData);

    PetStore dbPetStore = petStoreDao.save(petStore);
    return new PetStoreData(dbPetStore);
  }

  private void copyPetStoreFields(PetStore petStore, PetStoreData petStoreData) {
    petStore.setPetStoreCity(petStoreData.getPetStoreCity());
    petStore.setPetStoreName(petStoreData.getPetStoreName());
    petStore.setPetStoreAddress(petStoreData.getPetStoreAddress());
    petStore.setPetStoreState(petStoreData.getPetStoreState());
    petStore.setPetStorePhone(petStoreData.getPetStorePhone());
    petStore.setPetStoreZip(petStoreData.getPetStoreZip());
  }

  private PetStore findOrCreatePetStore(Long petStoreID) {
    PetStore petStore;

    if(Objects.isNull(petStoreID)) {
      petStore = new PetStore();
    } else {
      petStore = findPetStoreById(petStoreID);
    }

    return petStore;
  }

  private PetStore findPetStoreById(Long petStoreID) {
    return petStoreDao.findById(petStoreID).orElseThrow(() -> new NoSuchElementException("Pet store with Id=" + petStoreID + " does not exist."));
  }
}
