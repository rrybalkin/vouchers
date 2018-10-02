package com.romansun.printing.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

public class ReportUnit {
    @Alias(name = "firstname")
    private String firstName;
    @Alias(name = "lastname")
    private String lastName;
    @Alias(name = "middlename")
    private String middleName;
    @Alias(name = "group")
    private String group;
    @Alias(name = "lunches")
    private int lunches;
    @Alias(name = "dinners")
    private int dinners;
    @Alias(name = "lunch price")
    private double lunchPrice;
    @Alias(name = "dinner price")
    private double dinnerPrice;
    @Alias(name = "total cost")
    private double totalCost;

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
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
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

    public double getLunchPrice() {
        return lunchPrice;
    }

    public void setLunchPrice(double lunchPrice) {
        this.lunchPrice = lunchPrice;
    }

    public double getDinnerPrice() {
        return dinnerPrice;
    }

    public void setDinnerPrice(double dinnerPrice) {
        this.dinnerPrice = dinnerPrice;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    /**
     * Method for getting property of report unit by property name
     *
     * @param pName property name
     * @return value of property or null if property with this name doesn't exist
     */
    public Object getPropertyByName(String pName) throws IllegalArgumentException, IllegalAccessException {
        for (Field field : this.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            String alias = field.getAnnotation(Alias.class).name();

            if (alias.equalsIgnoreCase(pName)) {
                return field.get(this);
            }
        }

        return null;
    }

    @Target(value = ElementType.FIELD)
    @Retention(value = RetentionPolicy.RUNTIME)
    public @interface Alias {
        String name();
    }
}
