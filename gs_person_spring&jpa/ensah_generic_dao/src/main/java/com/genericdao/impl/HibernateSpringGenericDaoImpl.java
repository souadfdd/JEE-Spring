package com.genericdao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;

import com.genericdao.api.GenericDao;
import com.genericdao.common.EntityNotFoundException;

/**
 * Classe de base pour tous les DAOs, elle implémente les méthodes CRUD
 * génériques définit par le contrat GenericDAO<T>. Cette implémentation est
 * basée sur HibernateTemplate de Spring
 * 

 * 
 * @param <T>  le type d'objet métier manipulé
 * @param <PK> le type utilisé pour l'indentifiant d'un objet métier
 */

public class HibernateSpringGenericDaoImpl<T, PK extends Serializable> implements GenericDao<T, PK> {

	/** Utilisé par tous les DAOs pour tracer les événements */
	protected final Logger TRACER;

	/** Représente la classe de l'objet métier manipulé */
	private Class<T> persistentClass;

	@Autowired
	protected HibernateTemplate hibernateTemplate;

	/**
	 * Constructeur précisant la classe de l'objet métier manipulé
	 * 
	 * @param pPersistentClass la classe de l'objet métier manipulé
	 */

	public HibernateSpringGenericDaoImpl(final Class<T> pPersistentClass) {
		TRACER = Logger.getLogger(pPersistentClass);

		TRACER.trace("a DAO has been initialised to handle objects of type  " + persistentClass);
		persistentClass = pPersistentClass;
	}

	public HibernateSpringGenericDaoImpl() {
		Type t = getClass().getGenericSuperclass();
		ParameterizedType pt = (ParameterizedType) t;
		persistentClass = (Class) pt.getActualTypeArguments()[0];
		TRACER = Logger.getLogger(persistentClass);

	}

	public T create(T pObject) {

		hibernateTemplate.save(pObject);
		return pObject;
	}

	public void update(T pObject) {

		hibernateTemplate.update(pObject);

	}

	public List<T> getAll() {
		return hibernateTemplate.loadAll(persistentClass);
	}

	public void delete(PK pId) throws EntityNotFoundException {

		T lEntity = hibernateTemplate.load(persistentClass, pId);
		hibernateTemplate.delete(lEntity);

	}

	public boolean exists(PK id) {
		try {
			findById(id);
		} catch (EntityNotFoundException e) {
			return false;
		}

		return true;
	}

	public T findById(PK pId) throws EntityNotFoundException {
		T lEntity = (T) hibernateTemplate.get(this.persistentClass, pId);

		if (lEntity == null) {

			TRACER.trace("an exception EntityNotFoundException : No entity with ID  = " + pId + " is found");
			throw new EntityNotFoundException("The entity with ID  = " + pId + " is not found");
		}

		return lEntity;
	}

	public List<T> getAllDistinct() {

		Collection<T> result = new LinkedHashSet<T>(getAll());
		return new ArrayList<T>(result);

	}

	@Override
	public List<T> getEntityByColValue(String pColumnName, String pValue) {

		DetachedCriteria subSelect = DetachedCriteria.forClass(persistentClass);

		subSelect.add(Restrictions.eq(pColumnName, pValue));

		List l = hibernateTemplate.findByCriteria(subSelect);

		// si aucun résultat trouvé
		if (l == null || l.size() == 0) {
			return new ArrayList<T>();
		}

		return l;

	}

	@Override
	public List<T> getEntityByColValue(Map<String, String> colValues) throws EntityNotFoundException {

		DetachedCriteria subSelect = DetachedCriteria.forClass(persistentClass);

		for (Map.Entry<String, String> entry : colValues.entrySet()) {
			subSelect.add(Restrictions.eq(entry.getKey(), entry.getValue()));

		}

		List l = hibernateTemplate.findByCriteria(subSelect);

		// si aucun résultat trouvé
		if (l == null || l.size() == 0) {
			return new ArrayList<T>();
		}

		return l;

	}

	@Override
	public List<T> getEntityByColValue(Map<String, String> colValues, Map<String, String> orderCols) {
		DetachedCriteria subSelect = DetachedCriteria.forClass(persistentClass);

		for (Map.Entry<String, String> entry : colValues.entrySet()) {
			subSelect.add(Restrictions.eq(entry.getKey(), entry.getValue()));

		}

		if (orderCols != null) {
			for (Map.Entry<String, String> entry : orderCols.entrySet()) {
				if ("as".equals(entry.getValue())) {
					subSelect.addOrder(Order.asc(entry.getKey()));

				} else {
					subSelect.addOrder(Order.desc(entry.getKey()));

				}
			}
		}
		for (Map.Entry<String, String> entry : orderCols.entrySet()) {

		}
		List l = hibernateTemplate.findByCriteria(subSelect);

		// si aucun résultat trouvé
		if (l == null || l.size() == 0) {
			return new ArrayList<T>();
		}

		return l;
	}

}
