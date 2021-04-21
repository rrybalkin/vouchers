package com.rrybalkin.reporting.logic;

import org.apache.commons.lang3.StringUtils;

public class InfoVisitor {
	private final String firstName;
	private final String lastName;
	private final String middleName;
	private final String group;
	private final Integer breakfasts;
	private final Integer lunches;
	private final Integer dinners;

	public InfoVisitor(String fio, Integer breakfasts, Integer lunches, Integer dinners) {
		String[] splitFio = fio.split(" ");
		this.lastName = splitFio[0];
		this.firstName = splitFio[1];
		this.middleName = splitFio.length > 3 ? splitFio[3] : "";
		this.group = "";
		this.breakfasts = breakfasts;
		this.lunches = lunches;
		this.dinners = dinners;
	}

	public InfoVisitor(String firstName, String lastName, String middleName, String group, Integer breakfasts, Integer lunches, Integer dinners) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.middleName = middleName;
		this.group = group;
		this.breakfasts = breakfasts;
		this.lunches = lunches;
		this.dinners = dinners;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getMiddleName() {
		return middleName != null ? middleName : "";
	}

	public String getGroup() {
		return group;
	}

	public Integer getBreakfasts() {
		return breakfasts;
	}

	public Integer getLunches() {
		return lunches;
	}

	public Integer getDinners() {
		return dinners;
	}

	public String getFIO() {
		return getLastName() + " " + getFirstName() + " " + getMiddleName();
	}

	public boolean validate() {
		return StringUtils.isNotEmpty(firstName)
				&& StringUtils.isNotEmpty(lastName)
				&& breakfasts != null
				&& lunches != null
				&& dinners != null;
	}
}