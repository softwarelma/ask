package com.alten.ask.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.alten.ask.AskAbstractStaticAttributes;
import com.alten.ask.AskAbstractStaticMethods;
import com.alten.ask.AskException;
import com.alten.ask.logic.AskLogic;
import com.alten.ask.logic.AskSessionManager;
import com.alten.ask.model.orm.AskCodeDetail;
import com.alten.ask.model.orm.AskCodeDetailDao;
import com.alten.ask.model.orm.AskCodeHead;
import com.alten.ask.model.orm.AskCodeHeadDao;
import com.alten.ask.model.orm.AskInvoiceDetail;
import com.alten.ask.model.orm.AskInvoiceDetailDao;
import com.alten.ask.model.orm.AskInvoiceHead;
import com.alten.ask.model.orm.AskInvoiceHeadDao;
import com.alten.ask.model.orm.AskProduct;
import com.alten.ask.model.orm.AskProductDao;
import com.alten.ask.model.orm.AskTxAxDetail;
import com.alten.ask.model.orm.AskTxAxDetailDao;
import com.alten.ask.model.orm.AskTxHead;
import com.alten.ask.model.orm.AskTxHeadDao;
import com.alten.ask.model.orm.AskUser;
import com.alten.ask.model.orm.AskUserDao;
import com.alten.ask.model.orm.AskUserProp;
import com.alten.ask.model.orm.AskUserPropDao;

public class AskModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private final AskLogic askLogic;
	private final AskSessionManager askSessionManager;

	// all DAOs
	private AskCodeDetailDao askCodeDetailDao;
	private AskCodeHeadDao askCodeHeadDao;
	private AskInvoiceDetailDao askInvoiceDetailDao;
	private AskInvoiceHeadDao askInvoiceHeadDao;
	private AskProductDao askProductDao;
	private AskTxAxDetailDao askTxAxDetailDao;
	private AskTxHeadDao askTxHeadDao;
	private AskUserDao askUserDao;
	private AskUserPropDao askUserPropDao;

	public AskModel(AskLogic askLogic) throws AskException {
		this.askLogic = askLogic;
		this.askSessionManager = new AskSessionManager(this.askLogic);
	}

	public void init() throws AskException {
		this.initFake();
	}

	private void initFake() throws AskException {
		List<AskCodeHead> askCodeHeads = this.askCodeHeadDao.findByDescription("ROLE");

		if (!askCodeHeads.isEmpty()) {
			return;
		}

		Timestamp timestamp = AskAbstractStaticMethods.getCurrentTimestamp();
		initFakeCodes(timestamp, this.askCodeHeadDao, this.askCodeDetailDao);
		this.initFakeUser(timestamp);
		initFakeProd(timestamp, this.askProductDao);
	}

	/**
	 * ROLE: public, administrator
	 * 
	 * PRODUCT: book (exempt), food (exempt), medicine (exempt), music, perfume
	 * 
	 * AX: onFocus, onBlur, onClick, login, logout
	 * 
	 * ENTITY: AskCodeDetailDao, AskCodeHeadDao, AskInvoiceDetailDao,
	 * AskInvoiceHeadDao, AskProductDao, AskTxAxDetailDao, AskTxHeadDao,
	 * AskUserDao, AskUserPropDao
	 * 
	 * TX: login, logout, invoice, purchase
	 * 
	 * PROP: language
	 */
	public static void initFakeCodes(Timestamp timestamp, AskCodeHeadDao askCodeHeadDao,
			AskCodeDetailDao askCodeDetailDao) throws AskException {
		AskCodeHead askCodeHead;
		AskCodeDetail askCodeDetail;

		// VERTICAL TABLE "ROLE"

		askCodeHead = new AskCodeHead(timestamp, "ROLE", timestamp,
				(List<AskCodeDetail>) new ArrayList<AskCodeDetail>());
		if (askCodeHeadDao != null) {
			askCodeHeadDao.save(askCodeHead);
		}

		askCodeDetail = new AskCodeDetail(timestamp, "public", timestamp, 1, BigDecimal.valueOf(1), null, askCodeHead);
		askCodeHead.addAskCodeDetail(askCodeDetail);
		AskAbstractStaticAttributes.askCodeDetailRolePublic = askCodeDetail;
		if (askCodeDetailDao != null) {
			askCodeDetailDao.save(askCodeDetail);
		}

		askCodeDetail = new AskCodeDetail(timestamp, "administrator", timestamp, 2, BigDecimal.valueOf(2), null,
				askCodeHead);
		askCodeHead.addAskCodeDetail(askCodeDetail);
		if (askCodeDetailDao != null) {
			askCodeDetailDao.save(askCodeDetail);
		}

		// VERTICAL TABLE "PRODUCT"

		askCodeHead = new AskCodeHead(timestamp, "PRODUCT", timestamp,
				(List<AskCodeDetail>) new ArrayList<AskCodeDetail>());
		if (askCodeHeadDao != null) {
			askCodeHeadDao.save(askCodeHead);
		}

		askCodeDetail = new AskCodeDetail(timestamp, AskAbstractStaticAttributes.CODE_BOOK, timestamp, 1,
				BigDecimal.valueOf(1), null, askCodeHead);
		askCodeHead.addAskCodeDetail(askCodeDetail);
		AskAbstractStaticAttributes.askCodeDetailProductBook = askCodeDetail;
		if (askCodeDetailDao != null) {
			askCodeDetailDao.save(askCodeDetail);
		}

		askCodeDetail = new AskCodeDetail(timestamp, AskAbstractStaticAttributes.CODE_FOOD, timestamp, 2,
				BigDecimal.valueOf(2), null, askCodeHead);
		askCodeHead.addAskCodeDetail(askCodeDetail);
		AskAbstractStaticAttributes.askCodeDetailProductFood = askCodeDetail;
		if (askCodeDetailDao != null) {
			askCodeDetailDao.save(askCodeDetail);
		}

		askCodeDetail = new AskCodeDetail(timestamp, AskAbstractStaticAttributes.CODE_MEDICINE, timestamp, 3,
				BigDecimal.valueOf(3), null, askCodeHead);
		askCodeHead.addAskCodeDetail(askCodeDetail);
		AskAbstractStaticAttributes.askCodeDetailProductMedicine = askCodeDetail;
		if (askCodeDetailDao != null) {
			askCodeDetailDao.save(askCodeDetail);
		}

		askCodeDetail = new AskCodeDetail(timestamp, "music", timestamp, 4, BigDecimal.valueOf(4), null, askCodeHead);
		askCodeHead.addAskCodeDetail(askCodeDetail);
		AskAbstractStaticAttributes.askCodeDetailProductMusic = askCodeDetail;
		if (askCodeDetailDao != null) {
			askCodeDetailDao.save(askCodeDetail);
		}

		askCodeDetail = new AskCodeDetail(timestamp, "perfume", timestamp, 5, BigDecimal.valueOf(5), null, askCodeHead);
		askCodeHead.addAskCodeDetail(askCodeDetail);
		AskAbstractStaticAttributes.askCodeDetailProductPerfume = askCodeDetail;
		if (askCodeDetailDao != null) {
			askCodeDetailDao.save(askCodeDetail);
		}

		// VERTICAL TABLE "AX"

		askCodeHead = new AskCodeHead(timestamp, "AX", timestamp, (List<AskCodeDetail>) new ArrayList<AskCodeDetail>());
		if (askCodeHeadDao != null) {
			askCodeHeadDao.save(askCodeHead);
		}

		// The details might specialize if it was necessary.
		askCodeDetail = new AskCodeDetail(timestamp, "AX", timestamp, 1, BigDecimal.valueOf(1), null, askCodeHead);
		askCodeHead.addAskCodeDetail(askCodeDetail);
		AskAbstractStaticAttributes.askCodeDetailAx = askCodeDetail;
		if (askCodeDetailDao != null) {
			askCodeDetailDao.save(askCodeDetail);
		}

		// VERTICAL TABLE "ENTITY"

		askCodeHead = new AskCodeHead(timestamp, "ENTITY", timestamp,
				(List<AskCodeDetail>) new ArrayList<AskCodeDetail>());
		if (askCodeHeadDao != null) {
			askCodeHeadDao.save(askCodeHead);
		}

		askCodeDetail = new AskCodeDetail(timestamp, "AskCodeDetailDao", timestamp, 1, BigDecimal.valueOf(1), null,
				askCodeHead);
		askCodeHead.addAskCodeDetail(askCodeDetail);
		if (askCodeDetailDao != null) {
			askCodeDetailDao.save(askCodeDetail);
		}

		askCodeDetail = new AskCodeDetail(timestamp, "AskCodeHeadDao", timestamp, 2, BigDecimal.valueOf(2), null,
				askCodeHead);
		askCodeHead.addAskCodeDetail(askCodeDetail);
		if (askCodeDetailDao != null) {
			askCodeDetailDao.save(askCodeDetail);
		}

		askCodeDetail = new AskCodeDetail(timestamp, "AskInvoiceDetailDao", timestamp, 3, BigDecimal.valueOf(3), null,
				askCodeHead);
		askCodeHead.addAskCodeDetail(askCodeDetail);
		if (askCodeDetailDao != null) {
			askCodeDetailDao.save(askCodeDetail);
		}

		askCodeDetail = new AskCodeDetail(timestamp, "AskInvoiceHeadDao", timestamp, 4, BigDecimal.valueOf(4), null,
				askCodeHead);
		askCodeHead.addAskCodeDetail(askCodeDetail);
		if (askCodeDetailDao != null) {
			askCodeDetailDao.save(askCodeDetail);
		}

		askCodeDetail = new AskCodeDetail(timestamp, "AskProductDao", timestamp, 5, BigDecimal.valueOf(5), null,
				askCodeHead);
		askCodeHead.addAskCodeDetail(askCodeDetail);
		if (askCodeDetailDao != null) {
			askCodeDetailDao.save(askCodeDetail);
		}

		askCodeDetail = new AskCodeDetail(timestamp, "AskTxAxDetailDao", timestamp, 6, BigDecimal.valueOf(6), null,
				askCodeHead);
		askCodeHead.addAskCodeDetail(askCodeDetail);
		if (askCodeDetailDao != null) {
			askCodeDetailDao.save(askCodeDetail);
		}

		askCodeDetail = new AskCodeDetail(timestamp, "AskTxHeadDao", timestamp, 7, BigDecimal.valueOf(7), null,
				askCodeHead);
		askCodeHead.addAskCodeDetail(askCodeDetail);
		if (askCodeDetailDao != null) {
			askCodeDetailDao.save(askCodeDetail);
		}

		askCodeDetail = new AskCodeDetail(timestamp, "AskUserDao", timestamp, 8, BigDecimal.valueOf(8), null,
				askCodeHead);
		askCodeHead.addAskCodeDetail(askCodeDetail);
		if (askCodeDetailDao != null) {
			askCodeDetailDao.save(askCodeDetail);
		}

		askCodeDetail = new AskCodeDetail(timestamp, "AskUserPropDao", timestamp, 9, BigDecimal.valueOf(9), null,
				askCodeHead);
		askCodeHead.addAskCodeDetail(askCodeDetail);
		if (askCodeDetailDao != null) {
			askCodeDetailDao.save(askCodeDetail);
		}

		// VERTICAL TABLE "TX"

		askCodeHead = new AskCodeHead(timestamp, "TX", timestamp, (List<AskCodeDetail>) new ArrayList<AskCodeDetail>());
		if (askCodeHeadDao != null) {
			askCodeHeadDao.save(askCodeHead);
		}

		// The details might specialize if it was necessary.
		askCodeDetail = new AskCodeDetail(timestamp, "TX", timestamp, 1, BigDecimal.valueOf(1), null, askCodeHead);
		askCodeHead.addAskCodeDetail(askCodeDetail);
		AskAbstractStaticAttributes.askCodeDetailTx = askCodeDetail;
		if (askCodeDetailDao != null) {
			askCodeDetailDao.save(askCodeDetail);
		}

		// VERTICAL TABLE "PROP"

		askCodeHead = new AskCodeHead(timestamp, "PROP", timestamp,
				(List<AskCodeDetail>) new ArrayList<AskCodeDetail>());
		if (askCodeHeadDao != null) {
			askCodeHeadDao.save(askCodeHead);
		}

		askCodeDetail = new AskCodeDetail(timestamp, "language", timestamp, 1, BigDecimal.valueOf(1), null, askCodeHead);
		askCodeHead.addAskCodeDetail(askCodeDetail);
		AskAbstractStaticAttributes.askCodeDetailPropLanguage = askCodeDetail;
		if (askCodeDetailDao != null) {
			askCodeDetailDao.save(askCodeDetail);
		}
	}

	private void initFakeUser(Timestamp timestamp) throws AskException {
		// John Doe
		AskUser askUser = new AskUser(timestamp, "a description", "an email", timestamp, "John", "c1", "Doe", "c1",
				AskAbstractStaticAttributes.askCodeDetailRolePublic, (List<AskUserProp>) new ArrayList<AskUserProp>());
		this.askUserDao.save(askUser);

		AskUserProp askUserProp = new AskUserProp(timestamp, "en", timestamp, 1, null, null,
				AskAbstractStaticAttributes.askCodeDetailPropLanguage, askUser);
		askUser.addAskUserProp(askUserProp);
		this.askUserPropDao.save(askUserProp);

		// Mario Rossi
		askUser = new AskUser(timestamp, "a description", "an email", timestamp, "Mario", "c2", "Rossi", "c2",
				AskAbstractStaticAttributes.askCodeDetailRolePublic, (List<AskUserProp>) new ArrayList<AskUserProp>());
		this.askUserDao.save(askUser);

		askUserProp = new AskUserProp(timestamp, "it", timestamp, 1, null, null,
				AskAbstractStaticAttributes.askCodeDetailPropLanguage, askUser);
		askUser.addAskUserProp(askUserProp);
		this.askUserPropDao.save(askUserProp);

		// Juan Perez
		askUser = new AskUser(timestamp, "a description", "an email", timestamp, "Juan", "c3", "Perez", "c3",
				AskAbstractStaticAttributes.askCodeDetailRolePublic, (List<AskUserProp>) new ArrayList<AskUserProp>());
		this.askUserDao.save(askUser);

		askUserProp = new AskUserProp(timestamp, "es", timestamp, 1, null, null,
				AskAbstractStaticAttributes.askCodeDetailPropLanguage, askUser);
		askUser.addAskUserProp(askUserProp);
		this.askUserPropDao.save(askUserProp);
	}

	public static void initFakeProd(Timestamp timestamp, AskProductDao askProductDao) throws AskException {
		AskProduct askProduct;

		askProduct = new AskProduct(timestamp, "Umberto Eco: \"Il Nome Della Rosa\"", "BON-549315879", false,
				timestamp, BigDecimal.valueOf(12.49), AskAbstractStaticAttributes.askCodeDetailProductBook);
		AskAbstractStaticAttributes.askProductBookEco = askProduct;
		if (askProductDao != null) {
			askProductDao.save(askProduct);
		}

		askProduct = new AskProduct(timestamp, "Ennio Morricone: \"The Mission\"", "MUN-386817367", false, timestamp,
				BigDecimal.valueOf(14.99), AskAbstractStaticAttributes.askCodeDetailProductMusic);
		AskAbstractStaticAttributes.askProductMusicMorricone = askProduct;
		if (askProductDao != null) {
			askProductDao.save(askProduct);
		}

		askProduct = new AskProduct(timestamp, "Novi fondente da 100g", "FON-038403925", false, timestamp,
				BigDecimal.valueOf(0.85), AskAbstractStaticAttributes.askCodeDetailProductFood);
		AskAbstractStaticAttributes.askProductFoodNovi = askProduct;
		if (askProductDao != null) {
			askProductDao.save(askProduct);
		}

		askProduct = new AskProduct(timestamp, "Lindt noir puissant 100g", "FOI-820592856", true, timestamp,
				BigDecimal.valueOf(10.00), AskAbstractStaticAttributes.askCodeDetailProductFood);
		AskAbstractStaticAttributes.askProductFoodLindt = askProduct;
		if (askProductDao != null) {
			askProductDao.save(askProduct);
		}

		askProduct = new AskProduct(timestamp, "YvesSaintLaurent Opium Eau de Toilette 20ml", "PEI-302504867", true,
				timestamp, BigDecimal.valueOf(47.50), AskAbstractStaticAttributes.askCodeDetailProductPerfume);
		AskAbstractStaticAttributes.askProductPerfumeYSL = askProduct;
		if (askProductDao != null) {
			askProductDao.save(askProduct);
		}

		askProduct = new AskProduct(timestamp, "Hugo Boss Eau de Toilette 10ml", "PEI-840297541", true, timestamp,
				BigDecimal.valueOf(27.99), AskAbstractStaticAttributes.askCodeDetailProductPerfume);
		AskAbstractStaticAttributes.askProductPerfumeBoss = askProduct;
		if (askProductDao != null) {
			askProductDao.save(askProduct);
		}

		askProduct = new AskProduct(timestamp, "Giorgio Armani Acqua di Gio 20ml", "PEN-333568964", false, timestamp,
				BigDecimal.valueOf(18.99), AskAbstractStaticAttributes.askCodeDetailProductPerfume);
		AskAbstractStaticAttributes.askProductPerfumeArmani = askProduct;
		if (askProductDao != null) {
			askProductDao.save(askProduct);
		}

		askProduct = new AskProduct(timestamp, "Moment headache pills 400mg", "MEN-897313161", false, timestamp,
				BigDecimal.valueOf(9.75), AskAbstractStaticAttributes.askCodeDetailProductMedicine);
		AskAbstractStaticAttributes.askProductMedicineMoment = askProduct;
		if (askProductDao != null) {
			askProductDao.save(askProduct);
		}

		askProduct = new AskProduct(timestamp, "Kinder 240g", "FOI-975166836", true, timestamp,
				BigDecimal.valueOf(11.25), AskAbstractStaticAttributes.askCodeDetailProductFood);
		AskAbstractStaticAttributes.askProductFoodKinder = askProduct;
		if (askProductDao != null) {
			askProductDao.save(askProduct);
		}
	}

	public AskUser getAskUser(String username, String password) throws AskException {
		AskAbstractStaticMethods.validateNotNull(this.askLogic, username, this.askSessionManager, "askSessionManager");
		AskUser askUser = this.askUserDao.findUser(username, password);

		if (askUser == null) {
			return null;
		}

		this.askSessionManager.putInSession(askUser);
		this.newCurrentInvoice(askUser);
		return askUser;
	}

	public void newCurrentInvoice() throws AskException {
		AskUser askUser = this.getAskUserFromSession();

		if (askUser == null) {
			return;
		}

		this.newCurrentInvoice(askUser);
	}

	public void newCurrentInvoice(AskUser askUser) throws AskException {
		String username = askUser == null ? AskAbstractStaticAttributes.NO_USER : askUser.getUsername();
		AskAbstractStaticMethods.validateNotNull(this.askLogic, username, askUser, "askUser");

		/*
		 * The following control of askUser == null is a bug in the find bugs
		 * eclipse plug-in. At this point askUser cannot be null because of
		 * validateNotNull call.
		 */
		AskInvoiceHead askInvoiceHead = this.askInvoiceHeadDao
				.findActiveInvoice(askUser == null ? -1 : askUser.getId());

		if (askInvoiceHead == null) {
			Timestamp timestamp = AskAbstractStaticMethods.getCurrentTimestamp();
			List<AskInvoiceDetail> askInvoiceDetails = new ArrayList<AskInvoiceDetail>();
			askInvoiceHead = new AskInvoiceHead(true, timestamp, "Current invoice", timestamp, BigDecimal.ZERO, null,
					BigDecimal.ZERO, askInvoiceDetails, askUser);
			this.askInvoiceHeadDao.save(askInvoiceHead);
		}

		this.askSessionManager.putInSession(askUser, askInvoiceHead);
	}

	public List<AskInvoiceHead> findAllNonActiveInvoices() {
		AskUser askUser = this.askSessionManager.getAskUserFromSession();

		if (askUser == null) {
			return new ArrayList<AskInvoiceHead>();
		}

		return this.askInvoiceHeadDao.findAllNonActiveInvoices(askUser.getId());
	}

	public void doLogout() throws AskException {
		this.askSessionManager.removeAskUserFromSession();
	}

	public void setAskCodeDetailDao(AskCodeDetailDao askCodeDetailDao) {
		this.askCodeDetailDao = askCodeDetailDao;
	}

	public void setAskCodeHeadDao(AskCodeHeadDao askCodeHeadDao) {
		this.askCodeHeadDao = askCodeHeadDao;
	}

	public void setAskInvoiceDetailDao(AskInvoiceDetailDao askInvoiceDetailDao) {
		this.askInvoiceDetailDao = askInvoiceDetailDao;
	}

	public void setAskInvoiceHeadDao(AskInvoiceHeadDao askInvoiceHeadDao) {
		this.askInvoiceHeadDao = askInvoiceHeadDao;
	}

	public void setAskProductDao(AskProductDao askProductDao) {
		this.askProductDao = askProductDao;
	}

	public void setAskTxAxDetailDao(AskTxAxDetailDao askTxAxDetailDao) {
		this.askTxAxDetailDao = askTxAxDetailDao;
	}

	public void setAskTxHeadDao(AskTxHeadDao askTxHeadDao) {
		this.askTxHeadDao = askTxHeadDao;
	}

	public void setAskUserDao(AskUserDao askUserDao) {
		this.askUserDao = askUserDao;
	}

	public void setAskUserPropDao(AskUserPropDao askUserPropDao) {
		this.askUserPropDao = askUserPropDao;
	}

	public List<AskProduct> findAllProducts() {
		return this.askProductDao.findAll();
	}

	public void saveAllAskInvoiceDetail(List<AskInvoiceDetail> listAskInvoiceDetail) {
		this.askInvoiceDetailDao.save(listAskInvoiceDetail);
	}

	public void saveAskInvoiceDetail(AskInvoiceDetail askInvoiceDetail) {
		this.askInvoiceDetailDao.save(askInvoiceDetail);
	}

	public void saveAskTxAxDetail(AskUser askUser, AskTxAxDetail askTxAxDetail) {
		if (askUser == null || askTxAxDetail == null) {
			return;
		}

		try {
			AskTxHead askTxHead = this.getAskTxHead(askUser);

			if (askTxHead == null) {
				return;
			}

			this.setAskCodeDetailAx();
			askTxAxDetail.setAskCodeDetail1(AskAbstractStaticAttributes.askCodeDetailAx);
			askTxHead.addAskTxAxDetail(askTxAxDetail);
			askTxHead.setModificationTime(AskAbstractStaticMethods.getCurrentTimestamp());
			this.askTxAxDetailDao.save(askTxAxDetail);
			this.askTxHeadDao.save(askTxHead);
		} catch (AskException e) {
		}
	}

	private void setAskCodeDetailAx() {
		if (AskAbstractStaticAttributes.askCodeDetailAx != null) {
			return;
		}

		List<AskCodeDetail> listAskCodeDetail = this.askCodeDetailDao.findByDescription("AX");
		AskAbstractStaticAttributes.askCodeDetailAx = listAskCodeDetail.get(0);
	}

	private void setAskCodeDetailTx() {
		if (AskAbstractStaticAttributes.askCodeDetailTx != null) {
			return;
		}

		List<AskCodeDetail> listAskCodeDetail = this.askCodeDetailDao.findByDescription("TX");
		AskAbstractStaticAttributes.askCodeDetailTx = listAskCodeDetail.get(0);
	}

	private AskTxHead getAskTxHead(AskUser askUser) throws AskException {
		if (askUser == null) {
			return null;
		}

		AskTxHead askTxHead = this.askSessionManager.getAskTxHeadFromSession();

		if (askTxHead != null) {
			return askTxHead;
		}

		this.setAskCodeDetailTx();
		Timestamp timestamp = AskAbstractStaticMethods.getCurrentTimestamp();
		String denomination = AskAbstractStaticMethods.getAskUserDenomination(askUser);
		askTxHead = new AskTxHead(timestamp, denomination, timestamp, new ArrayList<AskTxAxDetail>(),
				AskAbstractStaticAttributes.askCodeDetailTx);
		this.askTxHeadDao.save(askTxHead);

		if (askUser != null) {
			this.askSessionManager.putInSession(askUser, askTxHead);
		}

		return askTxHead;
	}

	public void deleteAskInvoiceDetail(AskInvoiceDetail askInvoiceDetail) {
		this.askInvoiceDetailDao.delete(askInvoiceDetail);
	}

	public void saveAskInvoiceHead(AskInvoiceHead askInvoiceHead) {
		this.askInvoiceHeadDao.save(askInvoiceHead);
	}

	public void saveAskUserProp(AskUserProp askUserProp) {
		this.askUserPropDao.save(askUserProp);
	}

	public AskUser getAskUserFromSession() {
		return this.askSessionManager.getAskUserFromSession();
	}

	public AskInvoiceHead getAskInvoiceHeadFromSession() {
		return this.askSessionManager.getAskInvoiceHeadFromSession();
	}

	public List<AskTxHead> findAllAskTxHead(String username) {
		return this.askTxHeadDao.findAllTransactions(username);
	}

	public void refresh(AskTxHead askTxHead) {
		AskTxHead askTxHeadSess = this.askSessionManager.getAskTxHeadFromSession();

		if (askTxHead.equals(askTxHeadSess)) {
			askTxHead.setAskTxAxDetails(askTxHeadSess.getAskTxAxDetails());
		}
	}

}
