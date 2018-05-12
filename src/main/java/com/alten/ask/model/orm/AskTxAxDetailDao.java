package com.alten.ask.model.orm;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AskTxAxDetailDao extends CrudRepository<AskTxAxDetail, Long> {

	/**
	 * Finds all actions ordered by id in descendant order.
	 */
	@Query("SELECT t FROM AskTxAxDetail t ORDER BY t.id DESC")
	@Override
	public List<AskTxAxDetail> findAll();

}
