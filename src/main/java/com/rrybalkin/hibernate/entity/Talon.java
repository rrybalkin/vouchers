package com.rrybalkin.hibernate.entity;

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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="talon_id")
	private Long talonId;

	@Column(name="count_breakfast")
	private int breakfasts;
	
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

	public int getBreakfasts() {
		return breakfasts;
	}

	public void setBreakfasts(int breakfasts) {
		this.breakfasts = breakfasts;
	}

	public int getLunches() {
		return lunches;
	}

	public void setLunches(int lunches) {
		this.lunches = lunches;
	}

	public int getDinners() {
		return dinners;
	}

	public void setDinners(int dinners) {
		this.dinners = dinners;
	}
}
