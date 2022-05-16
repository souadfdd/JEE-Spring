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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.genericdao.api.GenericDao;
import com.genericdao.common.EntityNotFoundException;

/**
 * Classe de base pour tous les DAOs, elle implémente les méthodes CRUD
 * génériques définit par le contrat GenericDAO<T>. Cette implémentation est
 * basée sur JPA et Hibernate
 * 
 * @version 1.1
 *
 * 
 * @param <T>  le type d'objet métier manipulé
 * @param <PK> le type utilisé pour l'indentifiant d'un objet métier
 */
public abstract class JPAHibernateGenericDAOImpl<T, PK extends Serializable> implements GenericDao<T, PK> {

	/** La classe BO manipulé par le DAO */
	protected Class<T> boClass;

	/** Utilisé par tous les DAOs */
	protected final Logger LOGGER;

	/** référence l'unique instance de em Factory */
	@Autowired
	private EntityManagerFactory emf;

	public JPAHibernateGenericDAOImpl() {
		Type t = getClass().getGenericSuperclass();
		ParameterizedType pt = (ParameterizedType) t;
		boClass = (Class) pt.getActualTypeArguments()[0];
		LOGGER = Logger.getLogger(boClass);

	}

	protected EntityManager getEM() {
		return emf.createEntityManager();
	}

	public T create(T o) {

		LOGGER.debug("appel de la méthode create");

		EntityManager em = getEM();
		em.persist(o);

		return o;
	}

	public void update(T o) {

		LOGGER.debug("appel de la méthode update");

		// On obtient EntityManager en cours
		EntityManager em = getEM();
		em.merge(o);

	}

	public List<T> getAll() throws EntityNotFoundException {

		LOGGER.debug("appel de la méthode getAll");

		// On obtient EntityManager en cours
		EntityManager em = getEM();

		List<T> list = new ArrayList<>();
		CriteriaBuilder builder = em.getCriteriaBuilder();

		CriteriaQuery<T> crQuery = builder.createQuery(boClass);
		Root<T> root = crQuery.from(boClass);
		TypedQuery<T> query = em.createQuery(crQuery);
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

		// On obtient EntityManager en cours
		EntityManager em = getEM();

		List<T> list = new ArrayList<T>();
		CriteriaBuilder builder = em.getCriteriaBuilder();
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
		list = em.createQuery(criteria).getResultList();

		return list;
	}

	public void delete(PK pId) throws EntityNotFoundException {

		LOGGER.debug("appel de la méthode delete");

		// On obtient EntityManager en cours
		EntityManager em = getEM();
		T obj = (T) em.find(boClass, pId);
		em.remove(obj);

	}

	public List<T> getAllDistinct() throws EntityNotFoundException {

		Collection<T> result = new LinkedHashSet<T>(getAll());
		return new ArrayList<T>(result);

	}

	public T findById(PK pId) throws EntityNotFoundException {
		LOGGER.debug("appel de la méthode findById");

		// On obtient EntityManager en cours
		EntityManager em = getEM();

		T obj = (T) em.find(boClass, pId);

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
