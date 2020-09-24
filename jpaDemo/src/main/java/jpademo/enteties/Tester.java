/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpademo.enteties;

import dto.PersonStyleDTO;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author MariHaugen
 */
public class Tester {
    
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        
        Person p1 = new Person("Frank", 2012);
        Person p2 = new Person("Mari", 1985);
        
        Address a1 = new Address("Stensebyvejen 2", 3730, "Nexø");
        Address a2 = new Address("Gøteborggate 14g", 0566, "Oslo");
        
        Fee f1 = new Fee(100);
        Fee f2 = new Fee(200);
        Fee f3 = new Fee(300);
        
        SwimStyle s1 = new SwimStyle("ButterFly");
        SwimStyle s2 = new SwimStyle("Crawl");
        SwimStyle s3 = new SwimStyle("Breast");
        SwimStyle s4 = new SwimStyle("Back");
        
        p1.setAddress(a1);
        p2.setAddress(a2);
        
        p1.addFee(f1);
        p1.addFee(f2);
        p2.addFee(f3);
        
        p1.addSwimStyle(s1);
        p2.addSwimStyle(s4);
        p2.addSwimStyle(s3);
        p1.addSwimStyle(s4);
        p2.addSwimStyle(s2);
        
        
        
        
        em.getTransaction().begin();
            em.persist(p1);
            em.persist(p2);
        em.getTransaction().commit();
        
        em.getTransaction().begin();
            p1.removeSwimStyle(s4);
        em.getTransaction().commit();
        
        System.out.println("P1: " + p1.getP_id() + ", " + p1.getName());
        System.out.println("P2: " + p2.getP_id() + ", " + p2.getName());
        
        System.out.println("Franks gade: " + p1.getAddress().getStreet());
        
        System.out.println("La oss se om toveis fungerer: " + a1.getPerson().getName());
        
        System.out.println("Hvem har betalt f2: "+ f2.getPerson().getName());
        
        System.out.println("Hva har blitt betalt inn:");
        
        TypedQuery<Fee> q1 = em.createQuery("SELECT f FROM Fee f", Fee.class);
        
        List<Fee> fees = q1.getResultList();
        for (Fee f: fees) {
            System.out.println(f.getPerson().getName() + " har betalt " + f.getAmount() + " kr. Den: " + f.getPayDate() + " By: " + f.getPerson().getAddress().getCity());
            
        }
        
        TypedQuery<Person> q2 = em.createQuery("SELECT p FROM Person p", Person.class);
        List<Person> persons = q2.getResultList();
        for (Person p: persons) {
            System.out.println("\nNavn: ");
            System.out.println("  Fees:");
            for (Fee f: p.getFees()) {
                System.out.println("    Beløp: " + f.getAmount() + ", " + f.getPayDate().toString());
            }
            System.out.println("  Styles:");
            for (SwimStyle ss: p.styles) {
                System.out.println("    Style: " + ss.getStyleName());
            }
        }
        
        System.out.println("***** JPQL joins ****");
        
        //Create JPQL with constructor projections
        Query q3 = em.createQuery("SELECT new dto.PersonStyleDTO(p.name, p.year, s.styleName) FROM Person p JOIN p.styles s");
        List <PersonStyleDTO> personDetails = q3.getResultList();
        
        for (PersonStyleDTO ps: personDetails) {
            System.out.println("Navn: " + ps.getName() + ", " + ps.getYear() + ", " + ps.getSwimStyle());
        }
    }
}
            
     
    

