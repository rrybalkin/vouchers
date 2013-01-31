package com.romansun.hibernate.logic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="Visitors")
public class Visitor {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "visitors_seq_gen")
    @SequenceGenerator(name = "visitors_seq_gen", sequenceName = "sq_visitor_id")
	@Column(name="visitor_id")
	private Long id;
	@Column(name="firstname")
	private String firstname;
	@Column(name="lastname")
	private String lastname;
	@Column(name="middlename")
	private String middlename;
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="association", referencedColumnName="association_id")
	private Association association;
	@Column(name="description")
	private String description;
	@OneToOne(fetch = FetchType.EAGER)
	@JoinTable(name="visitor_talon", joinColumns = @JoinColumn(name="visitor_id"), inverseJoinColumns = @JoinColumn(name = "talon_id"))
	private Talon talon;
	
	public Visitor() {}
	
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
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getMiddlename() {
		return middlename;
	}
	public void setMiddlename(String middlename) {
		this.middlename = middlename;
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
	
	@Override
    public String toString() {
       return lastname + " " + firstname + " " + middlename;
    }
}
