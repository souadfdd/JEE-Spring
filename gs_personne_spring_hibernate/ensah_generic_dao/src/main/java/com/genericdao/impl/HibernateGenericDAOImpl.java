package com.genericdao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.hibernate.query.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

import com.genericdao.api.GenericDao;
import com.genericdao.common.EntityNotFoundException;

/**
 * Classe de base pour tous les DAOs, elle implémente les méthodes CRUD
 * génériques définit par le contrat GenericDAO<T>. Cette implémentation est
 * basée sur Hibernate. Cette implémentation necessite l'activation d'un
 * intercepteur au niveau de la couche service pour gérer les transactions,
 * ainsi elle ne peut pas etre utilisée pour gérer la transaction localement au
 * niveau DAO
 * 

 * 
 * @param <T>  le type d'objet métier manipulé
 * @param <PK> le type utilisé pour l'indentifiant d'un objet métier
 */

public abstract class HibernateGenericDAOImpl<T, PK extends Serializable> implements GenericDao<T, PK> {

	/** La classe des objets BO manipulé par le DAO */
	protected Class<T> boClass;

	/** Utilisé par tous les DAOs */
	// protected final Logger LOGGER;

	/** la fabrique des session injectée via spring */
	@Autowired
	private SessionFactory sessionFactory;

	/** Utilisé par tous les DAOs */
	protected final Logger LOGGER;

	public HibernateGenericDAOImpl() {

		Type t = getClass().getGenericSuperclass();
		ParameterizedType pt = (ParameterizedType) t;
		boClass = (Class) pt.getActualTypeArguments()[0];
		LOGGER = Logger.getLogger(boClass);

		LOGGER.debug("le dao de " + boClass + " a été initialisé");

	}

	public HibernateGenericDAOImpl(Class<T> pClass) {

		boClass = pClass;
		LOGGER = Logger.getLogger(boClass);

		LOGGER.debug("le dao de " + boClass + " a été initialisé");
	}

	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public T create(T o) {

		LOGGER.debug("appel de la méthode create");

		getSession().persist(o);

		return o;
	}

	public void update(T o) {

		LOGGER.debug("appel de la méthode save");

		getSession().update(o);

	}

	public List<T> getAll() {

		LOGGER.debug("appel de la méthode save");

		// On obtient la session en cours
		Session s = getSession();

		List<T> list = new ArrayList<>();

		// Dans ce cas c'est la couche BLL qui gere la session et la
		// transaction
		CriteriaBuilder builder = s.getCriteriaBuilder();
		CriteriaQuery<T> crQuery = builder.createQuery(boClass);
		Query<T> query = s.createQuery(crQuery);
		list = query.getResultList();

		return list;
	}

	public List<T> getEntityByColValue(String pColumnName, String pValue) {
		Map<String, String> colValues = new HashMap<String, String>();
		colValues.put(pColumnName, pValue);
		return getEntityByColValue(colValues, null);
	}

	public List<T> getEntityByColValue(Map<String, String> colValues) throws EntityNotFoundException {
		return getEntityByColValue(colValues, null);
	}

	public List<T> getEntityByColValue(Map<String, String> colValues, Map<String, String> orderCols)
			throws EntityNotFoundException {

		LOGGER.debug("appel de la méthode getByColValue");

		// On obtient la session en cours
		Session s = getSession();

		List<T> list = new ArrayList<T>();
		CriteriaBuilder builder = s.getCriteriaBuilder();
		CriteriaQuery<T> criteria = builder.createQuery(boClass);
		Root<T> root = criteria.from(boClass);
		criteria.select(root);
		for (Map.Entry<String, String> entry : colValues.entrySet()) {
			criteria.where(builder.equal(root.get(entry.getKey()), entry.getValue()));
		}

		if (orderCols != null) {
			for (Map.Entry<String, String> entry : orderCols.entrySet()) {
				if ("as".equals(entry.getValue())) {
					criteria.orderBy(builder.asc(root.get(entry.getKey())));

				} else {
					criteria.orderBy(builder.desc(root.get(entry.getKey())));

				}
			}
		}
		list = s.createQuery(criteria).getResultList();

		return list;
	}

	public void delete(PK pId) throws EntityNotFoundException {

		LOGGER.debug("appel de la méthode delete");

		getSession().delete(findById(pId));

	}

	public List<T> getAllDistinct() throws EntityNotFoundException {

		Collection<T> result = new LinkedHashSet<T>(getAll());
		return new ArrayList<T>(result);

	}

	public T findById(PK pId) throws EntityNotFoundException {
		LOGGER.debug("appel de la méthode findById");

		T obj = (T) getSession().get(boClass, pId);

		if (obj == null)
			throw new EntityNotFoundException();

		return obj;
	}

	public boolean exists(PK pId) {

		try {
			findById(pId);

		} catch (EntityNotFoundException ex) {
			return false;
		}

		return true;

	}

}
