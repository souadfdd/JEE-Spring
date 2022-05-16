package com.example.spring_rest_app.core.service;

import com.example.spring_rest_app.core.bo.Person;
import com.example.spring_rest_app.core.dao.IPersonDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PersonServiceImpl implements IPersonService  {


    @Autowired
    private IPersonDao personDao;


    @Override
    public void addPerson(Person pPerson) {
        personDao.save(pPerson);

    }

    @Override
    public void updatePerson(Person pPerson) {
        personDao.save(pPerson);
    }

    @Override
    public List<Person> getAllPersons() {
         return personDao.findAll();
    }

    @Override
    public void deletePerson(Long id) {
        personDao.deleteById(id);
    }

    @Override
    public Person getPersonById(Long id) {
        return personDao.findById(id).get();
    }
}
