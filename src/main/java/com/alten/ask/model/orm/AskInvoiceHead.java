package com.alten.ask.model.orm;

import java.io.Serializable;

import javax.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * The persistent class for the ask_invoice_head database table.
 * 
 */
@Entity
@Table(name = "ask_invoice_head")
@NamedQuery(name = "AskInvoiceHead.findAll", query = "SELECT a FROM AskInvoiceHead a")
public class AskInvoiceHead implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Boolean active;
	private Timestamp creationTime;
	private String description;
	private Timestamp modificationTime;
	private BigDecimal price;
	private Timestamp purchasingTime;
	private BigDecimal tax;
	private List<AskInvoiceDetail> askInvoiceDetails;
	private AskUser askUser;

	public AskInvoiceHead() {
	}

	public AskInvoiceHead(Boolean active, Timestamp creationTime, String description, Timestamp modificationTime,
			BigDecimal price, Timestamp purchasingTime, BigDecimal tax, List<AskInvoiceDetail> askInvoiceDetails,
			AskUser askUser) {
		super();
		this.active = active;
		this.creationTime = creationTime;
		this.description = description;
		this.modificationTime = modificationTime;
		this.price = price == null ? null : price.setScale(2);
		this.purchasingTime = purchasingTime;
		this.tax = tax == null ? null : tax.setScale(2);
		this.askInvoiceDetails = askInvoiceDetails;
		this.askUser = askUser;
	}

	@Override
	public String toString() {
		return "AskInvoiceHead [id=" + id + ", active=" + active + ", creationTime=" + creationTime + ", description="
				+ description + ", modificationTime=" + modificationTime + ", price=" + price + ", purchasingTime="
				+ purchasingTime + ", tax=" + tax + ", askInvoiceDetails=" + askInvoiceDetails + ", askUser=" + askUser
				+ "]";
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
		AskInvoiceHead other = (AskInvoiceHead) obj;
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

	@Column(nullable = false)
	public Boolean getActive() {
		return this.active;
	}

	public void setActive(Boolean active) {
		this.active = active;
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

	@Column(name = "purchasing_time")
	public Timestamp getPurchasingTime() {
		return this.purchasingTime;
	}

	public void setPurchasingTime(Timestamp purchasingTime) {
		this.purchasingTime = purchasingTime;
	}

	@Column(nullable = false, precision = 14, scale = 4)
	public BigDecimal getTax() {
		return this.tax;
	}

	public void setTax(BigDecimal tax) {
		this.tax = tax == null ? null : tax.setScale(2);
	}

	// bi-directional many-to-one association to AskInvoiceDetail
	@OneToMany(mappedBy = "askInvoiceHead", fetch = FetchType.EAGER)
	public List<AskInvoiceDetail> getAskInvoiceDetails() {
		return this.askInvoiceDetails;
	}

	public void setAskInvoiceDetails(List<AskInvoiceDetail> askInvoiceDetails) {
		this.askInvoiceDetails = askInvoiceDetails;
	}

	public AskInvoiceDetail addAskInvoiceDetail(AskInvoiceDetail askInvoiceDetail) {
		getAskInvoiceDetails().add(askInvoiceDetail);
		askInvoiceDetail.setAskInvoiceHead(this);

		return askInvoiceDetail;
	}

	public AskInvoiceDetail removeAskInvoiceDetail(AskInvoiceDetail askInvoiceDetail) {
		getAskInvoiceDetails().remove(askInvoiceDetail);
		askInvoiceDetail.setAskInvoiceHead(null);

		return askInvoiceDetail;
	}

	// uni-directional many-to-one association to AskUser
	@ManyToOne
	@JoinColumn(name = "id_user", nullable = false)
	public AskUser getAskUser() {
		return this.askUser;
	}

	public void setAskUser(AskUser askUser) {
		this.askUser = askUser;
	}

}