package com.ensah.core.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ensah.core.bo.Person;
import com.ensah.core.Dao.PersonDao;

@Service
@Transactional
public class PersonServiceImpl implements IPersonService {

	private PersonDao personDao;

	@Autowired
	public PersonServiceImpl(PersonDao pDao) {

		personDao = pDao;

	}

	public void addPerson(Person pPerson) {
		personDao.save(pPerson);
	}

	public List<Person> getAllPersons() {
		return personDao.findAll();
	}

	public void deletePerson(Long id) {
		personDao.deleteById(id);

	}

	public Person getPersonById(Long id) {
		return personDao.findById(id).get();

	}

	public void updatePerson(Person pPerson) {
		personDao.save(pPerson);

	}

	@Override
	public Person getPersonByNationalIdNumber(String nid) {
		List<Person> l = personDao.getPersonByNationalIdNumber(nid);
		return l != null && !l.isEmpty() ? l.get(0) : null;
	}

	@Override
	public List<Person> getPersonsStateOrderdByAge(String pState) {
		return personDao.findPersonsStateOrderdAge(pState);
	}

}
