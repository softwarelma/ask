package com.alten.ask.model.orm;

import java.io.Serializable;

import javax.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * The persistent class for the ask_invoice_detail database table.
 * 
 */
@Entity
@Table(name = "ask_invoice_detail")
@NamedQuery(name = "AskInvoiceDetail.findAll", query = "SELECT a FROM AskInvoiceDetail a")
public class AskInvoiceDetail implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Timestamp creationTime;
	private String description;
	private String ean;
	private Boolean imported;
	private Timestamp modificationTime;
	private BigDecimal price;// of 1 product
	private String productDescription;
	private BigDecimal tax;// of 1 product
	private AskCodeDetail askCodeDetail;
	private AskInvoiceHead askInvoiceHead;
	private AskProduct askProduct;
	private Integer quantity;

	public AskInvoiceDetail() {
	}

	public AskInvoiceDetail(Timestamp creationTime, String description, String ean, Boolean imported,
			Timestamp modificationTime, BigDecimal price, String productDescription, BigDecimal tax,
			AskCodeDetail askCodeDetail, AskInvoiceHead askInvoiceHead, AskProduct askProduct, Integer quantity) {
		super();
		this.creationTime = creationTime;
		this.description = description;
		this.ean = ean;
		this.imported = imported;
		this.modificationTime = modificationTime;
		this.price = price == null ? null : price.setScale(2);
		this.productDescription = productDescription;
		this.tax = tax == null ? null : tax.setScale(2);
		this.askCodeDetail = askCodeDetail;
		this.askInvoiceHead = askInvoiceHead;
		this.askProduct = askProduct;
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "AskInvoiceDetail [id=" + id + ", creationTime=" + creationTime + ", description=" + description
				+ ", ean=" + ean + ", imported=" + imported + ", modificationTime=" + modificationTime + ", price="
				+ price + ", productDescription=" + productDescription + ", tax=" + tax + ", askCodeDetail="
				+ askCodeDetail + ", askInvoiceHead=" + askInvoiceHead + ", askProduct=" + askProduct + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AskInvoiceDetail other = (AskInvoiceDetail) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "creation_time", nullable = false)
	public Timestamp getCreationTime() {
		return this.creationTime;
	}

	public void setCreationTime(Timestamp creationTime) {
		this.creationTime = creationTime;
	}

	@Column(nullable = false, length = 90)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(nullable = false, length = 13)
	public String getEan() {
		return this.ean;
	}

	public void setEan(String ean) {
		this.ean = ean;
	}

	@Column(nullable = false)
	public Boolean getImported() {
		return this.imported;
	}

	public void setImported(Boolean imported) {
		this.imported = imported;
	}

	@Column(name = "modification_time", nullable = false)
	public Timestamp getModificationTime() {
		return this.modificationTime;
	}

	public void setModificationTime(Timestamp modificationTime) {
		this.modificationTime = modificationTime;
	}

	@Column(nullable = false, precision = 14, scale = 4)
	public BigDecimal getPrice() {
		return this.price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price == null ? null : price.setScale(2);
	}

	@Column(name = "product_description", nullable = false, length = 90)
	public String getProductDescription() {
		return this.productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	@Column(nullable = false, precision = 14, scale = 4)
	public BigDecimal getTax() {
		return this.tax;
	}

	public void setTax(BigDecimal tax) {
		this.tax = tax == null ? null : tax.setScale(2);
	}

	// uni-directional many-to-one association to AskCodeDetail
	@ManyToOne
	@JoinColumn(name = "id_code_product", nullable = false)
	public AskCodeDetail getAskCodeDetail() {
		return this.askCodeDetail;
	}

	public void setAskCodeDetail(AskCodeDetail askCodeDetail) {
		this.askCodeDetail = askCodeDetail;
	}

	// bi-directional many-to-one association to AskInvoiceHead
	@ManyToOne
	@JoinColumn(name = "id_invoice_head", nullable = false)
	public AskInvoiceHead getAskInvoiceHead() {
		return this.askInvoiceHead;
	}

	public void setAskInvoiceHead(AskInvoiceHead askInvoiceHead) {
		this.askInvoiceHead = askInvoiceHead;
	}

	// uni-directional many-to-one association to AskProduct
	@ManyToOne
	@JoinColumn(name = "id_product", nullable = false)
	public AskProduct getAskProduct() {
		return this.askProduct;
	}

	public void setAskProduct(AskProduct askProduct) {
		this.askProduct = askProduct;
	}

	@Column(nullable = false)
	public Integer getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

}