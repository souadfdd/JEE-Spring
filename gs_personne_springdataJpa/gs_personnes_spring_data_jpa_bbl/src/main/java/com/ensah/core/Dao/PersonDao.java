package com.ensah.core.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ensah.core.bo.Person;

public interface PersonDao extends JpaRepository<Person, Long>, CustomizedPersonDao {
    List<Person> getPersonByNationalIdNumber(String nid);

}
