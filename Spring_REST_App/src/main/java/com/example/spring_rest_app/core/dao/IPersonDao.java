package com.example.spring_rest_app.core.dao;

import com.example.spring_rest_app.core.bo.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPersonDao extends JpaRepository<Person,Long> {
}
