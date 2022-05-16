package com.ensah.core.dao;

import com.ensah.core.bo.Person;
import com.genericdao.api.GenericDao;

/**
 * Interface du DAO
 *
 *
 */
public interface IPersonDao extends GenericDao<Person, Long> {

	Person getPersonByNationalIdNumber(String nid);

}
