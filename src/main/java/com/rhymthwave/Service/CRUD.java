package com.rhymthwave.Service;

import java.util.List;

public interface CRUD<E,T> {
	E create(E entity);
	E update(E entity);
	Boolean delete(T key);
	E findOne(T key);
	List<E> findAll();
}
