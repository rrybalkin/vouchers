package com.romansun.reports.logic;

import org.apache.commons.lang3.StringUtils;

public class InfoVisitor {
	private final String firstName;
	private final String lastName;
	private final String middleName;
	private final String group;
	private final Integer lunches;
	private final Integer dinners;

	public InfoVisitor(String fio, Integer lunches, Integer dinners) {
		String[] splittedFio = fio.split(" ");
		this.firstName = splittedFio[0];
		this.lastName = splittedFio[1];
		this.middleName = splittedFio.length > 2 ? splittedFio[3] : "";
		this.group = "";
		this.lunches = lunches;
		this.dinners = dinners;
	}

	public InfoVisitor(String firstName, String lastName, String middleName, String group, Integer lunches, Integer dinners) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.middleName = middleName;
		this.group = group;
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
				&& StringUtils.isNotEmpty(group)
				&& lunches != null
				&& dinners != null;
	}
}