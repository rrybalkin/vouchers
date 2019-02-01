package com.rrybalkin.hibernate.entity;

import com.rrybalkin.utils.Messages;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Associations")
public class Association implements Comparable<Association> {
    public static final String NO_GROUP = Messages.get("group.no-group");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "association_id")
    private Long id;

    private String name;

    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Association a) {
        return this.id.compareTo(a.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Association that = (Association) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
