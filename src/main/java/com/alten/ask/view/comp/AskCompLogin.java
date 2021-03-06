package com.alten.ask.view.comp;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.alten.ask.AskAbstractStaticAttributes;
import com.alten.ask.AskAbstractStaticMethods;
import com.alten.ask.AskException;
import com.alten.ask.AskLogger;
import com.alten.ask.AskLoggerManager;
import com.alten.ask.logic.AskLogic;
import com.alten.ask.model.orm.AskUser;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;

public class AskCompLogin extends CustomComponent implements AskCompInterface {

	private static final long serialVersionUID = 1L;

	private final AskLogger logger;
	private final AskLogic askLogic;

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	private HorizontalLayout mainLayout;
	private PasswordField passwordFieldPassword;
	private TextField textFieldUsername;
	private Button buttonLogout;
	private Label labelUser;

	private List<AbstractComponent> listComponentForLoggedUser;
	private List<AbstractComponent> listComponentForNoUser;

	/**
	 * The constructor should first build the main layout, set the composition
	 * root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the visual
	 * editor.
	 */
	public AskCompLogin(AskLogic askLogic) {
		this.askLogic = askLogic;
		this.logger = AskLoggerManager.getLogger(this.askLogic, AskCompLogin.class.getName());
		buildMainLayout();
		setCompositionRoot(mainLayout);
		this.labelUser.setContentMode(ContentMode.HTML);
	}

	@Override
	public void doEnter() throws AskException {
		this.doLogin();
	}

	private void doLogin() throws AskException {
		String username = this.textFieldUsername.getValue();
		this.textFieldUsername.setValue("");
		String password = this.passwordFieldPassword.getValue();
		this.passwordFieldPassword.setValue("");
		AskUser askUser = this.askLogic.getAskUser(username, password);

		if (askUser == null) {
			return;
		}

		this.setVisibility();
		this.askLogic.doAction(this.askLogic.getAskActionTypeLastNavigate());
	}

	public void doLogout() throws AskException {
		AskUser askUser = this.askLogic.getAskUserFromSession();

		if (askUser == null) {
			logger.log(Level.INFO, "Logout refused, no user in session.");
			return;
		}

		logger.log(Level.INFO, "Logout done for username \"" + askUser.getUsername() + "\".");
		this.askLogic.doLogout();
		this.labelUser.setValue("");
		this.setVisibility();
		this.askLogic.doAction(this.askLogic.getAskActionTypeLastNavigate());
	}

	private void initListCompForVisibility() throws AskException {
		if (this.listComponentForLoggedUser != null) {
			return;
		}

		this.listComponentForLoggedUser = new ArrayList<>();
		this.askLogic.addCompsForLoggedUser(this.listComponentForLoggedUser);

		this.listComponentForNoUser = new ArrayList<>();
		this.askLogic.addCompsForNoUser(this.listComponentForNoUser);
	}

	public void setVisibility() throws AskException {
		this.initListCompForVisibility();
		AskUser askUser = this.askLogic.getAskUserFromSession();
		boolean logged = askUser != null;

		for (AbstractComponent component : listComponentForLoggedUser) {
			component.setVisible(logged);
		}

		for (AbstractComponent component : listComponentForNoUser) {
			component.setVisible(!logged);
		}

		if (logged) {
			this.labelUser.setValue(askUser.getName() + "&nbsp;" + askUser.getSurname() + "&nbsp;("
					+ askUser.getUsername() + ")&nbsp;");
			this.buttonLogout.focus();
		} else {
			this.textFieldUsername.focus();
		}

		// this.askLogic.doAction(AskActionType.navigateToHome);
	}

	@Override
	public void onFocus(Object objectForOnFocus) {
	}

	@Override
	public void init() throws AskException {
		AskAbstractStaticMethods.addFocus(AskAbstractStaticAttributes.NO_USER, this.textFieldUsername, this.askLogic,
				this, null, this.logger);
		AskAbstractStaticMethods.addFocus(AskAbstractStaticAttributes.NO_USER, this.passwordFieldPassword,
				this.askLogic, this, null, this.logger);
		AskAbstractStaticMethods.addClick(this.askLogic, AskAbstractStaticAttributes.NO_USER, this.buttonLogout, this,
				null);
	}

	@Override
	public void doClick(AskActionType jugActionType) throws AskException {
		this.doLogout();
	}

	private HorizontalLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new HorizontalLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("-1px");
		mainLayout.setHeight("-1px");
		mainLayout.setMargin(false);

		// top-level component properties
		setWidth("-1px");
		setHeight("-1px");

		// labelUser
		labelUser = new Label();
		labelUser.setImmediate(false);
		labelUser.setWidth("-1px");
		labelUser.setHeight("-1px");
		labelUser.setValue("user");
		mainLayout.addComponent(labelUser);
		mainLayout.setComponentAlignment(labelUser, new Alignment(33));

		// buttonLogout
		buttonLogout = new Button();
		buttonLogout.setCaption("Logout");
		buttonLogout.setImmediate(true);
		buttonLogout.setWidth("-1px");
		buttonLogout.setHeight("-1px");
		mainLayout.addComponent(buttonLogout);

		// textFieldUsername
		textFieldUsername = new TextField();
		textFieldUsername.setImmediate(false);
		textFieldUsername.setWidth("-1px");
		textFieldUsername.setHeight("-1px");
		textFieldUsername.setInputPrompt("username");
		mainLayout.addComponent(textFieldUsername);

		// passwordFieldPassword
		passwordFieldPassword = new PasswordField();
		passwordFieldPassword.setImmediate(false);
		passwordFieldPassword.setWidth("-1px");
		passwordFieldPassword.setHeight("-1px");
		passwordFieldPassword.setInputPrompt("password");
		mainLayout.addComponent(passwordFieldPassword);

		return mainLayout;
	}

	@Override
	public void addCompsForLoggedUser(List<AbstractComponent> listComponentForLoggedUser) throws AskException {
		AskUser askUser = this.askLogic.getAskUserFromSession();
		String username = askUser == null ? AskAbstractStaticAttributes.NO_USER : askUser.getUsername();
		AskAbstractStaticMethods.validateNotNull(this.askLogic, username, listComponentForLoggedUser,
				"listComponentForLoggedUser");
		listComponentForLoggedUser.add(this.labelUser);
		listComponentForLoggedUser.add(this.buttonLogout);
	}

	@Override
	public void addCompsForNoUser(List<AbstractComponent> listComponentForNoUser) throws AskException {
		AskUser askUser = this.askLogic.getAskUserFromSession();
		String username = askUser == null ? AskAbstractStaticAttributes.NO_USER : askUser.getUsername();
		AskAbstractStaticMethods.validateNotNull(this.askLogic, username, listComponentForNoUser,
				"listComponentForNoUser");
		listComponentForNoUser.add(this.textFieldUsername);
		listComponentForNoUser.add(this.passwordFieldPassword);
	}

	public void postLogin() throws AskException {
	}

	public void postLogout() throws AskException {
	}

	@Override
	public void doAction(AskActionType askActionType) throws AskException {
	}

}
