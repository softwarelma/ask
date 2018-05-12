package com.alten.ask.logic;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;

import com.alten.ask.AskAbstractStaticAttributes;
import com.alten.ask.AskAbstractStaticMethods;
import com.alten.ask.AskException;
import com.alten.ask.AskLogger;
import com.alten.ask.AskLoggerManager;
import com.alten.ask.model.AskModel;
import com.alten.ask.model.orm.AskCodeDetailDao;
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
import com.alten.ask.view.AskView;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractOrderedLayout;

public class AskLogic implements AskActionInterface, AskEnterInterface, AskClickInterface {

	private static final long serialVersionUID = 1L;
	private final AskLogger logger = AskLoggerManager.getLogger(this, AskLogic.class.getName());

	private final AskModel askModel;
	private final AskView askView;
	private AskEnterInterface askEnter;
	private boolean globallyInitialized = false;
	private Locale locale;
	private ResourceBundle resourceBundleStrings;
	private AskActionType askActionTypeLastNavigate;

	public AskLogic() throws AskException {
		// the following order cannot be altered
		this.askModel = new AskModel(this);// 1
		this.initResourceBundle(AskAbstractStaticAttributes.NO_USER);// 2
		this.askView = new AskView(this);// 3
	}

	private void initResourceBundle(String username) throws AskException {
		// use 2-letters language code as the id and no other field
		this.locale = Locale.getDefault();
		this.resourceBundleStrings = ResourceBundle.getBundle("strings", this.locale);
		logger.log(Level.INFO, "Username \"" + username + "\", setting language as \"" + this.locale.getLanguage()
				+ "\".");
	}

	public void setDefaultLocale(String language) throws AskException {
		AskUser askUser = this.askModel.getAskUserFromSession();

		if (askUser == null) {
			AskAbstractStaticMethods.validateInclusion(this, AskAbstractStaticAttributes.NO_USER, language, "language",
					new String[] { "en", "es", "it" });
			Locale.setDefault(new Locale(language));
			this.initResourceBundle(AskAbstractStaticAttributes.NO_USER);
		} else {
			AskAbstractStaticMethods.validateInclusion(this, askUser.getUsername(), language, "language", new String[] {
					"en", "es", "it" });
			Locale.setDefault(new Locale(language));
			AskUserProp askUserPropLanguage = this.getPropLanguage(askUser, askUser.getAskUserProps());
			askUserPropLanguage.setDescription(language);
			this.askModel.saveAskUserProp(askUserPropLanguage);
			this.initResourceBundle(askUser.getUsername());
		}
	}

	public String getResourceBundleString(String key) throws AskException {
		AskAbstractStaticMethods.validateNotNull(this, key, "key");
		String value = this.resourceBundleStrings.getString(key);
		AskAbstractStaticMethods.validateNotNull(this, value, "value");
		return value;
	}

	public void globalInit() throws AskException {
		if (this.globallyInitialized) {
			throw new AskException(this, "AskLogic already initialized.", AskAbstractStaticAttributes.NO_USER);
		}

		this.askModel.init();
		this.askView.init();
		// this.askView.setVisibility();
		this.doAction(AskActionType.navigateToHome);
		this.globallyInitialized = true;
	}

	public void addMainComponent(AbstractOrderedLayout orderedLayout) throws AskException {
		this.askView.addMainComponent(AskAbstractStaticAttributes.NO_USER, orderedLayout);
	}

	public void setEnter(AskEnterInterface askEnter) throws AskException {
		this.askEnter = askEnter;

		if (this.askEnter == null) {
			this.askView.removeShortcutListener();
		} else {
			logger.log(Level.CONFIG, "Doing setEnter for " + askEnter.getClass().getName() + ".");
			this.askView.addShortcutListener();
		}
	}

	@Override
	public void doAction(AskActionType askActionType) throws AskException {
		AskUser askUser = this.askModel.getAskUserFromSession();
		String username = askUser == null ? AskAbstractStaticAttributes.NO_USER : askUser.getUsername();
		AskAbstractStaticMethods.validateNotNull(this, username, askActionType, "askActionType");
		String user = AskAbstractStaticAttributes.NO_USER;

		if (askUser != null) {
			user = AskAbstractStaticMethods.getAskUserDenomination(askUser);
		}

		if (askUser == null) {
			if (askActionType.equals(AskActionType.navigateToInvoices)
					|| askActionType.equals(AskActionType.navigateToCart)
					|| askActionType.equals(AskActionType.navigateToTx)) {
				askActionType = AskActionType.navigateToHome;
			}
		}

		switch (askActionType) {
		case navigateToHome:
		case navigateToInvoices:
		case navigateToCart:
		case navigateToTx:
			logger.log(Level.INFO, "User \"" + user + "\", doing action \"" + askActionType.toString() + "\"");
			this.askActionTypeLastNavigate = askActionType;
			break;
		case doAddProduct:
		case doRemoveProduct:
		case doPurchase:
			// detailed log needed
			break;
		default:
			logger.log(Level.INFO, "User \"" + user + "\", doing action \"" + askActionType.toString() + "\"");
			break;
		}

		this.askView.setVisibility();
		this.askView.doAction(askActionType);
	}

	public AskActionType getAskActionTypeLastNavigate() {
		return this.askActionTypeLastNavigate;
	}

	@Override
	public void doEnter() throws AskException {
		if (this.askEnter == null) {
			return;
		}

		this.askEnter.doEnter();
	}

	@Override
	public void onFocus(Object objectForOnFocus) throws AskException {
		logger.log(Level.INFO, "onFocus");
	}

	@Override
	public void doClick(AskActionType askActionType) throws AskException {
		this.doAction(askActionType);
	}

	public AskUser getAskUser(String username, String password) throws AskException {
		AskUser askUser = this.askModel.getAskUser(username, password);

		if (askUser == null) {
			logger.log(Level.INFO, "Login refused for username \"" + username + "\".");
		} else {
			logger.log(Level.INFO, "Login done for username \"" + username + "\".");
			this.askView.postLogin();
			AskUserProp propLanguage = this.getPropLanguage(askUser, askUser.getAskUserProps());
			this.setDefaultLocale(propLanguage.getDescription());
		}

		return askUser;
	}

	private AskUserProp getPropLanguage(AskUser askUser, List<AskUserProp> listAskUserProp) throws AskException {
		for (AskUserProp askUserProp : listAskUserProp) {
			if (askUserProp.getAskCodeDetail().getDescription().equals("language")) {
				return askUserProp;
			}
		}

		throw new AskException(this, "Language property not found.", askUser.getUsername());
	}

	public void doLogout() throws AskException {
		this.askModel.doLogout();
		this.askView.postLogout();
	}

	public void addCompsForLoggedUser(List<AbstractComponent> listComponentForLoggedUser) throws AskException {
		this.askView.addCompsForLoggedUser(listComponentForLoggedUser);
	}

	public void addCompsForNoUser(List<AbstractComponent> listComponentForNoUser) throws AskException {
		this.askView.addCompsForNoUser(listComponentForNoUser);
	}

	public AskUser getAskUserFromSession() {
		return this.askModel == null ? null : this.askModel.getAskUserFromSession();
	}

	public AskInvoiceHead getAskInvoiceHeadFromSession() {
		return this.askModel.getAskInvoiceHeadFromSession();
	}

	public void setAskCodeDetailDao(AskCodeDetailDao askCodeDetailDao) {
		this.askModel.setAskCodeDetailDao(askCodeDetailDao);
	}

	public void setAskCodeHeadDao(AskCodeHeadDao askCodeHeadDao) {
		this.askModel.setAskCodeHeadDao(askCodeHeadDao);
	}

	public void setAskInvoiceDetailDao(AskInvoiceDetailDao askInvoiceDetailDao) {
		this.askModel.setAskInvoiceDetailDao(askInvoiceDetailDao);
	}

	public void setAskInvoiceHeadDao(AskInvoiceHeadDao askInvoiceHeadDao) {
		this.askModel.setAskInvoiceHeadDao(askInvoiceHeadDao);
	}

	public void setAskProductDao(AskProductDao askProductDao) {
		this.askModel.setAskProductDao(askProductDao);
	}

	public void setAskTxAxDetailDao(AskTxAxDetailDao askTxAxDetailDao) {
		this.askModel.setAskTxAxDetailDao(askTxAxDetailDao);
	}

	public void setAskTxHeadDao(AskTxHeadDao askTxHeadDao) {
		this.askModel.setAskTxHeadDao(askTxHeadDao);
	}

	public void setAskUserDao(AskUserDao askUserDao) {
		this.askModel.setAskUserDao(askUserDao);
	}

	public void setAskUserPropDao(AskUserPropDao askUserPropDao) {
		this.askModel.setAskUserPropDao(askUserPropDao);
	}

	public List<AskProduct> findAllProducts() {
		return this.askModel.findAllProducts();
	}

	public void saveAllAskInvoiceDetail(List<AskInvoiceDetail> listAskInvoiceDetail) {
		this.askModel.saveAllAskInvoiceDetail(listAskInvoiceDetail);
	}

	public void saveAskInvoiceDetail(AskInvoiceDetail askInvoiceDetail) {
		this.askModel.saveAskInvoiceDetail(askInvoiceDetail);
	}

	public void deleteAskInvoiceDetail(AskInvoiceDetail askInvoiceDetail) {
		this.askModel.deleteAskInvoiceDetail(askInvoiceDetail);
	}

	public void saveAskInvoiceHead(AskInvoiceHead askInvoiceHead) {
		this.askModel.saveAskInvoiceHead(askInvoiceHead);
	}

	public void newCurrentInvoice() throws AskException {
		this.askModel.newCurrentInvoice();
	}

	public List<AskInvoiceHead> findAllNonActiveInvoices() {
		return this.askModel.findAllNonActiveInvoices();
	}

	public void saveAskTxAxDetail(AskUser askUser, AskTxAxDetail askTxAxDetail) {
		this.askModel.saveAskTxAxDetail(askUser, askTxAxDetail);
	}

	public List<AskTxHead> findAllAskTxHead(String username) {
		return this.askModel.findAllAskTxHead(username);
	}

	public void refresh(AskTxHead askTxHead) {
		this.askModel.refresh(askTxHead);
	}

}
