package com.alten.ask.model.orm;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.alten.ask.model.orm.AskCodeHead;

public interface AskCodeHeadDao extends CrudRepository<AskCodeHead, Long> {

	/**
	 * Finds all vertical tables ordered by id in descendant order.
	 */
	@Query("SELECT t FROM AskCodeHead t ORDER BY t.id DESC")
	@Override
	public List<AskCodeHead> findAll();

	/**
	 * Finds all vertical tables by description ordered by id in descendant
	 * order.
	 */
	@Query("SELECT t FROM AskCodeHead t WHERE t.description = :description ORDER BY t.id DESC")
	public List<AskCodeHead> findByDescription(@Param("description") String description);

}