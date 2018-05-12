package com.alten.ask.model.orm;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AskInvoiceDetailDao extends CrudRepository<AskInvoiceDetail, Long> {

	/**
	 * Finds all invoice-details ordered by id in descendant order.
	 */
	@Query("SELECT t FROM AskInvoiceDetail t ORDER BY t.id DESC")
	@Override
	public List<AskInvoiceDetail> findAll();

}
