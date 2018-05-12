package com.alten.ask;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.logging.Level;

import com.alten.ask.model.orm.AskCodeDetail;
import com.alten.ask.model.orm.AskProduct;

public abstract class AskAbstractStaticAttributes implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String DATE_TIME_FORMAT_STRING = "dd/MM/yyyy - HH:mm:ss";
	public static final String DATE_FORMAT_STRING = "dd/MM/yyyy";

	public static boolean addFocus = true;

	public static boolean addBlur = true;

	public static boolean addClick = true;

	// I/O constants
	public static boolean LOGGING_USE_FILE_HANDLER = false;
	public static final Level LOGGING_LEVEL = Level.INFO;// recommended
	public static final String LOGGING_FILE_NAME = "asklogging.txt";
	public static final String LOGGING_RETURN = "\r\n";

	public static final String CODE_BOOK = "book";
	public static final String CODE_FOOD = "food";
	public static final String CODE_MEDICINE = "medicine";

	public static final String NO_USER = "no-user";
	public static final String TEST_USER = "test-user";

	// some codes to reuse
	public static AskCodeDetail askCodeDetailRolePublic;
	public static AskCodeDetail askCodeDetailPropLanguage;
	public static AskCodeDetail askCodeDetailProductBook;
	public static AskCodeDetail askCodeDetailProductFood;
	public static AskCodeDetail askCodeDetailProductMedicine;
	public static AskCodeDetail askCodeDetailProductMusic;
	public static AskCodeDetail askCodeDetailProductPerfume;
	public static AskCodeDetail askCodeDetailTx;
	public static AskCodeDetail askCodeDetailAx;

	// all products
	public static AskProduct askProductBookEco;
	public static AskProduct askProductMusicMorricone;
	public static AskProduct askProductFoodNovi;
	public static AskProduct askProductFoodLindt;
	public static AskProduct askProductPerfumeYSL;
	public static AskProduct askProductPerfumeBoss;
	public static AskProduct askProductPerfumeArmani;
	public static AskProduct askProductMedicineMoment;
	public static AskProduct askProductFoodKinder;

	// test
	public static BigDecimal TEST_RESULT_1;
	public static BigDecimal TEST_RESULT_2;
	public static BigDecimal TEST_RESULT_3;

}
