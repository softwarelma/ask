package com.alten.ask.view.comp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import com.alten.ask.AskAbstractStaticAttributes;
import com.alten.ask.AskAbstractStaticMethods;
import com.alten.ask.AskEmailSender;
import com.alten.ask.AskException;
import com.alten.ask.AskLogger;
import com.alten.ask.AskLoggerManager;
import com.alten.ask.logic.AskLogic;
import com.alten.ask.model.orm.AskInvoiceDetail;
import com.alten.ask.model.orm.AskInvoiceHead;
import com.alten.ask.model.orm.AskProduct;
import com.alten.ask.model.orm.AskTxAxDetail;
import com.alten.ask.model.orm.AskTxHead;
import com.alten.ask.model.orm.AskUser;
import com.alten.ask.view.AskView;
import com.alten.ask.view.comp.AskCompGrid.AskGridSelectionInterface;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class AskCompCenter extends CustomComponent implements AskCompInterface, AskGridSelectionInterface {

	private static final long serialVersionUID = 1L;
	private final AskLogger logger;

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	private VerticalLayout mainLayout;
	private Label label_1;
	private final AskLogic askLogic;
	// invoices or transactions
	private AskCompGrid askCompGridTop;
	// products or details or actions
	private AskCompGrid askCompGridMainOrBottom;
	private Map<Object, AskProduct> mapIdProduct = new HashMap<Object, AskProduct>();
	private Map<Object, AskInvoiceHead> mapIdInvoice = new HashMap<Object, AskInvoiceHead>();
	private Map<Object, AskTxHead> mapIdTx = new HashMap<Object, AskTxHead>();

	public AskCompCenter(AskLogic askLogic) {
		this.askLogic = askLogic;
		this.logger = AskLoggerManager.getLogger(this.askLogic, AskView.class.getName());
		buildMainLayout();
		setCompositionRoot(mainLayout);
		this.label_1.setContentMode(ContentMode.HTML);
		this.askCompGridTop.setVisible(false);
	}

	private VerticalLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new VerticalLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		mainLayout.setMargin(true);

		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");

		// askCompGridTop
		askCompGridTop = new AskCompGrid(this.askLogic);
		askCompGridTop.setImmediate(false);
		askCompGridTop.setWidth("100.0%");
		askCompGridTop.setHeight("100.0%");
		mainLayout.addComponent(askCompGridTop);
		mainLayout.setExpandRatio(askCompGridTop, 0.25f);

		// askCompGridMainOrBottom
		askCompGridMainOrBottom = new AskCompGrid(this.askLogic);
		askCompGridMainOrBottom.setImmediate(false);
		askCompGridMainOrBottom.setWidth("100.0%");
		askCompGridMainOrBottom.setHeight("100.0%");
		mainLayout.addComponent(askCompGridMainOrBottom);
		mainLayout.setExpandRatio(askCompGridMainOrBottom, 0.25f);

		// label_1
		label_1 = new Label();
		label_1.setImmediate(false);
		label_1.setWidth("-1px");
		label_1.setHeight("-1px");
		label_1.setValue("Total:&nbsp;");
		mainLayout.addComponent(label_1);
		// mainLayout.setExpandRatio(label_1, 1.0f);
		mainLayout.setComponentAlignment(label_1, Alignment.BOTTOM_RIGHT);

		return mainLayout;
	}

	@Override
	public void init() throws AskException {
		this.askCompGridMainOrBottom.init();
	}

	public void postLogin() throws AskException {
	}

	public void postLogout() throws AskException {
	}

	@Override
	public void addCompsForLoggedUser(List<AbstractComponent> listComponentForLoggedUser) throws AskException {
	}

	@Override
	public void addCompsForNoUser(List<AbstractComponent> listComponentForNoUser) throws AskException {
	}

	public void navigateToHome(String username) throws AskException {
		this.askCompGridTop.setVisible(false);
		this.label_1.setVisible(false);
		List<AskProduct> askProducts = this.askLogic.findAllProducts();
		String colPrice = this.askLogic.getResourceBundleString("column.price");
		String[] colNames = { this.askLogic.getResourceBundleString("column.name"),
				this.askLogic.getResourceBundleString("column.ean"),
				this.askLogic.getResourceBundleString("column.imported"),
				this.askLogic.getResourceBundleString("column.category"), colPrice };
		Class<?>[] types = { String.class, String.class, String.class, String.class, BigDecimal.class };
		String colsForRightAlignment = "," + colPrice + ",";
		this.askCompGridMainOrBottom.init(username, colNames, types, SelectionMode.MULTI,
				this.askLogic.getResourceBundleString("title.products.shopping"), colsForRightAlignment);
		this.mapIdProduct.clear();
		Object id;

		/*
		 * using following columns
		 * 
		 * private String description;
		 * 
		 * private String ean;
		 * 
		 * private Boolean imported;
		 * 
		 * private AskCodeDetail askCodeDetail;
		 * 
		 * private BigDecimal price;
		 */

		for (AskProduct askProduct : askProducts) {
			id = this.askCompGridMainOrBottom.addRow(
					askProduct.getDescription(),
					askProduct.getEan(),
					askProduct.getImported() ? this.askLogic.getResourceBundleString("value.yes") : this.askLogic
							.getResourceBundleString("value.no"), askProduct.getAskCodeDetail().getDescription(),
					askProduct.getPrice());
			this.mapIdProduct.put(id, askProduct);
		}
	}

	private void navigateToCart() throws AskException {
		AskUser askUser = this.askLogic.getAskUserFromSession();
		AskInvoiceHead askInvoiceHead = this.askLogic.getAskInvoiceHeadFromSession();

		if (askUser == null || askInvoiceHead == null) {
			return;
		}

		this.askCompGridTop.setVisible(false);
		this.label_1.setVisible(true);
		AskProduct askProduct;
		String colPrice = this.askLogic.getResourceBundleString("column.price");
		String colTaxes = this.askLogic.getResourceBundleString("column.taxes");
		String colQuantity = this.askLogic.getResourceBundleString("column.quantity");
		String colTotal = this.askLogic.getResourceBundleString("column.total");
		String[] colNames = { this.askLogic.getResourceBundleString("column.name"),
				this.askLogic.getResourceBundleString("column.ean"),
				this.askLogic.getResourceBundleString("column.imported"),
				this.askLogic.getResourceBundleString("column.category"), colPrice, colTaxes, colQuantity, colTotal };
		Class<?>[] types = { String.class, String.class, String.class, String.class, BigDecimal.class,
				BigDecimal.class, Integer.class, BigDecimal.class };
		String colsForRightAlignment = "," + colPrice + "," + colTaxes + "," + colQuantity + "," + colTotal + ",";
		this.askCompGridMainOrBottom.init(askUser.getUsername(), colNames, types, SelectionMode.MULTI,
				this.askLogic.getResourceBundleString("title.products.cart"), colsForRightAlignment);
		BigDecimal totalPriceByDetail;
		BigDecimal totalTaxByInvoice = BigDecimal.ZERO;
		Object id;
		this.mapIdProduct.clear();

		for (AskInvoiceDetail askInvoiceDetail : askInvoiceHead.getAskInvoiceDetails()) {
			totalPriceByDetail = AskAbstractStaticMethods.getTotalPrice(this.askLogic, askUser.getUsername(),
					askInvoiceDetail);
			totalTaxByInvoice = totalTaxByInvoice.add(askInvoiceDetail.getTax().multiply(
					BigDecimal.valueOf(askInvoiceDetail.getQuantity())));
			askProduct = askInvoiceDetail.getAskProduct();
			id = this.askCompGridMainOrBottom.addRow(
					askProduct.getDescription(),
					askProduct.getEan(),
					askProduct.getImported() ? this.askLogic.getResourceBundleString("value.yes") : this.askLogic
							.getResourceBundleString("value.no"), askProduct.getAskCodeDetail().getDescription(),
					askProduct.getPrice(), askInvoiceDetail.getTax(), askInvoiceDetail.getQuantity(),
					totalPriceByDetail);
			this.mapIdProduct.put(id, askProduct);
		}

		askInvoiceHead.setTax(totalTaxByInvoice);
		BigDecimal totalPriceByInvoice = AskAbstractStaticMethods.getTotalPrice(this.askLogic, askUser.getUsername(),
				askInvoiceHead);
		askInvoiceHead.setPrice(totalPriceByInvoice);
		this.label_1.setValue("Total:&nbsp;" + totalPriceByInvoice);
	}

	private void navigateToInvoices() throws AskException {
		AskUser askUser = this.askLogic.getAskUserFromSession();
		AskInvoiceHead askInvoiceHead = this.askLogic.getAskInvoiceHeadFromSession();

		if (askUser == null || askInvoiceHead == null) {
			return;
		}

		this.askCompGridTop.setVisible(true);
		String colQuantity = this.askLogic.getResourceBundleString("column.quantity");
		String colTaxes = this.askLogic.getResourceBundleString("column.taxes");
		String colTotal = this.askLogic.getResourceBundleString("column.total");
		String[] colNames = { this.askLogic.getResourceBundleString("column.date"), colQuantity, colTaxes, colTotal };
		Class<?>[] types = { Timestamp.class, Integer.class, BigDecimal.class, BigDecimal.class };
		String colsForRightAlignment = "," + colQuantity + "," + colTaxes + "," + colTotal + ",";
		this.askCompGridTop.init(askUser.getUsername(), colNames, types, SelectionMode.SINGLE,
				this.askLogic.getResourceBundleString("title.invoices"), colsForRightAlignment);
		List<AskInvoiceHead> listAskInvoiceHead = this.askLogic.findAllNonActiveInvoices();
		Object id;
		this.mapIdInvoice.clear();

		for (AskInvoiceHead askInvoiceHead2 : listAskInvoiceHead) {
			id = this.askCompGridTop.addRow(askInvoiceHead2.getPurchasingTime(), askInvoiceHead2.getAskInvoiceDetails()
					.size(), askInvoiceHead2.getTax(), askInvoiceHead2.getPrice());
			this.mapIdInvoice.put(id, askInvoiceHead2);
		}

		this.label_1.setVisible(false);
		String colPrice = this.askLogic.getResourceBundleString("column.price");
		colNames = new String[] { this.askLogic.getResourceBundleString("column.name"),
				this.askLogic.getResourceBundleString("column.ean"),
				this.askLogic.getResourceBundleString("column.imported"),
				this.askLogic.getResourceBundleString("column.category"), colPrice, colTaxes, colQuantity, colTotal };
		types = new Class<?>[] { String.class, String.class, String.class, String.class, BigDecimal.class,
				BigDecimal.class, Integer.class, BigDecimal.class };
		colsForRightAlignment = "," + colPrice + "," + colTaxes + "," + colQuantity + "," + colTotal + ",";
		this.askCompGridMainOrBottom.init(askUser.getUsername(), colNames, types, SelectionMode.NONE,
				this.askLogic.getResourceBundleString("title.products.purchased"), colsForRightAlignment);
		this.askCompGridTop.addSelectionListener(AskActionType.navigateToInvoices, this);
	}

	private void navigateToTx() throws AskException {
		AskUser askUser = this.askLogic.getAskUserFromSession();

		if (askUser == null) {
			return;
		}

		this.askCompGridTop.setVisible(true);
		String colCreationTime = this.askLogic.getResourceBundleString("column.tx.creationTime");
		String colDescription = this.askLogic.getResourceBundleString("column.tx.description");
		String colModificationTime = this.askLogic.getResourceBundleString("column.tx.modificationTime");
		String[] colNames = { colCreationTime, colDescription, colModificationTime };
		Class<?>[] types = { Timestamp.class, String.class, Timestamp.class };
		String colsForRightAlignment = "";
		this.askCompGridTop.init(askUser.getUsername(), colNames, types, SelectionMode.SINGLE,
				this.askLogic.getResourceBundleString("title.tx"), colsForRightAlignment);
		List<AskTxHead> listAskTxHead = this.askLogic.findAllAskTxHead(askUser.getUsername());
		Object id;
		this.mapIdTx.clear();

		for (AskTxHead askTxHead : listAskTxHead) {
			id = this.askCompGridTop.addRow(askTxHead.getCreationTime(), askTxHead.getDescription(),
					askTxHead.getModificationTime());
			this.mapIdTx.put(id, askTxHead);
		}

		this.label_1.setVisible(false);
		String colEntity = this.askLogic.getResourceBundleString("column.ax.entity");
		colNames = new String[] { colCreationTime, colDescription, colModificationTime, colEntity };
		types = new Class<?>[] { Timestamp.class, String.class, Timestamp.class, String.class };
		colsForRightAlignment = "";
		this.askCompGridMainOrBottom.init(askUser.getUsername(), colNames, types, SelectionMode.NONE,
				this.askLogic.getResourceBundleString("title.ax"), colsForRightAlignment);
		this.askCompGridTop.addSelectionListener(AskActionType.navigateToTx, this);
	}

	@Override
	public void doSelection(AskActionType askActionType) throws AskException {
		switch (askActionType) {
		case navigateToInvoices:
			this.doSelectionNavigateToInvoices();
			break;
		case navigateToTx:
			this.doSelectionNavigateToTx();
			break;
		default:
			AskUser askUser = this.askLogic.getAskUserFromSession();
			String username = askUser == null ? AskAbstractStaticAttributes.NO_USER : askUser.getUsername();
			throw new AskException(this.askLogic, "Unknown askActionType \"" + askActionType + "\".", username);
		}
	}

	private void doSelectionNavigateToInvoices() throws AskException {
		AskUser askUser = this.askLogic.getAskUserFromSession();

		if (askUser == null) {
			return;
		}

		String user = AskAbstractStaticMethods.getAskUserDenomination(askUser);
		Collection<Object> collectionId = this.askCompGridTop.getSelectedRowsNoReset();

		if (collectionId.size() != 1) {
			return;
		}

		String colPrice = this.askLogic.getResourceBundleString("column.price");
		String colTaxes = this.askLogic.getResourceBundleString("column.taxes");
		String colQuantity = this.askLogic.getResourceBundleString("column.quantity");
		String colTotal = this.askLogic.getResourceBundleString("column.total");
		String[] colNames = { this.askLogic.getResourceBundleString("column.name"),
				this.askLogic.getResourceBundleString("column.ean"),
				this.askLogic.getResourceBundleString("column.imported"),
				this.askLogic.getResourceBundleString("column.category"), colPrice, colTaxes, colQuantity, colTotal };
		Class<?>[] types = { String.class, String.class, String.class, String.class, BigDecimal.class,
				BigDecimal.class, Integer.class, BigDecimal.class };
		String colsForRightAlignment = "," + colPrice + "," + colTaxes + "," + colQuantity + "," + colTotal + ",";
		this.askCompGridMainOrBottom.init(askUser.getUsername(), colNames, types, SelectionMode.NONE,
				this.askLogic.getResourceBundleString("title.products.purchased"), colsForRightAlignment);
		AskInvoiceHead askInvoiceHead;
		BigDecimal totalPriceByDetail;
		StringBuilder sb = new StringBuilder();
		String sep;

		for (Object id : collectionId) {
			askInvoiceHead = this.mapIdInvoice.get(id);
			logger.log(Level.INFO, "User \"" + user + "\", doing selection on invoice id " + askInvoiceHead.getId()
					+ ".");
			sep = "";

			for (AskInvoiceDetail askInvoiceDetail : askInvoiceHead.getAskInvoiceDetails()) {
				sb.append(sep);
				sep = ", ";
				sb.append(askInvoiceDetail.getId());
				sb.append("/");
				sb.append(askInvoiceDetail.getAskProduct().getId());
				totalPriceByDetail = askInvoiceDetail.getPrice().add(askInvoiceDetail.getTax())
						.multiply(BigDecimal.valueOf(askInvoiceDetail.getQuantity()));
				this.askCompGridMainOrBottom.addRow(askInvoiceDetail.getDescription(), askInvoiceDetail.getEan(),
						askInvoiceDetail.getImported() ? this.askLogic.getResourceBundleString("value.yes")
								: this.askLogic.getResourceBundleString("value.no"), askInvoiceDetail
								.getAskCodeDetail().getDescription(), askInvoiceDetail.getPrice(), askInvoiceDetail
								.getTax(), askInvoiceDetail.getQuantity(), totalPriceByDetail);
			}

			logger.log(Level.INFO, "User \"" + user + "\", retrieved pairs of idDetail/idProduct: " + sb + ".");
		}
	}

	private void doSelectionNavigateToTx() throws AskException {
		AskUser askUser = this.askLogic.getAskUserFromSession();

		if (askUser == null) {
			return;
		}

		String user = AskAbstractStaticMethods.getAskUserDenomination(askUser);
		Collection<Object> collectionId = this.askCompGridTop.getSelectedRowsNoReset();

		if (collectionId.size() != 1) {
			return;
		}

		String colCreationTime = this.askLogic.getResourceBundleString("column.tx.creationTime");
		String colDescription = this.askLogic.getResourceBundleString("column.tx.description");
		String colModificationTime = this.askLogic.getResourceBundleString("column.tx.modificationTime");
		String colEntity = this.askLogic.getResourceBundleString("column.ax.entity");
		String[] colNames = new String[] { colCreationTime, colDescription, colModificationTime, colEntity };
		Class<?>[] types = new Class<?>[] { Timestamp.class, String.class, Timestamp.class, String.class };
		String colsForRightAlignment = "";
		this.askCompGridMainOrBottom.init(askUser.getUsername(), colNames, types, SelectionMode.NONE,
				this.askLogic.getResourceBundleString("title.ax"), colsForRightAlignment);
		AskTxHead askTxHead;
		StringBuilder sb = new StringBuilder();
		StringBuilder sb2;
		String sep;

		for (Object id : collectionId) {
			askTxHead = this.mapIdTx.get(id);
			this.askLogic.refresh(askTxHead);
			logger.log(Level.INFO, "User \"" + user + "\", doing selection on transaction id " + askTxHead.getId()
					+ ".");
			sep = "";

			askTxHead.getAskTxAxDetails().sort(new Comparator<AskTxAxDetail>() {

				@Override
				public int compare(AskTxAxDetail o1, AskTxAxDetail o2) {
					return o2.getId().compareTo(o1.getId());
				}

			});

			for (AskTxAxDetail askTxAxDetail : askTxHead.getAskTxAxDetails()) {
				sb.append(sep);
				sep = ", ";
				sb.append(askTxAxDetail.getId());
				this.askCompGridMainOrBottom.addRow(askTxAxDetail.getCreationTime(), askTxAxDetail.getDescription(),
						askTxAxDetail.getModificationTime(), askTxAxDetail.getEntityAsString());
			}

			sb2 = new StringBuilder("User \"");
			sb2.append(user);
			sb2.append("\", retrieved actions: ");
			sb2.append(this.getLast3ByComma(sb));
			sb2.append(".");
			logger.log(Level.INFO, sb2.toString());
		}
	}

	private String getLast3ByComma(StringBuilder sb) {
		String s = sb.toString();
		String[] as;

		if (s.contains(",")) {
			as = s.split(",");
			s = "[...] ";

			if (as.length > 2) {
				s = s + as[as.length - 3] + ", " + as[as.length - 2] + ", " + as[as.length - 1];
			} else if (as.length == 2) {
				s = s + as[0] + ", " + as[1];
			} else if (as.length == 1) {
				s = s + as[0];
			}
		}

		return s;
	}

	private void doAddProduct() throws AskException {
		AskUser askUser = this.askLogic.getAskUserFromSession();
		AskInvoiceHead askInvoiceHead = this.askLogic.getAskInvoiceHeadFromSession();
		Collection<Object> collectionId = this.askCompGridMainOrBottom.getSelectedRowsAndReset();

		if (askUser == null || askInvoiceHead == null || collectionId.isEmpty()) {
			return;
		}

		String user = AskAbstractStaticMethods.getAskUserDenomination(askUser);
		logger.log(Level.INFO, "User \"" + user + "\", adding to cart on invoice id " + askInvoiceHead.getId() + ".");
		AskProduct askProduct;
		AskInvoiceDetail askInvoiceDetail;
		Timestamp timestamp = AskAbstractStaticMethods.getCurrentTimestamp();
		BigDecimal tax;
		StringBuilder sb = new StringBuilder();
		String sep = "";

		for (Object id : collectionId) {
			askProduct = this.mapIdProduct.get(id);
			tax = AskAbstractStaticMethods.getTax(this.askLogic, askUser.getUsername(), askProduct);
			askInvoiceDetail = this.getAskInvoiceDetail(askUser, askInvoiceHead, askProduct);

			if (askInvoiceDetail != null) {
				sb.append(sep);
				sep = ", ";
				sb.append(askInvoiceDetail.getId());
				sb.append("/");
				sb.append(askInvoiceDetail.getAskProduct().getId());
				askInvoiceDetail.setDescription(askProduct.getDescription());
				askInvoiceDetail.setEan(askProduct.getEan());
				askInvoiceDetail.setImported(askProduct.getImported());
				askInvoiceDetail.setModificationTime(timestamp);
				askInvoiceDetail.setPrice(askProduct.getPrice());
				askInvoiceDetail.setProductDescription(askProduct.getDescription());
				askInvoiceDetail.setTax(tax);
				askInvoiceDetail.setAskCodeDetail(askProduct.getAskCodeDetail());
				// askInvoiceDetail.setAskInvoiceHead(askInvoiceHead);
				// askInvoiceDetail.setAskProduct(askProduct);
				askInvoiceDetail.setQuantity(askInvoiceDetail.getQuantity() + 1);
				this.askLogic.saveAskInvoiceDetail(askInvoiceDetail);
				continue;
			}

			askInvoiceDetail = new AskInvoiceDetail(timestamp, askProduct.getDescription(), askProduct.getEan(),
					askProduct.getImported(), timestamp, askProduct.getPrice(), askProduct.getDescription(), tax,
					askProduct.getAskCodeDetail(), askInvoiceHead, askProduct, 1);
			askInvoiceHead.addAskInvoiceDetail(askInvoiceDetail);
			this.askLogic.saveAskInvoiceDetail(askInvoiceDetail);
			sb.append(sep);
			sep = ", ";
			sb.append(askInvoiceDetail.getId());
			sb.append("/");
			sb.append(askInvoiceDetail.getAskProduct().getId());
		}

		logger.log(Level.INFO, "User \"" + user + "\", there were added to cart the pairs of idDetail/idProduct: " + sb
				+ ".");
		// saving each detail at a time in order to have the id for log purpose
		// this.askLogic.saveAllAskInvoiceDetail(askInvoiceHead.getAskInvoiceDetails());
	}

	private void doRemoveProduct() throws AskException {
		AskUser askUser = this.askLogic.getAskUserFromSession();
		AskInvoiceHead askInvoiceHead = this.askLogic.getAskInvoiceHeadFromSession();
		Collection<Object> collectionId = this.askCompGridMainOrBottom.getSelectedRowsAndReset();

		if (askUser == null || askInvoiceHead == null || askInvoiceHead.getAskInvoiceDetails().isEmpty()
				|| collectionId.isEmpty()) {
			return;
		}

		AskProduct askProduct;
		AskInvoiceDetail askInvoiceDetail;
		String user = AskAbstractStaticMethods.getAskUserDenomination(askUser);
		logger.log(Level.INFO,
				"User \"" + user + "\", doing remove product from cart on invoice id " + askInvoiceHead.getId() + ".");
		StringBuilder sb = new StringBuilder();
		String sep = "";

		for (Object id : collectionId) {
			askProduct = this.mapIdProduct.get(id);
			askInvoiceDetail = this.getAskInvoiceDetail(askUser, askInvoiceHead, askProduct);
			AskAbstractStaticMethods.validateNotNull(this.askLogic, askUser.getUsername(), askInvoiceDetail,
					"askInvoiceDetail");
			askInvoiceHead.removeAskInvoiceDetail(askInvoiceDetail);
			sb.append(sep);
			sep = ", ";
			sb.append(askInvoiceDetail.getId());
			sb.append("/");
			sb.append(askInvoiceDetail.getAskProduct().getId());
			this.askLogic.deleteAskInvoiceDetail(askInvoiceDetail);
		}

		logger.log(Level.INFO, "User \"" + user + "\", there were removed the pairs of idDetail/idProduct: " + sb + ".");
		// this.askLogic.saveAskInvoiceHead(askInvoiceHead);
		this.navigateToCart();
	}

	private void doPurchase() throws AskException {
		AskUser askUser = this.askLogic.getAskUserFromSession();
		AskInvoiceHead askInvoiceHead = this.askLogic.getAskInvoiceHeadFromSession();

		if (askUser == null || askInvoiceHead == null || askInvoiceHead.getAskInvoiceDetails().isEmpty()) {
			return;
		}

		String user = AskAbstractStaticMethods.getAskUserDenomination(askUser);
		logger.log(Level.INFO, "User \"" + user + "\", doing purchase on invoice id " + askInvoiceHead.getId() + ".");
		// setting as not active and saving the current invoice
		Timestamp timestamp = AskAbstractStaticMethods.getCurrentTimestamp();
		askInvoiceHead.setActive(false);// to indicate that the order is already
										// paid
		askInvoiceHead.setModificationTime(timestamp);
		askInvoiceHead.setPurchasingTime(timestamp);
		this.askLogic.saveAskInvoiceHead(askInvoiceHead);
		StringBuilder sb = new StringBuilder();
		String sep = "";

		for (AskInvoiceDetail askInvoiceDetail : askInvoiceHead.getAskInvoiceDetails()) {
			sb.append(sep);
			sep = ", ";
			sb.append(askInvoiceDetail.getId());
			sb.append("/");
			sb.append(askInvoiceDetail.getAskProduct().getId());
		}

		logger.log(Level.INFO, "User \"" + user + "\", there were purchased the pairs of idDetail/idProduct: " + sb
				+ ".");
		this.sendEmail(askUser, askInvoiceHead);
		this.askLogic.newCurrentInvoice();
		this.navigateToCart();
	}

	private void sendEmail(AskUser askUser, AskInvoiceHead askInvoiceHead) throws AskException {
		String subject = this.getEmailSubject(askUser, askInvoiceHead);
		String body = this.getEmailBody(askUser, askInvoiceHead);
		new AskEmailSender().sendEmail(this.askLogic, askUser.getUsername(), subject, body);
	}

	private String getEmailSubject(AskUser askUser, AskInvoiceHead askInvoiceHead) throws AskException {
		return this.askLogic.getResourceBundleString("email.purchasing") + " id " + askUser.getId() + ", "
				+ this.askLogic.getResourceBundleString("email.invoice") + " id " + askInvoiceHead.getId();
	}

	private String getEmailBody(AskUser askUser, AskInvoiceHead askInvoiceHead) throws AskException {
		String user = AskAbstractStaticMethods.getAskUserDenomination(askUser);
		String invoice = AskAbstractStaticMethods.getAskInvoiceHeadDenomination(askInvoiceHead);
		String body = this.askLogic.getResourceBundleString("email.hello") + ",\r\n\r\n"
				+ this.askLogic.getResourceBundleString("email.the_user") + " " + user
				+ this.askLogic.getResourceBundleString("email.has_purchased") + " \r\n" + invoice + ".\r\n\r\n\r\n"
				+ this.askLogic.getResourceBundleString("email.regards");
		return body;
	}

	private AskInvoiceDetail getAskInvoiceDetail(AskUser askUser, AskInvoiceHead askInvoiceHead, AskProduct askProduct)
			throws AskException {
		AskAbstractStaticMethods
				.validateNotNull(this.askLogic, askUser.getUsername(), askInvoiceHead, "askInvoiceHead");
		AskAbstractStaticMethods.validateNotNull(this.askLogic, askUser.getUsername(), askProduct, "askProduct");

		for (AskInvoiceDetail askInvoiceDetail : askInvoiceHead.getAskInvoiceDetails()) {
			if (askInvoiceDetail.getAskProduct().equals(askProduct)) {
				return askInvoiceDetail;
			}
		}

		return null;
	}

	@Override
	public void doAction(AskActionType askActionType) throws AskException {
		AskUser askUser = this.askLogic.getAskUserFromSession();
		String username = askUser == null ? AskAbstractStaticAttributes.NO_USER : askUser.getUsername();

		switch (askActionType) {
		case navigateToHome:
			this.navigateToHome(username);
			break;
		case doAddProduct:
			this.doAddProduct();
			break;
		case navigateToInvoices:
			this.navigateToInvoices();
			break;
		case navigateToCart:
			this.navigateToCart();
			break;
		case doRemoveProduct:
			this.doRemoveProduct();
			break;
		case doPurchase:
			this.doPurchase();
			break;
		case navigateToTx:
			this.navigateToTx();
			break;
		case doLangEn:
			break;
		case doLangEs:
			break;
		case doLangIt:
			break;
		default:
			throw new AskException(this.askLogic, "Unknown askActionType " + askActionType, username);
		}
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
