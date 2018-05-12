package com.alten.ask;

import java.io.File;
import java.io.FileWriter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.logging.Level;

import com.alten.ask.logic.AskActionInterface.AskActionType;
import com.alten.ask.logic.AskClickInterface;
import com.alten.ask.logic.AskEnterInterface;
import com.alten.ask.logic.AskLogic;
import com.alten.ask.model.orm.AskInvoiceDetail;
import com.alten.ask.model.orm.AskInvoiceHead;
import com.alten.ask.model.orm.AskProduct;
import com.alten.ask.model.orm.AskUser;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public abstract class AskAbstractStaticMethods implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * if the file exists it will be overwritten
	 */
	public synchronized static void saveToFile(String text, String fullPath, AskLogic askLogic) {
		if (text == null || fullPath == null || text.equals("") || fullPath.equals("")) {
			return;
		}

		File file = new File(fullPath);

		try {
			for (int i = 0; i < 20 && file.exists(); i++) {
				Thread.sleep(100);
				file.delete();
			}

			FileWriter fileWriter = new FileWriter(file, false);
			// text += "\r\n";
			// text = text.replaceAll("\n", "\r\n");
			fileWriter.write(text, 0, text.length());
			fileWriter.close();
		} catch (Exception e) {
			try {
				throw new AskException(askLogic, "Wrapping exception for saveToFile call",
						AskAbstractStaticAttributes.NO_USER, e);
			} catch (AskException e2) {
			}
		}
	}

	/**
	 * if the file exists the text will be appended
	 */
	public synchronized static void saveToFileAppend(String text, String fullPath, AskLogic askLogic) {
		if (text == null || fullPath == null || text.equals("") || fullPath.equals("")) {
			return;
		}

		File file = new File(fullPath);

		try {
			FileWriter fileWriter = new FileWriter(file, true);
			// text += "\r\n";
			// text = text.replaceAll("\n", "\r\n");
			fileWriter.append(text, 0, text.length());
			fileWriter.close();
		} catch (Exception e) {
			try {
				throw new AskException(askLogic, "Wrapping exception for saveToFile call",
						AskAbstractStaticAttributes.NO_USER, e);
			} catch (AskException e2) {
			}
		}
	}

	public static String toStringThrowable(Throwable thrown) {
		StringBuilder sb = new StringBuilder();

		for (StackTraceElement ste : thrown.getStackTrace()) {
			sb.append("\tat ");
			sb.append(ste);
			sb.append(AskAbstractStaticAttributes.LOGGING_RETURN);
		}

		Throwable cause;

		if ((cause = thrown.getCause()) != null) {
			sb.append("Caused by: ");
			sb.append(cause.getClass().getName());
			sb.append(": ");
			sb.append(cause.getMessage());
			sb.append(AskAbstractStaticAttributes.LOGGING_RETURN);

			for (StackTraceElement ste : cause.getStackTrace()) {
				sb.append("\tat ");
				sb.append(ste);
				sb.append(AskAbstractStaticAttributes.LOGGING_RETURN);
			}
		}

		return sb.toString();
	}

	public static void validateNotNull(AskLogic askLogic, String username, Object object) throws AskException {
		if (object == null) {
			throw new AskException(askLogic, "The parameter should be not null", username);
		}
	}

	public static void validateNotNull(AskLogic askLogic, String username, Object object, String parameterName)
			throws AskException {
		validateNotNull(askLogic, username, parameterName);

		if (object == null) {
			throw new AskException(askLogic, "The parameter " + parameterName + " should be not null", username);
		}
	}

	public static void validateInclusion(AskLogic askLogic, String username, Object object, String parameterName,
			Object[] array) throws AskException {
		validateNotNull(askLogic, username, object, parameterName);
		validateNotNull(askLogic, username, array, "array");

		for (Object obj : array) {
			if (object.equals(obj)) {
				return;
			}
		}

		throw new AskException(askLogic, "The object \"" + object + "\" is not included in the array: "
				+ toStringArray(array), username);
	}

	public static String toStringArray(Object[] array) {
		if (array == null) {
			return "";
		}

		StringBuilder sb = new StringBuilder("[");
		String sep = "";

		for (Object o : array) {
			sb.append(sep);
			sep = ", ";
			sb.append(o == null ? "" : o);
		}

		sb.append("]");
		return sb.toString();
	}

	public static void addFocus(String username, AbstractTextField textField, final AskLogic askLogic,
			final AskEnterInterface enter, final Object objectForOnFocus, AskLogger logger) throws AskException {
		if (!AskAbstractStaticAttributes.addFocus) {
			return;
		}

		validateNotNull(askLogic, username, textField, "textField");
		validateNotNull(askLogic, username, askLogic, "askLogic");
		validateNotNull(askLogic, username, enter, "enter");
		logger.log(Level.CONFIG, "Doing addFocusListener for " + enter.getClass().getName() + ".");

		textField.addFocusListener(new FieldEvents.FocusListener() {

			private static final long serialVersionUID = 2L;

			@Override
			public void focus(FocusEvent event) {
				try {
					logger.log(Level.CONFIG, "Doing onFocus for " + enter.getClass().getName() + ".");
					enter.onFocus(objectForOnFocus);
					askLogic.setEnter(enter);
				} catch (AskException e) {
				}
			}
		});
		addBlur(username, textField, askLogic, enter, logger);
	}

	public static void addBlur(String username, AbstractTextField textField, final AskLogic askLogic,
			final AskEnterInterface enter, AskLogger logger) throws AskException {
		if (!AskAbstractStaticAttributes.addBlur) {
			return;
		}

		validateNotNull(askLogic, username, textField, "textField");
		validateNotNull(askLogic, username, askLogic, "askLogic");
		validateNotNull(askLogic, username, enter, "enter");
		logger.log(Level.CONFIG, "Doing addBlurListener for " + enter.getClass().getName() + ".");

		textField.addBlurListener(new FieldEvents.BlurListener() {

			private static final long serialVersionUID = 2L;

			@Override
			public void blur(BlurEvent event) {
				try {
					logger.log(Level.CONFIG, "Doing onBlur for " + enter.getClass().getName() + ".");
					askLogic.setEnter(null);
				} catch (AskException e) {
				}
			}
		});
	}

	public static void addClick(AskLogic askLogic, String username, Button button, final AskClickInterface askClick,
			final AskActionType askActionType) throws AskException {
		if (!AskAbstractStaticAttributes.addClick) {
			return;
		}

		validateNotNull(askLogic, username, button, "button");
		validateNotNull(askLogic, username, askClick, "askClick");
		// clickType could be null

		button.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 2L;

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					askClick.doClick(askActionType);
				} catch (AskException e) {
				}
			}
		});
	}

	/**
	 * Calculating tax by 1 product, rounding up to the nearest 0.05
	 */
	public static BigDecimal getTax(AskLogic askLogic, String username, AskProduct askProduct) throws AskException {
		validateNotNull(askLogic, username, askProduct, "askProduct");
		validateNotNull(askLogic, username, askProduct.getPrice(), "price");
		BigDecimal taxImport;

		if (askProduct.getImported()) {
			taxImport = askProduct.getPrice().multiply(BigDecimal.valueOf(0.05));
		} else {
			taxImport = BigDecimal.ZERO;
		}

		BigDecimal taxSale;

		if (askProduct.getAskCodeDetail().getDescription().equals(AskAbstractStaticAttributes.CODE_BOOK)
				|| askProduct.getAskCodeDetail().getDescription().equals(AskAbstractStaticAttributes.CODE_FOOD)
				|| askProduct.getAskCodeDetail().getDescription().equals(AskAbstractStaticAttributes.CODE_MEDICINE)) {
			taxSale = BigDecimal.ZERO;
		} else {
			taxSale = askProduct.getPrice().multiply(BigDecimal.valueOf(0.1));
		}

		BigDecimal taxTotal = taxImport.add(taxSale);
		// rounding up to the nearest 0.05
		taxTotal = taxTotal.multiply(BigDecimal.valueOf(2)).setScale(1, BigDecimal.ROUND_UP)
				.divide(BigDecimal.valueOf(2));
		// setting 2 decimals, no rounding needed
		taxTotal = taxTotal.setScale(2);
		return taxTotal;
	}

	/**
	 * Calculating tax by detail, n products
	 */
	public static BigDecimal getTax(AskLogic askLogic, String username, AskInvoiceDetail askInvoiceDetail)
			throws AskException {
		validateNotNull(askLogic, username, askInvoiceDetail, "askInvoiceDetail");
		AskProduct askProduct = askInvoiceDetail.getAskProduct();
		return getTax(askLogic, username, askProduct).multiply(BigDecimal.valueOf(askInvoiceDetail.getQuantity()));
	}

	/**
	 * Calculating total price by detail, n products with taxes
	 */
	public static BigDecimal getTotalPrice(AskLogic askLogic, String username, AskInvoiceDetail askInvoiceDetail)
			throws AskException {
		BigDecimal tax = getTax(askLogic, username, askInvoiceDetail);
		AskProduct askProduct = askInvoiceDetail.getAskProduct();
		return tax.add(askProduct.getPrice().multiply(BigDecimal.valueOf(askInvoiceDetail.getQuantity())));
	}

	/**
	 * Calculating total price by invoice, n details
	 */
	public static BigDecimal getTotalPrice(AskLogic askLogic, String username, AskInvoiceHead askInvoiceHead)
			throws AskException {
		BigDecimal totalPriceByInvoice = BigDecimal.ZERO;
		BigDecimal totalPriceByDetail;

		for (AskInvoiceDetail askInvoiceDetail : askInvoiceHead.getAskInvoiceDetails()) {
			totalPriceByDetail = getTotalPrice(askLogic, username, askInvoiceDetail);
			totalPriceByInvoice = totalPriceByInvoice.add(totalPriceByDetail);
		}

		return totalPriceByInvoice;
	}

	public static Timestamp getCurrentTimestamp() {
		return new Timestamp(Instant.now().toEpochMilli());
	}

	public static Timestamp getTimestamp(String s) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(AskAbstractStaticAttributes.DATE_TIME_FORMAT_STRING);
		return new Timestamp(sdf.parse(s).getTime());
	}

	public static String getString(Timestamp t) {
		Date date = new Date(t.getTime());
		// do not use .format(Object)
		SimpleDateFormat sdf = new SimpleDateFormat(AskAbstractStaticAttributes.DATE_TIME_FORMAT_STRING);
		return sdf.format(date);
	}

	public static String getAskUserDenomination(AskUser askUser) {
		return askUser.getId() + ", " + askUser.getName() + " " + askUser.getSurname() + " (" + askUser.getUsername()
				+ ")";
	}

	public static String getAskInvoiceHeadDenomination(AskInvoiceHead askInvoiceHead) {
		StringBuilder den = new StringBuilder("id " + askInvoiceHead.getId() + ", "
				+ getString(askInvoiceHead.getPurchasingTime()) + ", €" + askInvoiceHead.getPrice());
		String sep = "";

		for (AskInvoiceDetail askInvoiceDetail : askInvoiceHead.getAskInvoiceDetails()) {
			den.append(sep);
			sep = ",";
			den.append(" \r\n  \tid ");
			den.append(getAskInvoiceDetailDenomination(askInvoiceDetail));
		}

		return den.toString();
	}

	public static String getAskInvoiceDetailDenomination(AskInvoiceDetail askInvoiceDetail) {
		BigDecimal total = askInvoiceDetail.getPrice().add(askInvoiceDetail.getTax())
				.multiply(BigDecimal.valueOf(askInvoiceDetail.getQuantity()));
		String den = askInvoiceDetail.getId() + ", " + askInvoiceDetail.getDescription() + ", €" + total;
		return den;
	}

}
