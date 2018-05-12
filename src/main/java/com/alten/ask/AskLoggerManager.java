package com.alten.ask;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.alten.ask.logic.AskLogic;

public class AskLoggerManager implements Serializable {

	private static final long serialVersionUID = 1L;

	// volatile because it needs to by synchronized between threads
	private static volatile FileHandler fileHandler;
	private static final boolean ONE_HANDLER = true;

	public static AskLogger getLogger(AskLogic askLogic, String className) {
		Logger logger = Logger.getLogger(className);
		// logger.setUseParentHandlers(false);

		/*
		 * The Levels in ascending order are:
		 * 
		 * ALL, FINEST, FINER, FINE, CONFIG, INFO, WARNING, SEVERE, OFF
		 */
		logger.setLevel(AskAbstractStaticAttributes.LOGGING_LEVEL);

		FileHandler fileHandler = null;

		if (AskAbstractStaticAttributes.LOGGING_USE_FILE_HANDLER) {
			for (Handler handler : logger.getHandlers()) {
				logger.removeHandler(handler);
			}

			fileHandler = getFileHandler(logger);
		}

		if (fileHandler != null) {
			logger.addHandler(fileHandler);
		}

		// remember doing fileHandler.close() or fileHandler.flush(), see
		// AskLogger class
		logger.info("The logger was configured correctly with level " + logger.getLevel());
		return new AskLogger(askLogic, logger, className);
	}

	private static FileHandler getFileHandler(Logger logger) {
		FileHandler fileHandler;

		if (ONE_HANDLER) {
			// if (AskLoggerManager.fileHandler != null) {
			// return AskLoggerManager.fileHandler;
			// }

			try {
				synchronized (AskLoggerManager.class) {
					if (AskLoggerManager.fileHandler != null) {
						return AskLoggerManager.fileHandler;
					}

					fileHandler = new FileHandler(AskAbstractStaticAttributes.LOGGING_FILE_NAME, false);
					AskLoggerManager.fileHandler = fileHandler;
				}
			} catch (SecurityException e) {
				logger.log(Level.SEVERE, "No " + AskAbstractStaticAttributes.LOGGING_FILE_NAME
						+ " file could be safetly created because of security matters. The file could be incomplete.",
						e);
				return null;
			} catch (IOException e) {
				logger.log(Level.SEVERE, "No " + AskAbstractStaticAttributes.LOGGING_FILE_NAME
						+ " file could be safetly created because of in/out matters. The file could be incomplete.", e);
				return null;
			}

			SimpleFormatter formatter = new SimpleFormatter();
			AskLoggerManager.fileHandler.setFormatter(formatter);
			return AskLoggerManager.fileHandler;
		}

		try {
			fileHandler = new FileHandler(AskAbstractStaticAttributes.LOGGING_FILE_NAME, false);
		} catch (SecurityException e) {
			logger.log(Level.SEVERE, "No " + AskAbstractStaticAttributes.LOGGING_FILE_NAME
					+ " file could be safetly created because of security matters. The file could be incomplete.", e);
			return null;
		} catch (IOException e) {
			logger.log(Level.SEVERE, "No " + AskAbstractStaticAttributes.LOGGING_FILE_NAME
					+ " file could be safetly created because of in/out matters. The file could be incomplete.", e);
			return null;
		}

		SimpleFormatter formatter = new SimpleFormatter();
		fileHandler.setFormatter(formatter);
		return fileHandler;
	}

}
