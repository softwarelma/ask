package com.alten.ask.model.orm;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AskTxHeadDao extends CrudRepository<AskTxHead, Long> {

	/**
	 * Finds all transactions ordered by id in descendant order.
	 */
	@Query("SELECT t FROM AskTxHead t ORDER BY t.id DESC")
	@Override
	public List<AskTxHead> findAll();

	/**
	 * Finds all the transactions of the user.
	 */
	@Query("SELECT t FROM AskTxHead t WHERE t.description LIKE CONCAT('%(', :username, ')') ORDER BY t.id DESC")
	public List<AskTxHead> findAllTransactions(@Param("username") String username);

}
