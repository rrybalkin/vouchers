package com.romansun.hibernate.logic;

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

@Entity
@Table(name="Talons")
public class Talon {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "talons_seq_gen")
    @SequenceGenerator(name = "talons_seq_gen", sequenceName = "sq_talons_id")
	@Column(name="talon_id")
	private Long talon_id;
	@Column(name="count_lunch")
	private int count_lunch;
	@Column(name="count_dinner")
	private int count_dinner;
	@OneToOne(fetch = FetchType.EAGER)
	@JoinTable(name="visitor_talon", joinColumns = @JoinColumn(name="talon_id"), inverseJoinColumns = @JoinColumn(name = "visitor_id"))
	private Visitor visitor;
	
	public Talon() {}
	
	public Long getId() {
		return talon_id;
	}

	public void setId(Long talon_id) {
		this.talon_id = talon_id;
	}
	
	public Visitor getVisitor() {
		return visitor;
	}

	public void setVisitor(Visitor visitor) {
		this.visitor = visitor;
	}

	public int getCount_lunch() {
		return count_lunch;
	}

	public void setCount_lunch(int count_launch) {
		this.count_lunch = count_launch;
	}

	public int getCount_dinner() {
		return count_dinner;
	}

	public void setCount_dinner(int count_dinner) {
		this.count_dinner = count_dinner;
	}
}
