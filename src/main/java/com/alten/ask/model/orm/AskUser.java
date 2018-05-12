package com.alten.ask.model.orm;

import java.io.Serializable;

import javax.persistence.*;

import java.sql.Timestamp;
import java.util.List;

/**
 * The persistent class for the ask_user database table.
 * 
 */
@Entity
@Table(name = "ask_user")
@NamedQuery(name = "AskUser.findAll", query = "SELECT a FROM AskUser a")
public class AskUser implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Timestamp creationTime;
	private String description;
	private String email;
	private Timestamp modificationTime;
	private String name;
	private String password;
	private String surname;
	private String username;
	private AskCodeDetail askCodeDetail;// the role
	private List<AskUserProp> askUserProps;

	public AskUser() {
	}

	public AskUser(Timestamp creationTime, String description, String email,
			Timestamp modificationTime, String name, String password,
			String surname, String username, AskCodeDetail askCodeDetail,
			List<AskUserProp> askUserProps) {
		super();
		this.creationTime = creationTime;
		this.description = description;
		this.email = email;
		this.modificationTime = modificationTime;
		this.name = name;
		this.password = password;
		this.surname = surname;
		this.username = username;
		this.askCodeDetail = askCodeDetail;
		this.askUserProps = askUserProps;
	}

	@Override
	public String toString() {
		return "AskUser [id=" + id + ", creationTime=" + creationTime
				+ ", description=" + description + ", email=" + email
				+ ", modificationTime=" + modificationTime + ", name=" + name
				+ ", password=" + password + ", surname=" + surname
				+ ", username=" + username + ", askCodeDetail=" + askCodeDetail
				+ ", askUserProps=" + askUserProps + "]";
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
		AskUser other = (AskUser) obj;
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

	@Column(nullable = false, length = 90)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "modification_time", nullable = false)
	public Timestamp getModificationTime() {
		return this.modificationTime;
	}

	public void setModificationTime(Timestamp modificationTime) {
		this.modificationTime = modificationTime;
	}

	@Column(nullable = false, length = 30)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(nullable = false, length = 30)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(nullable = false, length = 30)
	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	@Column(nullable = false, length = 30)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	// uni-directional many-to-one association to AskCodeDetail
	@ManyToOne
	@JoinColumn(name = "id_code_role", nullable = false)
	public AskCodeDetail getAskCodeDetail() {
		return this.askCodeDetail;
	}

	public void setAskCodeDetail(AskCodeDetail askCodeDetail) {
		this.askCodeDetail = askCodeDetail;
	}

	// bi-directional many-to-one association to AskUserProp
	@OneToMany(mappedBy = "askUser", fetch = FetchType.EAGER)
	public List<AskUserProp> getAskUserProps() {
		return this.askUserProps;
	}

	public void setAskUserProps(List<AskUserProp> askUserProps) {
		this.askUserProps = askUserProps;
	}

	public AskUserProp addAskUserProp(AskUserProp askUserProp) {
		getAskUserProps().add(askUserProp);
		askUserProp.setAskUser(this);

		return askUserProp;
	}

	public AskUserProp removeAskUserProp(AskUserProp askUserProp) {
		getAskUserProps().remove(askUserProp);
		askUserProp.setAskUser(null);

		return askUserProp;
	}

}