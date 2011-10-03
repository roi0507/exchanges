package com.exchanges.services;

import java.util.List;

public interface EntityService<T, K> {

	long countAll();

	void delete(T entity);

	T find(K id);

	List<T> findAll();

	List<T> findEntries(int firstResult, int maxResults);

	void save(T entity);

	Class<T> getManagedClass();
}
