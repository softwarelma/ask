package com.alten.ask.model.orm;

import java.io.Serializable;

import javax.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * The persistent class for the ask_user_prop database table.
 * 
 */
@Entity
@Table(name = "ask_user_prop")
@NamedQuery(name = "AskUserProp.findAll", query = "SELECT a FROM AskUserProp a")
public class AskUserProp implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Timestamp creationTime;
	private String description;
	private Timestamp modificationTime;
	private Integer position;
	private BigDecimal valueDec;
	private String valueStr;
	private AskCodeDetail askCodeDetail;
	private AskUser askUser;

	public AskUserProp() {
	}

	public AskUserProp(Timestamp creationTime, String description,
			Timestamp modificationTime, Integer position, BigDecimal valueDec,
			String valueStr, AskCodeDetail askCodeDetail, AskUser askUser) {
		super();
		this.creationTime = creationTime;
		this.description = description;
		this.modificationTime = modificationTime;
		this.position = position;
		this.valueDec = valueDec;
		this.valueStr = valueStr;
		this.askCodeDetail = askCodeDetail;
		this.askUser = askUser;
	}

	@Override
	public String toString() {
		return "AskUserProp [id=" + id + ", creationTime=" + creationTime
				+ ", description=" + description + ", modificationTime="
				+ modificationTime + ", position=" + position + ", valueDec="
				+ valueDec + ", valueStr=" + valueStr + ", askCodeDetail="
				+ askCodeDetail + ", askUser=" + askUser + "]";
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
		AskUserProp other = (AskUserProp) obj;
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

	@Column(nullable = false)
	public Integer getPosition() {
		return this.position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	@Column(name = "value_dec", precision = 14, scale = 4)
	public BigDecimal getValueDec() {
		return this.valueDec;
	}

	public void setValueDec(BigDecimal valueDec) {
		this.valueDec = valueDec;
	}

	@Column(name = "value_str", length = 15)
	public String getValueStr() {
		return this.valueStr;
	}

	public void setValueStr(String valueStr) {
		this.valueStr = valueStr;
	}

	// uni-directional many-to-one association to AskCodeDetail
	@ManyToOne
	@JoinColumn(name = "id_code_prop", nullable = false)
	public AskCodeDetail getAskCodeDetail() {
		return this.askCodeDetail;
	}

	public void setAskCodeDetail(AskCodeDetail askCodeDetail) {
		this.askCodeDetail = askCodeDetail;
	}

	// bi-directional many-to-one association to AskUser
	@ManyToOne
	@JoinColumn(name = "id_user", nullable = false)
	public AskUser getAskUser() {
		return this.askUser;
	}

	public void setAskUser(AskUser askUser) {
		this.askUser = askUser;
	}

}