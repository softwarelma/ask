package com.alten.ask.model.orm;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AskProductDao extends CrudRepository<AskProduct, Long> {

	/**
	 * Finds all products ordered by description.
	 */
	@Query("SELECT p FROM AskProduct p ORDER BY p.description")
	@Override
	public List<AskProduct> findAll();

}
