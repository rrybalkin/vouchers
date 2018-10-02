package com.romansun.hibernate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.Objects;
import java.util.Optional;

@Entity
@Table(name="Visitors")
public class Visitor {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "visitors_seq_gen")
    @SequenceGenerator(name = "visitors_seq_gen", sequenceName = "sq_visitor_id")
	@Column(name="visitor_id")
	private Long id;
	
	@Column(name="firstname")
	private String firstName;
	
	@Column(name="lastname")
	private String lastName;
	
	@Column(name="middlename")
	private String middleName;
	
	@Column(name="description")
	private String description;
	
	@OneToOne(fetch=FetchType.EAGER)
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name="association", referencedColumnName="association_id")
	private Association association;
	
	@OneToOne(fetch = FetchType.EAGER)
	@Fetch(FetchMode.JOIN)
	@JoinTable(name="visitor_talon", joinColumns = @JoinColumn(name="visitor_id"), inverseJoinColumns = @JoinColumn(name = "talon_id"))
	private Talon talon;

	public Talon getTalon() {
		return talon;
	}

	public void setTalon(Talon talon) {
		this.talon = talon;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getMiddleName() {
		return middleName != null ? middleName : "";
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public Association getAssociation() {
		return association;
	}
	public void setAssociation(Association association) {
		this.association = association;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public String getFIO() {
		return getLastName() + " " + getFirstName() + " " + getMiddleName();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Visitor) {
			Visitor that = (Visitor) o;
			if (Objects.equals(this.id, that.id)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
    public String toString() {
       return getFIO();
    }
}
