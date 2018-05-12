package com.alten.ask.model.orm;

import java.io.Serializable;

import javax.persistence.*;

import java.sql.Timestamp;

/**
 * The persistent class for the ask_tx_ax_detail database table.
 * 
 */
@Entity
@Table(name = "ask_tx_ax_detail")
@NamedQuery(name = "AskTxAxDetail.findAll", query = "SELECT a FROM AskTxAxDetail a")
public class AskTxAxDetail implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Timestamp creationTime;
	private String description;
	private String entityAsString;
	private Long idEntity;
	private Timestamp modificationTime;
	private AskCodeDetail askCodeDetail1;
	private AskCodeDetail askCodeDetail2;
	private AskTxHead askTxHead;

	public AskTxAxDetail() {
	}

	public AskTxAxDetail(Timestamp creationTime, String description, String entityAsString, Long idEntity,
			Timestamp modificationTime, AskCodeDetail askCodeDetail1, AskCodeDetail askCodeDetail2, AskTxHead askTxHead) {
		super();
		this.creationTime = creationTime;
		this.description = description;
		this.entityAsString = entityAsString;
		this.idEntity = idEntity;
		this.modificationTime = modificationTime;
		this.askCodeDetail1 = askCodeDetail1;
		this.askCodeDetail2 = askCodeDetail2;
		this.askTxHead = askTxHead;
	}

	@Override
	public String toString() {
		return "AskTxAxDetail [id=" + id + ", creationTime=" + creationTime + ", description=" + description
				+ ", entityAsString=" + entityAsString + ", idEntity=" + idEntity + ", modificationTime="
				+ modificationTime + ", askCodeDetail1=" + askCodeDetail1 + ", askCodeDetail2=" + askCodeDetail2
				+ ", askTxHead=" + askTxHead + "]";
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
		AskTxAxDetail other = (AskTxAxDetail) obj;
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

	@Column(nullable = false, length = 1024)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "entity_as_string", nullable = false, length = 1024)
	public String getEntityAsString() {
		return this.entityAsString;
	}

	public void setEntityAsString(String entityAsString) {
		this.entityAsString = entityAsString;
	}

	@Column(name = "id_entity", nullable = false)
	public Long getIdEntity() {
		return this.idEntity;
	}

	public void setIdEntity(Long idEntity) {
		this.idEntity = idEntity;
	}

	@Column(name = "modification_time", nullable = false)
	public Timestamp getModificationTime() {
		return this.modificationTime;
	}

	public void setModificationTime(Timestamp modificationTime) {
		this.modificationTime = modificationTime;
	}

	// uni-directional many-to-one association to AskCodeDetail
	@ManyToOne
	@JoinColumn(name = "id_code_ax", nullable = false)
	public AskCodeDetail getAskCodeDetail1() {
		return this.askCodeDetail1;
	}

	public void setAskCodeDetail1(AskCodeDetail askCodeDetail1) {
		this.askCodeDetail1 = askCodeDetail1;
	}

	// uni-directional many-to-one association to AskCodeDetail
	@ManyToOne
	@JoinColumn(name = "id_code_entity", nullable = true)
	public AskCodeDetail getAskCodeDetail2() {
		return this.askCodeDetail2;
	}

	public void setAskCodeDetail2(AskCodeDetail askCodeDetail2) {
		this.askCodeDetail2 = askCodeDetail2;
	}

	// bi-directional many-to-one association to AskTxHead
	@ManyToOne
	@JoinColumn(name = "id_tx_head", nullable = false)
	public AskTxHead getAskTxHead() {
		return this.askTxHead;
	}

	public void setAskTxHead(AskTxHead askTxHead) {
		this.askTxHead = askTxHead;
	}

}