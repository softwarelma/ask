package com.alten.ask.model.orm;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * The persistent class for the ask_code_head database table.
 * 
 */
@Entity
@Table(name = "ask_code_head")
@NamedQuery(name = "AskCodeHead.findAll", query = "SELECT a FROM AskCodeHead a")
public class AskCodeHead implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Timestamp creationTime;
	private String description;
	private Timestamp modificationTime;
	private List<AskCodeDetail> askCodeDetails;

	public AskCodeHead() {
	}

	public AskCodeHead(Timestamp creationTime, String description,
			Timestamp modificationTime, List<AskCodeDetail> askCodeDetails) {
		super();
		this.creationTime = creationTime;
		this.description = description;
		this.modificationTime = modificationTime;
		this.askCodeDetails = askCodeDetails;
	}

	@Override
	public String toString() {
		return "AskCodeHead [id=" + id + ", creationTime=" + creationTime
				+ ", description=" + description + ", modificationTime="
				+ modificationTime + ", askCodeDetails=" + askCodeDetails + "]";
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
		AskCodeHead other = (AskCodeHead) obj;
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

	// bi-directional many-to-one association to AskCodeDetail
	@OneToMany(mappedBy = "askCodeHead", fetch = FetchType.EAGER)
	public List<AskCodeDetail> getAskCodeDetails() {
		return this.askCodeDetails;
	}

	public void setAskCodeDetails(List<AskCodeDetail> askCodeDetails) {
		this.askCodeDetails = askCodeDetails;
	}

	public AskCodeDetail addAskCodeDetail(AskCodeDetail askCodeDetail) {
		getAskCodeDetails().add(askCodeDetail);
		askCodeDetail.setAskCodeHead(this);

		return askCodeDetail;
	}

	public AskCodeDetail removeAskCodeDetail(AskCodeDetail askCodeDetail) {
		getAskCodeDetails().remove(askCodeDetail);
		askCodeDetail.setAskCodeHead(null);

		return askCodeDetail;
	}

}