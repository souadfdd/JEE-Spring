package com.genericdao.api;

import java.util.List;
import java.util.Map;

import com.genericdao.common.EntityNotFoundException;

/**
 * Définit le contrat d'un DAO d'une manière générique
 *
 *
 * @param <T>  Le type de l'objet metier (l'entité) manipulé par le DAO
 * @param <PK> Le type utilisé pour les identifiants des entités (Long,
 *             Integer,...)
 */
public interface GenericDao<T, PK> {

	/**
	 * permet de persister un objet
	 * 
	 * @param o l'objet à persister
	 * @return l'identifiant autogénérée après l'enregistrement de l'objet
	 */
	T create(T o);

	/**
	 * Permet d'rattacher un objet détaché pour synchorniser son état avec la base
	 * de données
	 * 
	 * @param o l'objet détaché contenant éventuellement des modifications
	 *          effectuées en dehors d'une session
	 */
	void update(T o);

	/**
	 * Permet d'effectuer une recherche sur les entité de type T
	 * 
	 * @return Retourne toutes les entités de type T de la base de données
	 * 
	 */
	List<T> getAll();

	/**
	 * Variante de la méthode getAll qui retourne une liste de résultats en ignorant
	 * les doublants
	 * 
	 * @return toute les différentes entités de type T
	 */
	List<T> getAllDistinct();

	/**
	 * Recherche une entité par son identifiant
	 * 
	 * @param pId identifiant de l'entité
	 * @return l'entité de type T ayant l'identifiant pId
	 * @throws EntityNotFoundException si aucune entité avec l'identifiant pId n'est
	 *                                 trouvé en base de données
	 */
	T findById(PK pId) throws EntityNotFoundException;

	/**
	 * Permet de rechercher des entités en base de données avec les valeurs des
	 * propriétés de l'objet présentées sous forme de clé/valeur telle que: clé= nom
	 * de l'attribut et valeur = la valeur de l'attribut
	 * 
	 * @param colValues noms et valeurs des propriétes
	 * @return la liste des entités respectant la condition de recherche
	 */
	public List<T> getEntityByColValue(Map<String, String> colValues) throws EntityNotFoundException;

	/**
	 * Analogue à la méthode {@link getEntityByColValue(Map<String, String>
	 * colValues)} mais elle permet de récupérer les entités dans l'ordre indiqué
	 * comme paramètre à la méthode
	 * 
	 * @param colValues noms et valeurs des propriétes
	 * @return la liste des entités respectant la condition de recherche
	 */
	public List<T> getEntityByColValue(Map<String, String> colValues, Map<String, String> orderCols);

	/**
	 * Permet de rechercher des entités en base de données ayant une valeur pour une
	 * de ces proprités éagle à celle specifiée comme argument
	 * 
	 * @param pColumnName noms de la proprité
	 * @param pValue      valeur de la propriété
	 * @return la liste des entités respectant la condition de recherche
	 */
	public List<T> getEntityByColValue(String pColumnName, String pValue);

	/**
	 * Permet de supprimer une entité dans la base de données
	 * 
	 * @param pId l'identifiant de l'entité à supprimer
	 * @throws EntityNotFoundException si aucune entité avec l'identifiant pId n'est
	 *                                 trouvé en base de données
	 */
	void delete(PK pId) throws EntityNotFoundException;

	/**
	 * Permet de tester si une entité avec l'identifiant passé en paramètre existe
	 * en base de données
	 * 
	 * @param pId l'identifiant de l'entité recherchée
	 * @return true si trouvé false sinon
	 */
	boolean exists(PK pId);

}