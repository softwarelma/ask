package com.alten.ask.view.comp;

import java.util.List;

import com.alten.ask.AskException;
import com.alten.ask.logic.AskLogic;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

public class AskCompMain extends CustomComponent implements AskCompInterface {

	private static final long serialVersionUID = 1L;

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	private VerticalLayout mainLayout;
	private AskCompFooter askCompFooter;
	private HorizontalLayout horizontalLayout_1;
	private AskCompCenter askCompCenter;
	private AskCompSections askCompSections;
	private AskCompHeader askCompHeader;
	private final AskLogic askLogic;

	/**
	 * The constructor should first build the main layout, set the composition
	 * root and then do any custom initialization.
	 * 
	 * The constructor will not be automatically regenerated by the visual
	 * editor.
	 */
	public AskCompMain(AskLogic askLogic) throws AskException {
		this.askLogic = askLogic;
		buildMainLayout();
		setCompositionRoot(mainLayout);
	}

	@Override
	public void init() throws AskException {
		this.askCompHeader.init();
		this.askCompSections.init();
		this.askCompCenter.init();
		this.askCompFooter.init();
		// setAskButtonSectionSelectedWrapper
	}

	private VerticalLayout buildMainLayout() throws AskException {
		// common part: create layout
		mainLayout = new VerticalLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		mainLayout.setMargin(false);

		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");

		// askCompHeader
		askCompHeader = new AskCompHeader(this.askLogic);
		askCompHeader.setImmediate(false);
		askCompHeader.setWidth("100.0%");
		askCompHeader.setHeight("-1px");
		mainLayout.addComponent(askCompHeader);

		// horizontalLayout_1
		horizontalLayout_1 = buildHorizontalLayout_1();
		mainLayout.addComponent(horizontalLayout_1);
		mainLayout.setExpandRatio(horizontalLayout_1, 1.0f);

		// askCompFooter
		askCompFooter = new AskCompFooter();
		askCompFooter.setImmediate(false);
		askCompFooter.setWidth("100.0%");
		askCompFooter.setHeight("-1px");
		mainLayout.addComponent(askCompFooter);

		return mainLayout;
	}

	private HorizontalLayout buildHorizontalLayout_1() throws AskException {
		// common part: create layout
		horizontalLayout_1 = new HorizontalLayout();
		horizontalLayout_1.setImmediate(false);
		horizontalLayout_1.setWidth("100.0%");
		horizontalLayout_1.setHeight("100.0%");
		horizontalLayout_1.setMargin(false);

		// askCompSections
		askCompSections = new AskCompSections(this.askLogic);
		askCompSections.setImmediate(false);
		askCompSections.setWidth("200px");
		askCompSections.setHeight("100.0%");
		horizontalLayout_1.addComponent(askCompSections);

		// askCompCenter
		askCompCenter = new AskCompCenter(this.askLogic);
		askCompCenter.setImmediate(false);
		askCompCenter.setWidth("100.0%");
		askCompCenter.setHeight("100.0%");
		horizontalLayout_1.addComponent(askCompCenter);
		horizontalLayout_1.setExpandRatio(askCompCenter, 1.0f);

		return horizontalLayout_1;
	}

	@Override
	public void addCompsForLoggedUser(List<AbstractComponent> listComponentForLoggedUser) throws AskException {
		this.askCompHeader.addCompsForLoggedUser(listComponentForLoggedUser);
		this.askCompSections.addCompsForLoggedUser(listComponentForLoggedUser);
		this.askCompCenter.addCompsForLoggedUser(listComponentForLoggedUser);
		this.askCompFooter.addCompsForLoggedUser(listComponentForLoggedUser);
	}

	@Override
	public void addCompsForNoUser(List<AbstractComponent> listComponentForNoUser) throws AskException {
		this.askCompHeader.addCompsForNoUser(listComponentForNoUser);
		this.askCompSections.addCompsForNoUser(listComponentForNoUser);
		this.askCompCenter.addCompsForNoUser(listComponentForNoUser);
		this.askCompFooter.addCompsForNoUser(listComponentForNoUser);
	}

	public void setVisibility() throws AskException {
		this.askCompHeader.setVisibility();
	}

	public void postLogin() throws AskException {
		this.askCompHeader.postLogin();
		this.askCompSections.postLogin();
		this.askCompCenter.postLogin();
		this.askCompFooter.postLogin();
	}

	public void postLogout() throws AskException {
		this.askCompHeader.postLogout();
		this.askCompSections.postLogout();
		this.askCompCenter.postLogout();
		this.askCompFooter.postLogout();
	}

	@Override
	public void doAction(AskActionType askActionType) throws AskException {
		// 1st thing to do, depending on center component other components could
		// change
		this.askCompCenter.doAction(askActionType);

		this.askCompHeader.doAction(askActionType);
		this.askCompSections.doAction(askActionType);
		this.askCompFooter.doAction(askActionType);
	}

	@Override
	public void doEnter() throws AskException {
	}

	@Override
	public void onFocus(Object objectForOnFocus) throws AskException {
	}

	@Override
	public void doClick(AskActionType askActionType) throws AskException {
	}

}
