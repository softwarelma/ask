package com.alten.ask;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.alten.ask.logic.AskLogic;
import com.alten.ask.model.orm.AskTxAxDetail;
import com.alten.ask.model.orm.AskUser;

public class AskLogger implements Serializable {

	private static final long serialVersionUID = 1L;
	private final AskLogic askLogic;
	private transient Logger logger;
	private final String className;

	public AskLogger(AskLogic askLogic, Logger logger, String className) {
		this.askLogic = askLogic;
		this.logger = logger;
		this.className = className;
	}

	private void verifyAndResetLogger() {
		if (this.logger != null) {
			return;
		}

		this.logger = AskLoggerManager.getLogger(this.askLogic, this.className).logger;
	}

	public void log(Level level, String msg) {
		if (level == null || msg == null) {
			return;
		}

		Level thisLevel = this.getLevel();

		if (thisLevel == null || thisLevel.intValue() > level.intValue()) {
			return;
		}

		msg = msg.length() > 512 ? msg.substring(0, 512) : msg;
		this.verifyAndResetLogger();
		this.logger.log(level, msg);

		if (AskAbstractStaticAttributes.LOGGING_USE_FILE_HANDLER) {
			this.flush();
		} else {
			String timestamp = AskAbstractStaticMethods.getString(AskAbstractStaticMethods.getCurrentTimestamp());
			StringBuilder sb = new StringBuilder(timestamp);
			sb.append(": ");
			sb.append(this.className);
			sb.append(": ");
			sb.append(msg);
			sb.append(AskAbstractStaticAttributes.LOGGING_RETURN);
			AskAbstractStaticMethods.saveToFileAppend(sb.toString(), AskAbstractStaticAttributes.LOGGING_FILE_NAME,
					this.askLogic);
		}

		if (this.askLogic == null) {
			return;
		}

		AskUser askUser = this.askLogic.getAskUserFromSession();

		if (askUser == null) {
			return;
		}

		Timestamp timestamp = AskAbstractStaticMethods.getCurrentTimestamp();
		AskTxAxDetail askTxAxDetail = new AskTxAxDetail(timestamp, msg, "N/A", -1L, timestamp, null, null, null);
		this.askLogic.saveAskTxAxDetail(askUser, askTxAxDetail);
	}

	public void log(Level level, String msg, Throwable thrown) {
		if (level == null || msg == null) {
			return;
		}

		Level thisLevel = this.getLevel();

		if (thisLevel == null || thisLevel.intValue() > level.intValue()) {
			return;
		}

		msg = msg.length() > 512 ? msg.substring(0, 512) : msg;
		this.verifyAndResetLogger();
		this.logger.log(level, msg, thrown);

		if (AskAbstractStaticAttributes.LOGGING_USE_FILE_HANDLER) {
			this.flush();
		} else {
			String timestamp = AskAbstractStaticMethods.getString(AskAbstractStaticMethods.getCurrentTimestamp());
			StringBuilder sb = new StringBuilder(timestamp);
			sb.append(": ");
			sb.append(thrown.getClass().getName());
			sb.append(": ");
			sb.append(msg);
			sb.append(AskAbstractStaticAttributes.LOGGING_RETURN);
			sb.append(AskAbstractStaticMethods.toStringThrowable(thrown));
			AskAbstractStaticMethods.saveToFileAppend(sb.toString(), AskAbstractStaticAttributes.LOGGING_FILE_NAME,
					this.askLogic);
		}

		if (this.askLogic == null) {
			return;
		}

		AskUser askUser = this.askLogic.getAskUserFromSession();

		if (askUser == null) {
			return;
		}

		Timestamp timestamp = AskAbstractStaticMethods.getCurrentTimestamp();
		String entityAsString = "N/A";

		if (thrown != null && thrown.getStackTrace() != null) {
			entityAsString = AskAbstractStaticMethods.toStringArray(thrown.getStackTrace());
			entityAsString = entityAsString.length() > 512 ? entityAsString.substring(0, 512) : entityAsString;
		}

		AskTxAxDetail askTxAxDetail = new AskTxAxDetail(timestamp, msg, entityAsString, -1L, timestamp, null, null,
				null);
		this.askLogic.saveAskTxAxDetail(askUser, askTxAxDetail);
	}

	public Level getLevel() {
		this.verifyAndResetLogger();
		return this.logger.getLevel() == null ? AskAbstractStaticAttributes.LOGGING_LEVEL : this.logger.getLevel();
	}

	private void flush() {
		if (this.logger == null || this.logger.getHandlers() == null) {
			return;
		}

		for (Handler handler : this.logger.getHandlers()) {
			if (handler == null) {
				continue;
			}

			handler.flush();
		}
	}

}
