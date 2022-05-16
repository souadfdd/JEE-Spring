package com.ensah.springsecurityperson.core.dao;

import com.ensah.springsecurityperson.core.bo.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPersonDao extends JpaRepository<Person,Long> {
}
