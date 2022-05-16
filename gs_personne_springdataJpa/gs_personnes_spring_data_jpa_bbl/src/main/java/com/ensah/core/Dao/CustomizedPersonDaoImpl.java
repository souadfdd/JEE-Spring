package com.ensah.core.Dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ensah.core.bo.Person;
import com.genericdao.impl.JPAHibernateGenericDAOImpl;

public class CustomizedPersonDaoImpl extends JPAHibernateGenericDAOImpl<Person, Long> implements CustomizedPersonDao {


	public List<Person> findPersonsStateOrderdAge(String pState){
		Map<String, String> creterias = new HashMap<String, String>();
		creterias.put("state", pState);
		Map<String, String> orders = new HashMap<String, String>();
		orders.put("age", "desc");
		return getEntityByColValue(creterias, orders);
	}
	

	// d'autres méthodes
}
