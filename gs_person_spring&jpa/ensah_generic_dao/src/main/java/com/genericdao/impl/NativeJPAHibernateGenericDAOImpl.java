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
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;

import com.genericdao.api.GenericDao;
import com.genericdao.common.DataAccessLayerException;
import com.genericdao.common.EntityNotFoundException;

/**
 * Classe de base pour tous les DAOs, elle implémente les méthodes CRUD
 * génériques définit par le contrat GenericDAO<T>. Cette implémentation est
 * basée sur JPA et Hibernate
 *
 * 
 * @param <T>  le type d'objet métier manipulé
 * @param <PK> le type utilisé pour l'indentifiant d'un objet métier
 */
public abstract class NativeJPAHibernateGenericDAOImpl<T, PK extends Serializable> implements GenericDao<T, PK> {

	/** La classe BO manipulé par le DAO */
	protected Class<T> boClass;

	/** Utilisé par tous les DAOs */
	protected final Logger LOGGER;

	/** référence l'unique instance de em Factory */
	private EntityManagerFactory emf;

	public NativeJPAHibernateGenericDAOImpl(Class<T> pClass, EntityManagerFactory emf) {

		boClass = pClass;

		LOGGER = Logger.getLogger(boClass);

		this.emf = emf;

		LOGGER.debug("le dao de " + boClass + " a été initialisé");
	}

	public NativeJPAHibernateGenericDAOImpl(EntityManagerFactory emf) {
		Type t = getClass().getGenericSuperclass();
		ParameterizedType pt = (ParameterizedType) t;
		boClass = (Class) pt.getActualTypeArguments()[0];
		LOGGER = Logger.getLogger(boClass);
		this.emf = emf;

	}

	/**
	 * Annule la transaction
	 * 
	 * @param tx
	 * @param ex
	 * @throws Exception
	 */
	protected void handleDaoOpError(EntityTransaction tx, RuntimeException ex) {

		if (tx != null) {
			tx.rollback();
		}

		// On trace l'erreur
		LOGGER.error("erreur à cause de l'exception " + ex);

		// On remonte l'exception
		throw new DataAccessLayerException(ex);

	}

	protected void closeEM(EntityManager em) {

		if (em != null && em.isOpen()) {
			em.close();
		}
	}

	protected EntityManager getEM() {
		return emf.createEntityManager();
	}

	protected boolean anActiveTransactionExists(EntityManager em) {
		if (em != null && em.getTransaction() != null)

			return em.getTransaction().isActive();

		return false;
	}

	public T create(T o) {

		LOGGER.debug("appel de la méthode create");

		// On obtient EntityManager en cours
		EntityManager em = getEM();

		// Si la couche BLL a démarré une transaction
		if (anActiveTransactionExists(em)) {
			LOGGER.debug("le DAO utilise la transaction  BLL");

			// Dans ce cas c'est la couche BLL qui gere le EM et la

			em.persist(o);

		} else {
			LOGGER.debug("le DAO initialise sa propre transaction");

			EntityTransaction tx = null;

			try {

				// On démarre une transaction localement
				tx = em.getTransaction();
				tx.begin();

				em.persist(o);

				// On valide la transaction
				tx.commit();
			} catch (RuntimeException ex) {
				// En cas de problèmes en annule la transaction
				handleDaoOpError(tx, ex);
			} finally {
				// Fermer EntityManager em'elle est encore ouverte
				closeEM(em);
			}
		}

		LOGGER.debug("fin d'appel de la méthode save avec succés ");

		return o;
	}

	public void update(T o) {

		LOGGER.debug("appel de la méthode save");

		// On obtient EntityManager en cours
		EntityManager em = getEM();

		// Si la couche BLL a démarré une transaction
		if (anActiveTransactionExists(em)) {

			em.merge(o);

		} else {
			LOGGER.debug("le DAO initialise sa propre transaction");

			EntityTransaction tx = null;

			try {

				// On démarre une transaction localement
				tx = em.getTransaction();
				tx.begin();

				em.merge(o);

				// On valide la transaction
				tx.commit();
			} catch (RuntimeException ex) {
				// En cas de problèmes en annule la transaction
				handleDaoOpError(tx, ex);
			} finally {
				// Fermer EntityManager em'elle est encore ouverte
				closeEM(em);
			}
		}

		LOGGER.debug("fin d'appel de la méthode save avec succés ");

	}

	public List<T> getAll() throws EntityNotFoundException {

		LOGGER.debug("appel de la méthode save");

		// On obtient EntityManager en cours
		EntityManager em = getEM();

		List<T> list = null;

		// Si la couche BLL a démarré une transaction
		if (anActiveTransactionExists(em)) {
			CriteriaBuilder builder = em.getCriteriaBuilder();

			CriteriaQuery<T> crQuery = builder.createQuery(boClass);
			Root<T> root = crQuery.from(boClass);
			TypedQuery<T> query = em.createQuery(crQuery);
			list = query.getResultList();

		} else {
			LOGGER.debug("le DAO initialise sa propre transaction");

			EntityTransaction tx = null;

			try {

				// On démarre une transaction localement
				tx = em.getTransaction();
				tx.begin();
				CriteriaBuilder builder = em.getCriteriaBuilder();

				CriteriaQuery<T> crQuery = builder.createQuery(boClass);
				Root<T> root = crQuery.from(boClass);
				TypedQuery<T> query = em.createQuery(crQuery);
				list = query.getResultList();

				tx.commit();
			} catch (RuntimeException ex) {
				// En cas de problèmes en annule la transaction
				handleDaoOpError(tx, ex);
			} finally {
				// Fermer EntityManager em'elle est encore ouverte
				closeEM(em);
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

		// On obtient EntityManager en cours
		EntityManager em = getEM();

		List<T> list = new ArrayList<T>();

		// Si la couche BLL a démarré une transaction
		if (anActiveTransactionExists(em)) {
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

		} else {
			LOGGER.debug("le DAO initialise sa propre transaction");

			EntityTransaction tx = null;

			try {

				// On démarre une transaction localement
				tx = em.getTransaction();
				tx.begin();

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

				tx.commit();
			} catch (RuntimeException ex) {
				// En cas de problèmes en annule la transaction
				handleDaoOpError(tx, ex);
			} finally {
				// Fermer EntityManager em'elle est encore ouverte
				closeEM(em);
			}
		}

		return list;
	}

	public void delete(PK pId) throws EntityNotFoundException {

		LOGGER.debug("appel de la méthode delete");

		// On obtient EntityManager en cours
		EntityManager em = getEM();

		// Si la couche BLL a démarré une transaction
		if (anActiveTransactionExists(em)) {
			T obj = (T) em.find(boClass, pId);
			em.remove(obj);

		} else {
			LOGGER.debug("le DAO initialise sa propre transaction");

			EntityTransaction tx = null;

			try {

				// On démarre une transaction localement
				tx = em.getTransaction();
				tx.begin();
				T obj = (T) em.find(boClass, pId);
				em.remove(obj);

				// On valide la transaction
				tx.commit();
			} catch (RuntimeException ex) {
				// En cas de problèmes en annule la transaction
				handleDaoOpError(tx, ex);
			} finally {
				// Fermer EntityManager em'elle est encore ouverte
				closeEM(em);
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

		// On obtient EntityManager en cours
		EntityManager em = getEM();

		T obj = null;

		// Si la couche BLL a démarré une transaction
		if (anActiveTransactionExists(em)) {

			obj = (T) em.find(boClass, pId);

		} else {
			LOGGER.debug("le DAO initialise sa propre transaction");

			EntityTransaction tx = null;

			try {

				// On démarre une transaction localement
				tx = em.getTransaction();
				tx.begin();

				obj = (T) em.find(boClass, pId);

				tx.commit();
			} catch (RuntimeException ex) {
				// En cas de problèmes en annule la transaction
				handleDaoOpError(tx, ex);
			} finally {
				// Fermer EntityManager s'elle est encore ouverte
				closeEM(em);
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
