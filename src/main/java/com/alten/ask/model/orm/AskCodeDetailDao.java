package com.alten.ask.model.orm;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AskCodeDetailDao extends CrudRepository<AskCodeDetail, Long> {

	/**
	 * Finds all codes ordered by id in descendant order.
	 */
	@Query("SELECT t FROM AskCodeDetail t ORDER BY t.id DESC")
	@Override
	public List<AskCodeDetail> findAll();

	/**
	 * Finds all codes by description ordered by id in descendant order.
	 */
	@Query("SELECT t FROM AskCodeDetail t WHERE t.description = :description ORDER BY t.id DESC")
	List<AskCodeDetail> findByDescription(@Param("description") String description);

}