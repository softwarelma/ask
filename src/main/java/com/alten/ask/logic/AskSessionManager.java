package com.alten.ask.logic;

import java.io.Serializable;

import com.alten.ask.AskAbstractStaticAttributes;
import com.alten.ask.AskAbstractStaticMethods;
import com.alten.ask.AskException;
import com.alten.ask.model.orm.AskInvoiceHead;
import com.alten.ask.model.orm.AskTxHead;
import com.alten.ask.model.orm.AskUser;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;

public class AskSessionManager implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String ASK_LOGGED_USER = "askLoggedUser";
	private static final String ASK_ACTIVE_INVOICE = "askActiveInvoice";
	private static final String ASK_TX_HEAD = "askTxHead";
	private final AskLogic askLogic;

	public AskSessionManager(AskLogic askLogic) {
		this.askLogic = askLogic;
	}

	public void putInSession(AskUser askUser) throws AskException {
		String username = askUser == null ? AskAbstractStaticAttributes.NO_USER : askUser.getUsername();
		AskAbstractStaticMethods.validateNotNull(this.askLogic, username, askUser, ASK_LOGGED_USER);
		VaadinSession vaadinSession = UI.getCurrent().getSession();
		vaadinSession.setAttribute(ASK_LOGGED_USER, askUser);
	}

	public void putInSession(AskUser askUser, AskInvoiceHead askInvoiceHead) throws AskException {
		String username = askUser == null ? AskAbstractStaticAttributes.NO_USER : askUser.getUsername();
		AskAbstractStaticMethods.validateNotNull(this.askLogic, username, askInvoiceHead, ASK_ACTIVE_INVOICE);
		VaadinSession vaadinSession = UI.getCurrent().getSession();
		vaadinSession.setAttribute(ASK_ACTIVE_INVOICE, askInvoiceHead);
	}

	public void putInSession(AskUser askUser, AskTxHead askTxHead) throws AskException {
		String username = askUser == null ? AskAbstractStaticAttributes.NO_USER : askUser.getUsername();
		AskAbstractStaticMethods.validateNotNull(this.askLogic, username, askTxHead, ASK_TX_HEAD);
		VaadinSession vaadinSession = UI.getCurrent().getSession();
		vaadinSession.setAttribute(ASK_TX_HEAD, askTxHead);
	}

	public void removeAskUserFromSession() {
		VaadinSession vaadinSession = UI.getCurrent().getSession();
		vaadinSession.setAttribute(ASK_LOGGED_USER, null);
		vaadinSession.setAttribute(ASK_ACTIVE_INVOICE, null);
		vaadinSession.setAttribute(ASK_TX_HEAD, null);
	}

	public AskUser getAskUserFromSession() {
		VaadinSession vaadinSession = UI.getCurrent().getSession();
		Object object = vaadinSession.getAttribute(ASK_LOGGED_USER);

		if (object == null) {
			return null;
		}

		if (!(object instanceof AskUser)) {
			return null;
		}

		return (AskUser) object;
	}

	public AskInvoiceHead getAskInvoiceHeadFromSession() {
		VaadinSession vaadinSession = UI.getCurrent().getSession();
		Object object = vaadinSession.getAttribute(ASK_ACTIVE_INVOICE);

		if (object == null) {
			return null;
		}

		if (!(object instanceof AskInvoiceHead)) {
			return null;
		}

		return (AskInvoiceHead) object;
	}

	public AskTxHead getAskTxHeadFromSession() {
		VaadinSession vaadinSession = UI.getCurrent().getSession();
		Object object = vaadinSession.getAttribute(ASK_TX_HEAD);

		if (object == null) {
			return null;
		}

		if (!(object instanceof AskTxHead)) {
			return null;
		}

		return (AskTxHead) object;
	}

}
