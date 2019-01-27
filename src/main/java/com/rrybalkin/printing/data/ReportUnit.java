package com.rrybalkin.printing.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

public class ReportUnit {
    @Alias(name = "firstname")
    String firstName;
    @Alias(name = "lastname")
    String lastName;
    @Alias(name = "middlename")
    String middleName;
    @Alias(name = "group")
    String group;
    @Alias(name = "lunches")
    int lunches;
    @Alias(name = "dinners")
    int dinners;

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

    /**
     * Method for getting property of report unit by property name
     *
     * @param pName property name
     * @return value of property or null if property with this name doesn't exist
     */
    public Object getPropertyByName(String pName) throws IllegalArgumentException, IllegalAccessException {
        for (Field field : this.getClass().getDeclaredFields()) {
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
