package com.alten.ask.logic;

import java.io.Serializable;

import com.alten.ask.AskException;

public interface AskEnterInterface extends Serializable {

	public void doEnter() throws AskException;

	public void onFocus(Object objectForOnFocus) throws AskException;

}
