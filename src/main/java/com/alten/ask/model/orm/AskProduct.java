package com.alten.ask.model.orm;

import java.io.Serializable;

import javax.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * The persistent class for the ask_product database table.
 * 
 */
@Entity
@Table(name = "ask_product")
@NamedQuery(name = "AskProduct.findAll", query = "SELECT a FROM AskProduct a")
public class AskProduct implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Timestamp creationTime;
	private String description;
	private String ean;
	private Boolean imported;
	private Timestamp modificationTime;
	private BigDecimal price;
	private AskCodeDetail askCodeDetail;

	public AskProduct() {
	}

	public AskProduct(Timestamp creationTime, String description, String ean, Boolean imported,
			Timestamp modificationTime, BigDecimal price, AskCodeDetail askCodeDetail) {
		super();
		this.creationTime = creationTime;
		this.description = description;
		this.ean = ean;
		this.imported = imported;
		this.modificationTime = modificationTime;
		this.price = price.setScale(2);
		this.askCodeDetail = askCodeDetail;
	}

	@Override
	public String toString() {
		return "AskProduct [id=" + id + ", creationTime=" + creationTime + ", description=" + description + ", ean="
				+ ean + ", imported=" + imported + ", modificationTime=" + modificationTime + ", price=" + price
				+ ", askCodeDetail=" + askCodeDetail + "]";
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
		AskProduct other = (AskProduct) obj;
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
		this.price = price.setScale(2);
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

}