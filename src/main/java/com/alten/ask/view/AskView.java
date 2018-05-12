package com.alten.ask.view;

import java.util.List;
import java.util.logging.Level;

import com.alten.ask.AskAbstractStaticMethods;
import com.alten.ask.AskException;
import com.alten.ask.AskLogger;
import com.alten.ask.AskLoggerManager;
import com.alten.ask.logic.AskActionInterface;
import com.alten.ask.logic.AskLogic;
import com.alten.ask.view.comp.AskCompMain;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractOrderedLayout;

public class AskView implements AskActionInterface {

	private static final long serialVersionUID = 1L;
	private final AskLogger logger;

	private final AskLogic askLogic;
	private final AskCompMain askCompMain;
	private ShortcutListener shortcutListenerEnter;
	private boolean shortcutListenerEnterAdded;

	public AskView(AskLogic askLogic) throws AskException {
		this.askLogic = askLogic;
		this.askCompMain = new AskCompMain(this.askLogic);
		this.logger = AskLoggerManager.getLogger(this.askLogic, AskView.class.getName());
	}

	public void init() throws AskException {
		this.askCompMain.init();
		this.createShortcutListener();
		this.addShortcutListener();
	}

	public void setVisibility() throws AskException {
		this.askCompMain.setVisibility();
	}

	public void addMainComponent(String username, AbstractOrderedLayout orderedLayout) throws AskException {
		AskAbstractStaticMethods.validateNotNull(this.askLogic, username, this.askCompMain, "askCompMain");
		orderedLayout.addComponent(this.askCompMain);
	}

	public void addShortcutListener() throws AskException {
		if (this.shortcutListenerEnterAdded) {
			return;
		}

		logger.log(Level.CONFIG, "Doing addShortcutListener.");
		this.askCompMain.addShortcutListener(this.shortcutListenerEnter);
		this.shortcutListenerEnterAdded = true;
	}

	public void removeShortcutListener() throws AskException {
		if (!this.shortcutListenerEnterAdded) {
			return;
		}

		logger.log(Level.CONFIG, "Doing removeShortcutListener.");
		this.askCompMain.removeShortcutListener(this.shortcutListenerEnter);
		this.shortcutListenerEnterAdded = false;
	}

	private void createShortcutListener() {
		this.shortcutListenerEnter = new ShortcutListener("EnterOnTextAreaShorcut", ShortcutAction.KeyCode.ENTER, null) {

			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				try {
					askLogic.doEnter();
				} catch (AskException e) {
				}
			}
		};
	}

	public void postLogin() throws AskException {
		this.askCompMain.postLogin();
	}

	public void postLogout() throws AskException {
		this.askCompMain.postLogout();
	}

	public void addCompsForLoggedUser(List<AbstractComponent> listComponentForLoggedUser) throws AskException {
		this.askCompMain.addCompsForLoggedUser(listComponentForLoggedUser);
	}

	public void addCompsForNoUser(List<AbstractComponent> listComponentForNoUser) throws AskException {
		this.askCompMain.addCompsForNoUser(listComponentForNoUser);
	}

	@Override
	public void doAction(AskActionType askActionType) throws AskException {
		this.askCompMain.doAction(askActionType);
	}

}
