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

import com.genericdao.api.GenericDao;
import com.genericdao.common.DataAccessLayerException;
import com.genericdao.common.EntityNotFoundException;

/**
 * Classe de base pour tous les DAOs, elle implémente les méthodes CRUD
 * génériques définit par le contrat GenericDAO<T>. Cette implémentation est
 * basée sur Hibernate nativement
 * 
 * @version 1.1
 * 
 * @param <T>  le type d'objet métier manipulé
 * @param <PK> le type utilisé pour l'indentifiant d'un objet métier
 */
public abstract class NativeHibernateGenericDAOImpl<T, PK extends Serializable> implements GenericDao<T, PK> {

	/** La classe BO manipulé par le DAO */
	protected Class<T> boClass;

	/** Utilisé par tous les DAOs */
	protected final Logger LOGGER;

	/** la fabrique des session */
	protected SessionFactory sf;

	public NativeHibernateGenericDAOImpl(SessionFactory sf) {
		Type t = getClass().getGenericSuperclass();
		ParameterizedType pt = (ParameterizedType) t;
		boClass = (Class) pt.getActualTypeArguments()[0];
		LOGGER = Logger.getLogger(boClass);
		this.sf = sf;

	}

	public NativeHibernateGenericDAOImpl(Class<T> pClass, SessionFactory sf) {

		boClass = pClass;
		this.sf = sf;
		LOGGER = Logger.getLogger(boClass);

		LOGGER.debug("le dao de " + boClass + " a été initialisé");
	}

	/**
	 * Annule la transaction
	 * 
	 * @param tx
	 * @param ex
	 * @throws Exception
	 */
	protected void handleDaoOpError(Transaction tx, RuntimeException ex) {

		if (tx != null) {
			tx.rollback();
		}

		// On trace l'erreur
		LOGGER.error("erreur à cause de l'exception " + ex);

		// On remonte l'exception
		throw new DataAccessLayerException(ex);

	}

	protected void closeSession(Session s) {

		if (s != null && s.isOpen()) {
			s.close();
		}
	}

	protected Session getSession() {
		return sf.getCurrentSession();
	}

	protected boolean anActiveTransactionExists(Session s) {
		if (s != null && s.getTransaction() != null)

			return s.getTransaction().isActive();

		return false;
	}

	public T create(T o) {

		LOGGER.debug("appel de la méthode create");

		// On obtient la session en cours
		Session s = sf.getCurrentSession();

		PK id = null;

		// Si la couche BLL a démarré une transaction
		if (anActiveTransactionExists(s)) {
			LOGGER.debug("le DAO utilise la transaction  BLL");

			// Dans ce cas c'est la couche BLL qui gere la session et la
			// transaction
			s.persist(o);

		} else {
			LOGGER.debug("le DAO initialise sa propre transaction");

			Transaction tx = null;

			try {

				// On démarre une transaction localement
				tx = s.beginTransaction();

				s.persist(o);

				// On valide la transaction
				tx.commit();
			} catch (RuntimeException ex) {
				// En cas de problèmes en annule la transaction
				handleDaoOpError(tx, ex);
			} finally {
				// Fermer la session s'elle est encore ouverte
				closeSession(s);
			}
		}

		LOGGER.debug("fin d'appel de la méthode save avec succés ");

		return o;
	}

	public void update(T o) {

		LOGGER.debug("appel de la méthode save");

		// On obtient la session en cours
		Session s = getSession();

		// Si la couche BLL a démarré une transaction
		if (anActiveTransactionExists(s)) {

			// Dans ce cas c'est la couche BLL qui gere la session et la
			// transaction
			s.update(o);

		} else {
			LOGGER.debug("le DAO initialise sa propre transaction");

			Transaction tx = null;

			try {

				// On démarre une transaction localement
				tx = s.beginTransaction();

				s.update(o);

				// On valide la transaction
				tx.commit();
			} catch (RuntimeException ex) {
				// En cas de problèmes en annule la transaction
				handleDaoOpError(tx, ex);
			} finally {
				// Fermer la session s'elle est encore ouverte
				closeSession(s);
			}
		}

		LOGGER.debug("fin d'appel de la méthode save avec succés ");

	}

	public List<T> getAll() {

		LOGGER.debug("appel de la méthode save");

		// On obtient la session en cours
		Session s = getSession();

		List<T> list = new ArrayList<>();

		// Si la couche BLL a démarré une transaction
		if (anActiveTransactionExists(s)) {

			// Dans ce cas c'est la couche BLL qui gere la session et la
			// transaction
			CriteriaBuilder builder = s.getCriteriaBuilder();
			CriteriaQuery<T> crQuery = builder.createQuery(boClass);
			Query<T> query = s.createQuery(crQuery);
			list = query.getResultList();

		} else {
			LOGGER.debug("le DAO initialise sa propre transaction");

			Transaction tx = null;

			try {

				// On démarre une transaction localement
				tx = s.beginTransaction();
				CriteriaBuilder builder = s.getCriteriaBuilder();
				CriteriaQuery<T> crQuery = builder.createQuery(boClass);
				Root<T> root = crQuery.from(boClass);
				Query<T> query = s.createQuery(crQuery);
				list = query.getResultList();

				tx.commit();
			} catch (RuntimeException ex) {
				// En cas de problèmes en annule la transaction
				handleDaoOpError(tx, ex);
			} finally {
				// Fermer la session s'elle est encore ouverte
				closeSession(s);
			}
		}

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

		// Si la couche BLL a démarré une transaction
		if (anActiveTransactionExists(s)) {

			// Dans ce cas c'est la couche BLL qui gere la session et la
			// transactionz
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

		} else {
			LOGGER.debug("le DAO initialise sa propre transaction");

			Transaction tx = null;

			try {

				// On démarre une transaction localement
				tx = s.beginTransaction();
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

				tx.commit();
			} catch (RuntimeException ex) {
				// En cas de problèmes en annule la transaction
				handleDaoOpError(tx, ex);
			} finally {
				// Fermer la session s'elle est encore ouverte
				closeSession(s);
			}
		}

		return list;
	}

	public void delete(PK pId) throws EntityNotFoundException {

		LOGGER.debug("appel de la méthode delete");

		// On obtient la session en cours
		Session s = getSession();

		PK id = null;

		// Si la couche BLL a démarré une transaction
		if (anActiveTransactionExists(s)) {

			// Dans ce cas c'est la couche BLL qui gere la session et la
			// transaction
			T obj = (T) findById(pId);
			s.delete(obj);

		} else {
			LOGGER.debug("le DAO initialise sa propre transaction");

			Transaction tx = null;

			try {

				// On démarre une transaction localement
				tx = s.beginTransaction();
				T obj = (T) findById(pId);
				s.delete(obj);

				// On valide la transaction
				tx.commit();
			} catch (RuntimeException ex) {
				// En cas de problèmes en annule la transaction
				handleDaoOpError(tx, ex);
			} finally {
				// Fermer la session s'elle est encore ouverte
				closeSession(s);
			}
		}

		LOGGER.debug("fin d'appel de la méthode delete avec succés ");

	}

	public List<T> getAllDistinct() throws EntityNotFoundException {

		Collection<T> result = new LinkedHashSet<T>(getAll());
		return new ArrayList<T>(result);

	}

	public T findById(PK pId) throws EntityNotFoundException {
		LOGGER.debug("appel de la méthode findById");

		// On obtient la session en cours
		Session s = getSession();

		T obj = null;

		// Si la couche BLL a démarré une transaction
		if (anActiveTransactionExists(s)) {

			// Dans ce cas c'est la couche BLL qui gere la session et la
			// transaction
			obj = (T) s.get(boClass, pId);

		} else {
			LOGGER.debug("le DAO initialise sa propre transaction");

			Transaction tx = null;

			try {

				// On démarre une transaction localement
				tx = s.beginTransaction();

				obj = (T) s.get(boClass, pId);

				tx.commit();
			} catch (RuntimeException ex) {
				// En cas de problèmes en annule la transaction
				handleDaoOpError(tx, ex);
			} finally {
				// Fermer la session s'elle est encore ouverte
				closeSession(s);
			}
		}

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
