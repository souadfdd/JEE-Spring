
package com.ensah.core.dao;

import com.ensah.core.bo.Person;
import com.genericdao.impl.HibernateSpringGenericDaoImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PersonDaoImp extends HibernateSpringGenericDaoImpl<Person, Long> implements IPersonDao {

    public PersonDaoImp() {
        super(Person.class);
    }

    @Override
    public Person getPersonByNationalIdNumber(String nid) {

        List<Person> list = getEntityByColValue("nationalIdNumber", nid);
        return list!=null && !list.isEmpty() ? list.get(0)  : null;
    }



}

