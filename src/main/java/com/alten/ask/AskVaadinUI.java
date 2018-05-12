package com.alten.ask;

import org.springframework.beans.factory.annotation.Autowired;

import com.alten.ask.logic.AskLogic;
import com.alten.ask.model.orm.AskCodeDetailDao;
import com.alten.ask.model.orm.AskCodeHeadDao;
import com.alten.ask.model.orm.AskInvoiceDetailDao;
import com.alten.ask.model.orm.AskInvoiceHeadDao;
import com.alten.ask.model.orm.AskProductDao;
import com.alten.ask.model.orm.AskTxAxDetailDao;
import com.alten.ask.model.orm.AskTxHeadDao;
import com.alten.ask.model.orm.AskUserDao;
import com.alten.ask.model.orm.AskUserPropDao;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

//@Controller
@SpringUI
public class AskVaadinUI extends UI {

	private static final long serialVersionUID = 1L;
	private static final AskLogger logger = AskLoggerManager.getLogger(null, AskMainApplication.class.getName());

	private AskLogic askLogic;

	@Autowired
	AskCodeDetailDao askCodeDetailDao;
	@Autowired
	AskCodeHeadDao askCodeHeadDao;
	@Autowired
	AskInvoiceDetailDao askInvoiceDetailDao;
	@Autowired
	AskInvoiceHeadDao askInvoiceHeadDao;
	@Autowired
	AskProductDao askProductDao;
	@Autowired
	AskTxAxDetailDao askTxAxDetailDao;
	@Autowired
	AskTxHeadDao askTxHeadDao;
	@Autowired
	AskUserDao askUserDao;
	@Autowired
	AskUserPropDao askUserPropDao;

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		logger.log(logger.getLevel(), "Running Vaadin Application...");
		setTheme("ask");
		final VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		layout.setMargin(false);
		setContent(layout);

		try {
			this.askLogic = new AskLogic();

			this.askLogic.setAskCodeDetailDao(this.askCodeDetailDao);
			this.askLogic.setAskCodeHeadDao(this.askCodeHeadDao);
			this.askLogic.setAskInvoiceDetailDao(this.askInvoiceDetailDao);
			this.askLogic.setAskInvoiceHeadDao(this.askInvoiceHeadDao);
			this.askLogic.setAskProductDao(this.askProductDao);
			this.askLogic.setAskTxAxDetailDao(this.askTxAxDetailDao);
			this.askLogic.setAskTxHeadDao(this.askTxHeadDao);
			this.askLogic.setAskUserDao(this.askUserDao);
			this.askLogic.setAskUserPropDao(this.askUserPropDao);

			this.askLogic.globalInit();
			this.askLogic.addMainComponent(layout);
		} catch (Exception e) {
			if (!(e instanceof AskException)) {
				try {
					throw new AskException(this.askLogic, "Wrapping exception for vaadin request init",
							AskAbstractStaticAttributes.NO_USER, e);
				} catch (AskException e2) {
				}
			}
		}
	}

}