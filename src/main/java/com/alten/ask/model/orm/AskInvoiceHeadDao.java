package com.alten.ask.model.orm;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AskInvoiceHeadDao extends CrudRepository<AskInvoiceHead, Long> {

	/**
	 * Finds all invoices ordered by id in descendant order.
	 */
	@Query("SELECT t FROM AskInvoiceHead t ORDER BY t.id DESC")
	@Override
	public List<AskInvoiceHead> findAll();

	/**
	 * Finds the only active invoice of the user if exists.
	 */
	@Query("SELECT t FROM AskInvoiceHead t WHERE t.askUser.id = :id_user AND t.active = true")
	public AskInvoiceHead findActiveInvoice(@Param("id_user") Long idUser);

	/**
	 * Finds all purchased invoices of the user.
	 */
	@Query("SELECT t FROM AskInvoiceHead t WHERE t.askUser.id = :id_user AND t.active = false ORDER BY t.id DESC")
	public List<AskInvoiceHead> findAllNonActiveInvoices(@Param("id_user") Long idUser);

}
