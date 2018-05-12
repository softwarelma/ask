package com.alten.ask.logic;

import java.io.Serializable;

import com.alten.ask.AskException;

public interface AskActionInterface extends Serializable {

	public enum AskActionType {
		navigateToHome, navigateToInvoices, navigateToCart, navigateToTx,

		doAddProduct, doRemoveProduct, doPurchase,

		doLangEn, doLangEs, doLangIt
	}

	public void doAction(AskActionType askActionType) throws AskException;

}
