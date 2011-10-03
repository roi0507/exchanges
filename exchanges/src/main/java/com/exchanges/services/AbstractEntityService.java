package com.exchanges.services;

import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import com.exchanges.domain.Entity;

@Transactional
public abstract class AbstractEntityService<T,K> implements EntityService<T,K>{

	
	@PersistenceContext
	private EntityManager entityManager;
	
	protected EntityManager getEntityManager() {
		return entityManager;
	}
	
	public long countAll(){
		return (Long) getSingleResult(MessageFormat.format("SELECT COUNT(*) FROM {0}", getEntityName()));
	}

	private Object getSingleResult(String jpql) {
		return createQuery(jpql).getSingleResult();
	}

	private Query createQuery(String jpql) {
		return getEntityManager().createQuery(jpql);
	}

	private String getEntityName() {
		return getManagedClass().getSimpleName();
	}


	public void delete(T entity){
		getEntityManager().remove(entity);
	}

	public T find(K id){
		return getEntityManager().find(getManagedClass(), id);
	}


	public List<T> findAll(){
		List entities = getEntityManager().createQuery("FROM " + getEntityName()).getResultList();
		return entities;
	}

	public List<T> findEntries(int firstResult, int maxResults){
		List entities = getEntityManager().createQuery("FROM " + getEntityName()).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
		return entities;
	}


	public void save(T entity){
		Entity<String> theEntity = (Entity)entity;
		if (theEntity.getId() == null){
			theEntity.setId(UUID.randomUUID().toString());
			getEntityManager().persist(entity);
		}
		else{
			getEntityManager().merge(entity);
			
		}
	}
	
	public abstract Class<T> getManagedClass();
}
