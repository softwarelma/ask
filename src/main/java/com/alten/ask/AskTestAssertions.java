package com.alten.ask;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.alten.ask.model.AskModel;
import com.alten.ask.model.orm.AskInvoiceDetail;
import com.alten.ask.model.orm.AskInvoiceHead;
import com.alten.ask.model.orm.AskProduct;

//@Controller
public class AskTestAssertions extends Assert implements Serializable {

	private static final long serialVersionUID = 1L;
	private static AskLogger logger = AskLoggerManager.getLogger(null, AskTestAssertions.class.getName());

	@Before
	public void before() {
		logger.log(Level.INFO, "Running AskTestAssertions before() beginning...");
		Timestamp timestamp = AskAbstractStaticMethods.getCurrentTimestamp();
		boolean ok = true;

		try {
			AskModel.initFakeCodes(timestamp, null, null);
			AskModel.initFakeProd(timestamp, null);
		} catch (AskException e) {
			ok = false;
		}

		logger.log(Level.INFO, "Running AskTestAssertions before() ended with " + (ok ? "NO" : "SOME") + " exception.");
	}

	@Test
	public void testAssertions() {
		logger.log(Level.INFO, "Running AskTestAssertions testAssertions() beginning...");

		boolean ok = true;
		BigDecimal totalPrice = null;
		List<AskInvoiceDetail> askInvoiceDetails;
		Timestamp timestamp = AskAbstractStaticMethods.getCurrentTimestamp();
		AskInvoiceHead askInvoiceHead;
		AskInvoiceDetail askInvoiceDetail;
		AskProduct askProduct;

		/*
		 * INPUT 1
		 */

		askInvoiceDetails = new ArrayList<AskInvoiceDetail>();
		askInvoiceHead = new AskInvoiceHead(true, timestamp, "INPUT 1", timestamp, null, timestamp, null,
				askInvoiceDetails, null);

		// 1 book at 12.49
		askProduct = AskAbstractStaticAttributes.askProductBookEco;
		askInvoiceDetail = new AskInvoiceDetail(timestamp, "1 book at 12.49", askProduct.getEan(),
				askProduct.getImported(), timestamp, askProduct.getPrice(), askProduct.getDescription(), null,
				askProduct.getAskCodeDetail(), askInvoiceHead, askProduct, 1);
		askInvoiceHead.addAskInvoiceDetail(askInvoiceDetail);

		// 1 music CD at 14.99
		askProduct = AskAbstractStaticAttributes.askProductMusicMorricone;
		askInvoiceDetail = new AskInvoiceDetail(timestamp, "1 music CD at 14.99", askProduct.getEan(),
				askProduct.getImported(), timestamp, askProduct.getPrice(), askProduct.getDescription(), null,
				askProduct.getAskCodeDetail(), askInvoiceHead, askProduct, 1);
		askInvoiceHead.addAskInvoiceDetail(askInvoiceDetail);

		// 1 chocolate bar at 0.85
		askProduct = AskAbstractStaticAttributes.askProductFoodNovi;
		askInvoiceDetail = new AskInvoiceDetail(timestamp, "1 chocolate bar at 0.85", askProduct.getEan(),
				askProduct.getImported(), timestamp, askProduct.getPrice(), askProduct.getDescription(), null,
				askProduct.getAskCodeDetail(), askInvoiceHead, askProduct, 1);
		askInvoiceHead.addAskInvoiceDetail(askInvoiceDetail);

		try {
			totalPrice = AskAbstractStaticMethods.getTotalPrice(null, AskAbstractStaticAttributes.TEST_USER,
					askInvoiceHead);
		} catch (AskException e) {
			ok = false;
		}

		// from output 1
		assertEquals(totalPrice, AskAbstractStaticAttributes.TEST_RESULT_1);

		/*
		 * INPUT 2
		 */

		askInvoiceDetails = new ArrayList<AskInvoiceDetail>();
		askInvoiceHead = new AskInvoiceHead(true, timestamp, "INPUT 2", timestamp, null, timestamp, null,
				askInvoiceDetails, null);

		// 1 imported box of chocolates at 10.00
		askProduct = AskAbstractStaticAttributes.askProductFoodLindt;
		askInvoiceDetail = new AskInvoiceDetail(timestamp, "1 imported box of chocolates at 10.00",
				askProduct.getEan(), askProduct.getImported(), timestamp, askProduct.getPrice(),
				askProduct.getDescription(), null, askProduct.getAskCodeDetail(), askInvoiceHead, askProduct, 1);
		askInvoiceHead.addAskInvoiceDetail(askInvoiceDetail);

		// 1 imported bottle of perfume at 47.50
		askProduct = AskAbstractStaticAttributes.askProductPerfumeYSL;
		askInvoiceDetail = new AskInvoiceDetail(timestamp, "1 imported bottle of perfume at 47.50",
				askProduct.getEan(), askProduct.getImported(), timestamp, askProduct.getPrice(),
				askProduct.getDescription(), null, askProduct.getAskCodeDetail(), askInvoiceHead, askProduct, 1);
		askInvoiceHead.addAskInvoiceDetail(askInvoiceDetail);

		try {
			totalPrice = AskAbstractStaticMethods.getTotalPrice(null, AskAbstractStaticAttributes.TEST_USER,
					askInvoiceHead);
		} catch (AskException e) {
			ok = false;
		}

		// from output 2
		assertEquals(totalPrice, AskAbstractStaticAttributes.TEST_RESULT_2);

		/*
		 * INPUT 3
		 */

		askInvoiceDetails = new ArrayList<AskInvoiceDetail>();
		askInvoiceHead = new AskInvoiceHead(true, timestamp, "INPUT 3", timestamp, null, timestamp, null,
				askInvoiceDetails, null);

		// 1 imported bottle of perfume at 27.99
		askProduct = AskAbstractStaticAttributes.askProductPerfumeBoss;
		askInvoiceDetail = new AskInvoiceDetail(timestamp, "1 imported bottle of perfume at 27.99",
				askProduct.getEan(), askProduct.getImported(), timestamp, askProduct.getPrice(),
				askProduct.getDescription(), null, askProduct.getAskCodeDetail(), askInvoiceHead, askProduct, 1);
		askInvoiceHead.addAskInvoiceDetail(askInvoiceDetail);

		// 1 bottle of perfume at 18.99
		askProduct = AskAbstractStaticAttributes.askProductPerfumeArmani;
		askInvoiceDetail = new AskInvoiceDetail(timestamp, "1 bottle of perfume at 18.99", askProduct.getEan(),
				askProduct.getImported(), timestamp, askProduct.getPrice(), askProduct.getDescription(), null,
				askProduct.getAskCodeDetail(), askInvoiceHead, askProduct, 1);
		askInvoiceHead.addAskInvoiceDetail(askInvoiceDetail);

		// 1 packet of headache pills at 9.75
		askProduct = AskAbstractStaticAttributes.askProductMedicineMoment;
		askInvoiceDetail = new AskInvoiceDetail(timestamp, "1 packet of headache pills at 9.75", askProduct.getEan(),
				askProduct.getImported(), timestamp, askProduct.getPrice(), askProduct.getDescription(), null,
				askProduct.getAskCodeDetail(), askInvoiceHead, askProduct, 1);
		askInvoiceHead.addAskInvoiceDetail(askInvoiceDetail);

		// 1 box of imported chocolates at 11.25
		askProduct = AskAbstractStaticAttributes.askProductFoodKinder;
		askInvoiceDetail = new AskInvoiceDetail(timestamp, "1 box of imported chocolates at 11.25",
				askProduct.getEan(), askProduct.getImported(), timestamp, askProduct.getPrice(),
				askProduct.getDescription(), null, askProduct.getAskCodeDetail(), askInvoiceHead, askProduct, 1);
		askInvoiceHead.addAskInvoiceDetail(askInvoiceDetail);

		try {
			totalPrice = AskAbstractStaticMethods.getTotalPrice(null, AskAbstractStaticAttributes.TEST_USER,
					askInvoiceHead);
		} catch (AskException e) {
			ok = false;
		}

		// from output 3
		assertEquals(totalPrice, AskAbstractStaticAttributes.TEST_RESULT_3);

		/*
		 * VERIFYING THE EXCEPTIONS
		 */

		logger.log(Level.INFO, "Running AskTestAssertions testAssertions() ended with " + (ok ? "NO" : "SOME")
				+ " exception.");

		// TIPS, other possibilities would be

		// Assert.assertEquals(str1, str2);

		// Check that a condition is true
		// assertTrue(val1 < val2);

		// Check that a condition is false
		// assertFalse(val1 > val2);

		// Check that an object isn't null
		// assertNotNull(str1);

		// Check that an object is null
		// assertNull(str3);

		// Check if two object references point to the same object
		// assertSame(str4, str5);

		// Check if two object references not point to the same object
		// assertNotSame(str1, str3);

		// Check whether two arrays are equal to each other.
		// assertArrayEquals(expectedArray, resultArray);
	}

}
