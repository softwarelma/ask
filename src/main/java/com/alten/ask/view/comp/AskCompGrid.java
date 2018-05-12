package com.alten.ask.view.comp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;

import com.alten.ask.AskAbstractStaticAttributes;
import com.alten.ask.AskAbstractStaticMethods;
import com.alten.ask.AskException;
import com.alten.ask.AskLogger;
import com.alten.ask.AskLoggerManager;
import com.alten.ask.logic.AskLogic;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.HtmlRenderer;

public class AskCompGrid extends CustomComponent implements AskCompInterface {

	private static final long serialVersionUID = 1L;

	private final AskLogger logger;
	private final AskLogic askLogic;

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	private VerticalLayout mainLayout;
	private Grid grid;

	public AskCompGrid(AskLogic askLogic) {
		this.askLogic = askLogic;
		this.logger = AskLoggerManager.getLogger(this.askLogic, AskCompGrid.class.getName());
		buildMainLayout();
		setCompositionRoot(mainLayout);
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

		return mainLayout;
	}

	public void init(String username, String[] colNames, Class<?>[] types, SelectionMode selectionMode, String title,
			final String colsForRightAlignment) throws AskException {
		AskAbstractStaticMethods.validateNotNull(this.askLogic, username, colNames, "colNames");
		AskAbstractStaticMethods.validateNotNull(this.askLogic, username, types, "types");
		AskAbstractStaticMethods.validateNotNull(this.askLogic, username, selectionMode, "selectionMode");
		// the title could be a avoid string, ""
		AskAbstractStaticMethods.validateNotNull(this.askLogic, title, "title");

		if (colNames.length != types.length) {
			throw new AskException(this.askLogic,
					"Length mismatch between " + colNames.length + " and " + types.length, username);
		}

		if (this.grid != null) {
			this.mainLayout.removeComponent(this.grid);
		}

		// askCompGrid
		grid = new Grid(title);
		grid.setImmediate(false);
		grid.setWidth("100.0%");
		grid.setHeight("100.0%");
		mainLayout.addComponent(grid);
		// mainLayout.setExpandRatio(grid, 0.25f);

		this.grid.setSelectionMode(selectionMode);
		Column column;
		grid.setCellStyleGenerator(new Grid.CellStyleGenerator() {

			private static final long serialVersionUID = 1L;

			@Override
			public String getStyle(Grid.CellReference cellReference) {
				return colsForRightAlignment.contains("," + cellReference.getPropertyId() + ",") ? "askRightAligned"
						: "askLeftAligned";
			}

		});

		for (int i = 0; i < colNames.length; i++) {
			AskAbstractStaticMethods.validateNotNull(this.askLogic, username, colNames[i], "colNames[" + i + "]");
			AskAbstractStaticMethods.validateNotNull(this.askLogic, username, types[i], "types[" + i + "]");
			column = this.grid.addColumn(colNames[i], types[i]);

			// if (!colNames[i].equals("EAN")) {
			// continue;
			// }

			column.setConverter(this.getConverter(username, types[i]));
			column.setRenderer(new HtmlRenderer());
		}

		// TIPS
		// this.grid.getSelectionModel().reset();
		// this.grid.getContainerDataSource().removeAllItems();
		// Headers and Footers
		// HeaderRow row = grid.prependHeaderRow();
		// row.join("firstName", "lastName").setHtml("<b>Full name</b>");
		// FooterRow footer = grid.appendFooterRow();
		// footer.getCell("salary").setText("avg: 1528.55");
	}

	private Converter<?, ?> getConverter(String username, Class<?> clazz) throws AskException {
		// Converter<PRESENTATION, MODEL>, from:
		// https://vaadin.com/wiki?p_p_id=36&p_p_lifecycle=0&p_p_state=pop_up&p_p_mode=view&
		// _36_struts_action=%2Fwiki%2Fview&p_r_p_185834411_nodeName=vaadin.com+wiki&p_r_p_
		// 185834411_title=Formatting+data+in+Grid&_36_viewMode=print

		if (String.class.equals(clazz)) {
			return this.getConverterString();
		} else if (Timestamp.class.equals(clazz)) {
			return this.getConverterTimestamp();
		} else if (Integer.class.equals(clazz)) {
			return this.getConverterInteger();
		} else if (BigDecimal.class.equals(clazz)) {
			return this.getConverterBigDecimal();
		}

		throw new AskException(this.askLogic, "Unknown class \"" + clazz + "\"", username);
	}

	private Converter<String, String> getConverterString() {
		Converter<String, String> converter = new Converter<String, String>() {

			private static final long serialVersionUID = 1L;

			@Override
			public String convertToModel(String value, Class<? extends String> targetType, Locale locale)
					throws com.vaadin.data.util.converter.Converter.ConversionException {
				if (value != null && value.contains("\">") && value.contains("</")) {
					// reverting the convertToPresentation logic
					value = value.split("\">")[1].split("</")[0];
				}

				return value;
			}

			@Override
			public String convertToPresentation(String value, Class<? extends String> targetType, Locale locale)
					throws com.vaadin.data.util.converter.Converter.ConversionException {
				return "<a title=\"" + value.replace("\"", "&quot;") + "\">" + value + "</a>";
			}

			@Override
			public Class<String> getModelType() {
				return String.class;
			}

			@Override
			public Class<String> getPresentationType() {
				return String.class;
			}

		};

		return converter;
	}

	private Converter<String, Timestamp> getConverterTimestamp() {
		Converter<String, Timestamp> converter = new Converter<String, Timestamp>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Timestamp convertToModel(String value, Class<? extends Timestamp> targetType, Locale locale)
					throws com.vaadin.data.util.converter.Converter.ConversionException {
				if (value != null && value.contains("\">") && value.contains("</")) {
					// reverting the convertToPresentation logic
					value = value.split("\">")[1].split("</")[0];
				}

				try {
					return AskAbstractStaticMethods.getTimestamp(value);
				} catch (ParseException | NullPointerException e) {
					logger.log(Level.INFO, "Non-valid Timestamp \"" + value + "\". It should have the format \""
							+ AskAbstractStaticAttributes.DATE_TIME_FORMAT_STRING + "\"", e);
					return null;
				}
			}

			@Override
			public String convertToPresentation(Timestamp value, Class<? extends String> targetType, Locale locale)
					throws com.vaadin.data.util.converter.Converter.ConversionException {
				return "<a title=\"" + AskAbstractStaticMethods.getString(value) + " ("
						+ AskAbstractStaticAttributes.DATE_TIME_FORMAT_STRING + ")\">"
						+ AskAbstractStaticMethods.getString(value) + "</a>";
			}

			@Override
			public Class<Timestamp> getModelType() {
				return Timestamp.class;
			}

			@Override
			public Class<String> getPresentationType() {
				return String.class;
			}

		};

		return converter;
	}

	private Converter<String, Integer> getConverterInteger() {
		Converter<String, Integer> converter = new Converter<String, Integer>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Integer convertToModel(String value, Class<? extends Integer> targetType, Locale locale)
					throws com.vaadin.data.util.converter.Converter.ConversionException {
				if (value != null && value.contains("\">") && value.contains("</")) {
					// reverting the convertToPresentation logic
					value = value.split("\">")[1].split("</")[0];
				}

				try {
					return Integer.parseInt(value);
				} catch (NumberFormatException e) {
					logger.log(Level.INFO, "Non-valid Integer \"" + value + "\"", e);
					return null;
				}
			}

			@Override
			public String convertToPresentation(Integer value, Class<? extends String> targetType, Locale locale)
					throws com.vaadin.data.util.converter.Converter.ConversionException {
				return "<a title=\"" + value + "\">" + value + "</a>";
			}

			@Override
			public Class<Integer> getModelType() {
				return Integer.class;
			}

			@Override
			public Class<String> getPresentationType() {
				return String.class;
			}

		};

		return converter;
	}

	private Converter<String, BigDecimal> getConverterBigDecimal() {
		Converter<String, BigDecimal> converter = new Converter<String, BigDecimal>() {

			private static final long serialVersionUID = 1L;

			@Override
			public BigDecimal convertToModel(String value, Class<? extends BigDecimal> targetType, Locale locale)
					throws com.vaadin.data.util.converter.Converter.ConversionException {
				if (value != null && value.contains("\">") && value.contains("</")) {
					// reverting the convertToPresentation logic
					value = value.split("\">")[1].split("</")[0];
				}

				try {
					return new BigDecimal(value);
				} catch (NumberFormatException | NullPointerException e) {
					logger.log(Level.INFO, "Non-valid BigDecimal \"" + value + "\"", e);
					return null;
				}
			}

			@Override
			public String convertToPresentation(BigDecimal value, Class<? extends String> targetType, Locale locale)
					throws com.vaadin.data.util.converter.Converter.ConversionException {
				return "<a title=\"" + value + "\">" + value + "</a>";
			}

			@Override
			public Class<BigDecimal> getModelType() {
				return BigDecimal.class;
			}

			@Override
			public Class<String> getPresentationType() {
				return String.class;
			}

		};

		return converter;
	}

	public void addSelectionListener(final AskActionType askActionType, final AskGridSelectionInterface askGridSelection) {
		this.grid.addSelectionListener(new SelectionListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void select(SelectionEvent event) {
				try {
					askGridSelection.doSelection(askActionType);
				} catch (AskException e) {
				}
			}

		});
	}

	public interface AskGridSelectionInterface extends Serializable {

		public void doSelection(AskActionType askActionType) throws AskException;

	}

	@Override
	public void init() throws AskException {
	}

	/**
	 * @return the ID of the added row
	 */
	public Object addRow(Object... data) {
		Object id = this.grid.addRow(data);
		// Item item = this.grid.getContainerDataSource().getItem(id);
		// Property<?> prop;

		// for (Object idI : item.getItemPropertyIds()) {
		// prop = item.getItemProperty(idI);
		// }

		return id;
	}

	public Collection<Object> getSelectedRowsAndReset() {
		Collection<Object> collectionId = this.grid.getSelectedRows();
		this.grid.getSelectionModel().reset();
		return collectionId;
	}

	public Collection<Object> getSelectedRowsNoReset() {
		Collection<Object> collectionId = this.grid.getSelectedRows();
		return collectionId;
	}

	@Override
	public void addCompsForLoggedUser(List<AbstractComponent> listComponentForLoggedUser) throws AskException {
	}

	@Override
	public void addCompsForNoUser(List<AbstractComponent> listComponentForNoUser) throws AskException {
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
