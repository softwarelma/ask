package com.alten.ask;

import com.alten.ask.logic.AskLogic;

public class AskException extends Exception {

	private static final long serialVersionUID = 1L;
	private final AskLogger logger;

	public AskException(AskLogic askLogic, String message, String username) {
		super(message);
		logger = AskLoggerManager.getLogger(askLogic, AskException.class.getName());

		// logging this exception
		logger.log(logger.getLevel(), "Username \"" + username + "\". " + message, this);
	}

	public AskException(AskLogic askLogic, String message, String username, Exception e) {
		super(message, e);
		logger = AskLoggerManager.getLogger(askLogic, AskException.class.getName());

		if (e != null) {
			// logging the cause of this exception
			logger.log(logger.getLevel(), e.getMessage(), e);
		}

		// logging this exception
		logger.log(logger.getLevel(), "Username \"" + username + "\". " + message, this);
	}

}
