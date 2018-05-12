package com.alten.ask.model.orm;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AskUserPropDao extends CrudRepository<AskUserProp, Long> {

	/**
	 * Finds all user-properties ordered by id in descendant order.
	 */
	@Query("SELECT t FROM AskUserProp t ORDER BY t.id DESC")
	@Override
	public List<AskUserProp> findAll();

}
