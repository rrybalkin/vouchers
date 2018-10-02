package com.romansun.hibernate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "Associations")
public class Association implements Comparable<Association> {
    public static final String NO_GROUP = "No group";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "visitors_seq_gen")
    @SequenceGenerator(name = "visitors_seq_gen", sequenceName = "sq_association_id")
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
}
