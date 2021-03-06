package com.alten.ask.view.comp;

import java.util.List;

import com.alten.ask.AskException;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class AskCompFooter extends CustomComponent implements AskCompInterface {

	private static final long serialVersionUID = 1L;

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	private VerticalLayout mainLayout;
	private Label label_1;

	/**
	 * The constructor should first build the main layout, set the composition
	 * root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the visual
	 * editor.
	 */
	public AskCompFooter() {
		buildMainLayout();
		setCompositionRoot(mainLayout);
	}

	@Override
	public void init() {
	}

	private VerticalLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new VerticalLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("-1px");
		mainLayout.setMargin(false);

		// top-level component properties
		setWidth("100.0%");
		setHeight("-1px");

		// label_1
		label_1 = new Label();
		label_1.setImmediate(false);
		label_1.setWidth("-1px");
		label_1.setHeight("-1px");
		label_1.setValue("alten.com - 2015");
		mainLayout.addComponent(label_1);
		mainLayout.setComponentAlignment(label_1, new Alignment(20));

		return mainLayout;
	}

	@Override
	public void addCompsForLoggedUser(List<AbstractComponent> listComponentForLoggedUser) {
	}

	@Override
	public void addCompsForNoUser(List<AbstractComponent> listComponentForNoUser) {
	}

	public void postLogin() throws AskException {
	}

	public void postLogout() throws AskException {
	}

	@Override
	public void doAction(AskActionType askActionType) throws AskException {
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
