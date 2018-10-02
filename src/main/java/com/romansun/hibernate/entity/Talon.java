package com.romansun.hibernate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="Talons")
public class Talon {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "talons_seq_gen")
    @SequenceGenerator(name = "talons_seq_gen", sequenceName = "sq_talons_id")
	@Column(name="talon_id")
	private Long talonId;
	
	@Column(name="count_lunch")
	private int lunches;
	
	@Column(name="count_dinner")
	private int dinners;
	
	public Long getId() {
		return talonId;
	}

	public void setId(Long talon_id) {
		this.talonId = talon_id;
	}
	
	public int getLunches() {
		return lunches;
	}

	public void setLunches(int count_launch) {
		this.lunches = count_launch;
	}

	public int getDinners() {
		return dinners;
	}

	public void setDinners(int count_dinner) {
		this.dinners = count_dinner;
	}
}
