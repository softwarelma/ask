package com.alten.ask.view.comp;

import java.util.List;

import com.alten.ask.AskException;
import com.alten.ask.logic.AskActionInterface;
import com.alten.ask.logic.AskClickInterface;
import com.alten.ask.logic.AskEnterInterface;
import com.vaadin.ui.AbstractComponent;

public interface AskCompInterface extends AskActionInterface, AskEnterInterface, AskClickInterface {

	public void init() throws AskException;

	public void addCompsForLoggedUser(List<AbstractComponent> listComponentForLoggedUser) throws AskException;

	public void addCompsForNoUser(List<AbstractComponent> listComponentForNoUser) throws AskException;

}
