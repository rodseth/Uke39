/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import dto.PersonDTO;
import dto.PersonsDTO;
import exceptions.GenericException;
import exceptions.MissingInputException;
import exceptions.PersonNotFoundException;

public interface IPersonFacade {
  public PersonDTO addPerson(String fName, String lName, String phone,String street, String city, String zip) throws MissingInputException;  
  public PersonDTO deletePerson(int id) throws PersonNotFoundException;
  public PersonDTO getPerson(int id) throws PersonNotFoundException; 
  public PersonsDTO getAllPersons();  
  public PersonDTO editPerson(PersonDTO p) throws PersonNotFoundException, MissingInputException;  
}
