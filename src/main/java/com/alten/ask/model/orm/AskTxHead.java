package com.alten.ask.model.orm;

import java.io.Serializable;

import javax.persistence.*;

import java.sql.Timestamp;
import java.util.List;

/**
 * The persistent class for the ask_tx_head database table.
 * 
 */
@Entity
@Table(name = "ask_tx_head")
@NamedQuery(name = "AskTxHead.findAll", query = "SELECT a FROM AskTxHead a")
public class AskTxHead implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Timestamp creationTime;
	private String description;
	private Timestamp modificationTime;
	private List<AskTxAxDetail> askTxAxDetails;
	private AskCodeDetail askCodeDetail;

	public AskTxHead() {
	}

	public AskTxHead(Timestamp creationTime, String description,
			Timestamp modificationTime, List<AskTxAxDetail> askTxAxDetails,
			AskCodeDetail askCodeDetail) {
		super();
		this.creationTime = creationTime;
		this.description = description;
		this.modificationTime = modificationTime;
		this.askTxAxDetails = askTxAxDetails;
		this.askCodeDetail = askCodeDetail;
	}

	@Override
	public String toString() {
		return "AskTxHead [id=" + id + ", creationTime=" + creationTime
				+ ", description=" + description + ", modificationTime="
				+ modificationTime + ", askTxAxDetails=" + askTxAxDetails
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
		AskTxHead other = (AskTxHead) obj;
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

	@Column(name = "modification_time", nullable = false)
	public Timestamp getModificationTime() {
		return this.modificationTime;
	}

	public void setModificationTime(Timestamp modificationTime) {
		this.modificationTime = modificationTime;
	}

	// bi-directional many-to-one association to AskTxAxDetail
	@OneToMany(mappedBy = "askTxHead", fetch = FetchType.EAGER)
	public List<AskTxAxDetail> getAskTxAxDetails() {
		return this.askTxAxDetails;
	}

	public void setAskTxAxDetails(List<AskTxAxDetail> askTxAxDetails) {
		this.askTxAxDetails = askTxAxDetails;
	}

	public AskTxAxDetail addAskTxAxDetail(AskTxAxDetail askTxAxDetail) {
		getAskTxAxDetails().add(askTxAxDetail);
		askTxAxDetail.setAskTxHead(this);

		return askTxAxDetail;
	}

	public AskTxAxDetail removeAskTxAxDetail(AskTxAxDetail askTxAxDetail) {
		getAskTxAxDetails().remove(askTxAxDetail);
		askTxAxDetail.setAskTxHead(null);

		return askTxAxDetail;
	}

	// uni-directional many-to-one association to AskCodeDetail
	@ManyToOne
	@JoinColumn(name = "id_code_tx", nullable = false)
	public AskCodeDetail getAskCodeDetail() {
		return this.askCodeDetail;
	}

	public void setAskCodeDetail(AskCodeDetail askCodeDetail) {
		this.askCodeDetail = askCodeDetail;
	}

}