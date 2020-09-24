package facades;

import dto.PersonDTO;
import dto.PersonsDTO;
import entities.Person;
import exceptions.PersonNotFoundException;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 *
 * Rename Class to a relevant name Add add relevant facade methods
 */
public class PersonFacade implements IPersonFacade {

    private static PersonFacade instance;
    private static EntityManagerFactory emf;
    
    //Private Constructor to ensure Singleton
    private PersonFacade() {}
    
    
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
    public long getPersonCount(){
        EntityManager em = emf.createEntityManager();
        try{
            long personCount = (long)em.createQuery("SELECT COUNT(p) FROM Person p").getSingleResult();
            return personCount;
        }finally{  
            em.close();
        }
        
    }

    @Override
    public PersonDTO addPerson(String fName, String lName, String phone) {
        
        EntityManager em = getEntityManager();
        Person person = new Person(fName, lName, phone);
        try {
            em.getTransaction().begin();
            em.persist(person);
            em.getTransaction().commit();
            PersonDTO pDTO = new PersonDTO(person);
             return pDTO;
            } finally {
            em.close();
           
        }
        
    }

    @Override
    public PersonDTO deletePerson(int id) {
        EntityManager em = getEntityManager();
        try{
            em.getTransaction().begin();
            Person p = em.find(Person.class, id);
            PersonDTO pDelete = new PersonDTO(p);
            em.remove(p);
            em.getTransaction().commit();
            
            return pDelete;
        }finally{
            em.close();
        }
        
    }

    @Override
    public PersonDTO getPerson(int id) throws PersonNotFoundException {
            EntityManager em = getEntityManager();
            Person p = em.find(Person.class, id);
            if (p == null) {
                throw new PersonNotFoundException("Kan ikke finne en person med dette id");
            } else{
             try {
             PersonDTO pFind = new PersonDTO(p);
             return pFind;
        } finally {
            em.close();
        }
     }
    }
            

    @Override
    public PersonsDTO getAllPersons() {
        EntityManager em = getEntityManager();
        
            TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p", Person.class);
            List<Person> persons = query.getResultList();
            PersonsDTO allPersons =new PersonsDTO(persons);
            
            return allPersons;
    }

    @Override
    public PersonDTO editPerson(PersonDTO p) throws PersonNotFoundException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Person person = em.find(Person.class, p.getId());
            person.setFirstName(p.getfName());
            person.setLastName(p.getlName());
            person.setPhone(p.getPhone());
            person.setLastEdited(new Date());
            em.getTransaction().commit();
            
            return new PersonDTO(person);
            
        }finally {
            em.close();
        }
    }

}
