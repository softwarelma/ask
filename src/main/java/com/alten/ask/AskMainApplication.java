package com.alten.ask;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.logging.Level;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@PropertySources(value = { @PropertySource("classpath:/application-2.properties") })
@SpringBootApplication
public class AskMainApplication implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final AskLogger logger = AskLoggerManager.getLogger(null, AskMainApplication.class.getName());

	public static void main(String[] args) {
		if (AskAbstractStaticAttributes.LOGGING_USE_FILE_HANDLER) {
			logger.log(logger.getLevel(), "Starting Spring application...");
		} else {
			String timestamp = AskAbstractStaticMethods.getString(AskAbstractStaticMethods.getCurrentTimestamp());
			AskAbstractStaticMethods.saveToFile(timestamp + ": Starting Spring application..."
					+ AskAbstractStaticAttributes.LOGGING_RETURN, AskAbstractStaticAttributes.LOGGING_FILE_NAME, null);
		}

		if (args.length > 0 && args[0].equals("test")) {
			if (args.length > 1 && args[1].equals("fail")) {
				logger.log(logger.getLevel(), "Running failing test...");
				// wrong values
				AskAbstractStaticAttributes.TEST_RESULT_1 = BigDecimal.valueOf(29.84);
				AskAbstractStaticAttributes.TEST_RESULT_2 = BigDecimal.valueOf(65.14);
				AskAbstractStaticAttributes.TEST_RESULT_3 = BigDecimal.valueOf(74.70);
			} else {
				logger.log(logger.getLevel(), "Running default test...");
				// right values
				AskAbstractStaticAttributes.TEST_RESULT_1 = BigDecimal.valueOf(29.83);
				AskAbstractStaticAttributes.TEST_RESULT_2 = BigDecimal.valueOf(65.15);
				AskAbstractStaticAttributes.TEST_RESULT_3 = BigDecimal.valueOf(74.68);
			}

			Result result = JUnitCore.runClasses(AskTestAssertions.class);

			for (Failure failure : result.getFailures()) {
				Exception e = new Exception(failure.toString());
				logger.log(Level.INFO, e.getMessage(), e);
			}

			boolean ok = result.wasSuccessful();
			logger.log(Level.INFO, "********** Did the tests execute successfuly? " + ok + " **********");
		} else {
			logger.log(logger.getLevel(), "Running Spring-Vaadin Application...");
			SpringApplication.run(AskMainApplication.class, args);
		}
	}

}
