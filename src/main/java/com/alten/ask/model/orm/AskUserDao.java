package com.alten.ask.model.orm;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AskUserDao extends CrudRepository<AskUser, Long> {

	/**
	 * Finds all users by id in descendant order.
	 */
	@Query("SELECT t FROM AskUser t ORDER BY t.id DESC")
	@Override
	public List<AskUser> findAll();

	/**
	 * Finds a user by user name and password as a search criteria.
	 */
	@Query("SELECT t FROM AskUser t WHERE t.username = :username AND t.password = :password")
	public AskUser findUser(@Param("username") String username, @Param("password") String password);

	/**
	 * Finds users by user name and password as a search criteria.
	 */
	@Query("SELECT t FROM AskUser t WHERE t.username = :username AND t.password = :password ORDER BY t.id DESC")
	public List<AskUser> findUsers(@Param("username") String username, @Param("password") String password);

}
