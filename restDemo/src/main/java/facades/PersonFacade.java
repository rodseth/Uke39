package facades;

import dto.PersonDTO;
import dto.PersonsDTO;
import entities.Address;
import entities.Person;
import exceptions.MissingInputException;
import exceptions.PersonNotFoundException;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * Rename Class to a relevant name Add add relevant facade methods
 */
public class PersonFacade implements IPersonFacade {

    private static PersonFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private PersonFacade() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static PersonFacade getFacadeExample(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PersonFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    //TODO Remove/Change this before use
    public long getPersonCount() {
        EntityManager em = emf.createEntityManager();
        try {
            long personCount = (long) em.createQuery("SELECT COUNT(p) FROM Person p").getSingleResult();
            return personCount;
        } finally {
            em.close();
        }

    }

    @Override
    public PersonDTO addPerson(String fName, String lName, String phone, String street, String city, String zip) throws MissingInputException {
        if((fName.length()== 0) || (lName.length()== 0) || (phone.length() == 0)){
        throw new MissingInputException("You have not filled out all det fields requierd for adding a person.");
    }

        EntityManager em = getEntityManager();
        Person person = new Person(fName, lName, phone);
        //Address address = new Address(street, city, zip);
        //person.setAddress(address);
        try {
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT a From Address a WHERE a.street = :street AND a.city = :city AND a.zip = :zip");
            query.setParameter ("street", street);
            query.setParameter ("city", city);
            query.setParameter ("zip", zip);
            List<Address> addresses = query.getResultList();
            if (addresses.size() > 0) {
                person.setAddress (addresses.get (0));
            } else {
                person.setAddress(new Address (street, city, zip));
            }
            em.persist(person);
            em.getTransaction().commit();
            
        } finally {
            em.close();

        }
        
            return new PersonDTO(person);

    }

    @Override
    public PersonDTO deletePerson(int id) throws PersonNotFoundException {
        EntityManager em = getEntityManager();
        Person p = em.find(Person.class, id);
        if (p == null) {
            throw new PersonNotFoundException("Could not find a person with this id, it does not exist");
        } else {
            try {
                em.getTransaction().begin();
                PersonDTO pDelete = new PersonDTO(p);
                em.remove(p);
                em.getTransaction().commit();

                return pDelete;
            } finally {
                em.close();
            }
        }
    }

    @Override
    public PersonDTO getPerson(int id) throws PersonNotFoundException {
        EntityManager em = getEntityManager();
        Person p = em.find(Person.class, id);
         if (p == null) {
                throw new PersonNotFoundException("Could not find a person with this id, it does not exist");
                
            } else {
         try {
            
            PersonDTO pFind = new PersonDTO(p);
           
                return pFind;
            
         
            }finally {
                    em.close();
                }
        }
    }

    


    @Override
    public PersonsDTO getAllPersons() {
        EntityManager em = getEntityManager();

        TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p", Person.class);
        List<Person> persons = query.getResultList();
        PersonsDTO allPersons = new PersonsDTO(persons);

        return allPersons;
    }

    @Override
    public PersonDTO editPerson(PersonDTO p) throws PersonNotFoundException, MissingInputException {
        if((p.getfName().length()== 0) || (p.getlName().length()== 0) || (p.getPhone().length() == 0)){
        throw new MissingInputException("You have not filled out all det fields requierd for editing a person.");
    }
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Person person = em.find(Person.class, p.getId());
            if (person == null){
                throw new PersonNotFoundException("Could not find a person with this id: (%id), it does not exist");
            }
            person.setFirstName(p.getfName());
            person.setLastName(p.getlName());
            person.setPhone(p.getPhone());
            Address address = new Address(p.getStreet(),p.getCity(), p.getZip());
            person.setAddress(address);
            person.setLastEdited(new Date());
            em.getTransaction().commit();

            return new PersonDTO(person);

        } finally {
            em.close();
        }
    }

}
