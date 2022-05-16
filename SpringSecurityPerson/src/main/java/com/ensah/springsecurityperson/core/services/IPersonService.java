package com.ensah.springsecurityperson.core.services;

import com.ensah.springsecurityperson.core.bo.Person;

import java.util.List;

public interface IPersonService {
    public void addPerson(Person pPerson);

    public void updatePerson(Person pPerson);

    public List<Person> getAllPersons();

    public void deletePerson(Long id);

    public Person getPersonById(Long id);

}
