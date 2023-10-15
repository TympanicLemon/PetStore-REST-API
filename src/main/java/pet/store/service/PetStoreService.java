package pet.store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pet.store.controller.model.PetStoreCustomer;
import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreEmployee;
import pet.store.dao.CustomerDao;
import pet.store.dao.PetStoreDao;
import pet.store.dao.EmployeeDao;
import pet.store.entity.Customer;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;

import java.util.*;

@Service
public class PetStoreService {
  @Autowired
  PetStoreDao petStoreDao;

  @Autowired
  EmployeeDao employeeDao;

  @Autowired
  CustomerDao customerDao;

  @Transactional(readOnly = false)
  public PetStoreData savePetStore(PetStoreData petStoreData) {
    Long petStoreId = petStoreData.getPetStoreID();
    PetStore petStore = findOrCreatePetStore(petStoreId);

    copyPetStoreFields(petStore, petStoreData);
    return new PetStoreData(petStoreDao.save(petStore));
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
    if(Objects.isNull(petStoreID)) {
      return new PetStore();
    } else {
      return findPetStoreById(petStoreID);
    }
  }

  private PetStore findPetStoreById(Long petStoreID) {
    return petStoreDao.findById(petStoreID).
      orElseThrow(() -> new NoSuchElementException("Pet store with Id=" + petStoreID + " does not exist."));
  }

  @Transactional(readOnly = false)
  public PetStoreEmployee saveEmployee(Long petStoreId, PetStoreEmployee petStoreEmployee) {
    PetStore petStore = findPetStoreById(petStoreId);
    Long employeeId = petStoreEmployee.getEmployeeId();
    Employee employee = findOrCreateEmployee(petStoreId, employeeId);

    copyEmployeeFields(employee, petStoreEmployee);

    employee.setPetStore(petStore);
    petStore.getEmployees().add(employee);

    Employee dbEmployee = employeeDao.save(employee);
    return new PetStoreEmployee(dbEmployee);
  }

  private void copyEmployeeFields(Employee employee, PetStoreEmployee petStoreEmployee) {
    employee.setEmployeeFirstName(petStoreEmployee.getEmployeeFirstName());
    employee.setEmployeeLastName(petStoreEmployee.getEmployeeLastName());
    employee.setEmployeePhone(petStoreEmployee.getEmployeePhone());
    employee.setEmployeeJobTitle(petStoreEmployee.getEmployeeJobTitle());
  }

  private Employee findOrCreateEmployee(Long petStoreId, Long employeeId) {
    if(Objects.isNull(employeeId)) {
      return new Employee();
    } else {
      return findEmployeeById(petStoreId , employeeId);
    }
  }

  private Employee findEmployeeById(Long petStoreId, Long employeeId) {
    Employee employee = employeeDao.findById(employeeId).
      orElseThrow(() -> new NoSuchElementException("Pet store with ID=" + petStoreId + " was not found."));

    if(employee.getPetStore().getPetStoreID().equals(petStoreId)) {
      return employee;
    } else {
      throw new IllegalArgumentException("Employee with ID=" + employeeId + " does not belong to pet store with ID=" + petStoreId);
    }
  }

  @Transactional(readOnly = false)
  public PetStoreCustomer saveCustomer(Long petStoreId, PetStoreCustomer petStoreCustomer) {
    PetStore petStore = findPetStoreById(petStoreId);
    Long customerId = petStoreCustomer.getCustomerId();
    Customer customer = findOrCreateCustomer(petStoreId, customerId);

    copyCustomerFields(customer, petStoreCustomer);

    petStore.getCustomers().add(customer);
    customer.getPetStores().add(petStore);

    return new PetStoreCustomer(customerDao.save(customer));
  }

  private void copyCustomerFields(Customer customer, PetStoreCustomer petStoreCustomer) {
    customer.setCustomerFirstName(petStoreCustomer.getCustomerFirstName());
    customer.setCustomerLastName(petStoreCustomer.getCustomerLastName());
    customer.setCustomerEmail(petStoreCustomer.getCustomerEmail());
  }

  private Customer findOrCreateCustomer(Long petStoreId, Long customerId) {
    if(Objects.isNull(customerId)) {
      return new Customer();
    } else {
      return findCustomerById(petStoreId, customerId);
    }
  }

  private Customer findCustomerById(Long petStoreId, Long customerId) {
    Customer customer = customerDao.findById(customerId).
      orElseThrow(() -> new NoSuchElementException("Customer with ID=" + customerId + " was not found."));

    for(PetStore petStore: customer.getPetStores()) {
      if(petStore.getPetStoreID().equals(petStoreId)) {
        return  customer;
      }
    }

    throw new IllegalArgumentException("Customer with ID=" + customerId + " is not associated with pet store with ID=" + petStoreId);
  }

  @Transactional(readOnly = true)
  public List<PetStoreData> retrieveAllPetStores() {
    List<PetStore> petStores = petStoreDao.findAll();

    List<PetStoreData> result = new LinkedList<>();

    for(PetStore petStore: petStores) {
      PetStoreData psd = new PetStoreData(petStore);

      psd.getCustomers().clear();
      psd.getEmployees().clear();

      result.add(psd);
    }

    return result;
  }

  @Transactional(readOnly = true)
  public PetStoreData retrievePetStoreById(Long petStoreId) {
    PetStore petStore = findPetStoreById(petStoreId);
    return new PetStoreData(petStore);
  }

  @Transactional(readOnly = false)
  public void deletePetStoreById(Long petStoreId) {
    PetStore petStore = findPetStoreById(petStoreId);
    petStoreDao.delete(petStore);
  }
}
