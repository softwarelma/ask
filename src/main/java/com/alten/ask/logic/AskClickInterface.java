package com.alten.ask.logic;

import java.io.Serializable;

import com.alten.ask.AskException;
import com.alten.ask.logic.AskActionInterface.AskActionType;

public interface AskClickInterface extends Serializable {

	public void doClick(AskActionType askActionType) throws AskException;

}
