package org.acme.schooltimetabling.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Lesson {

    @Id
    @GeneratedValue
    private Long id;

    public Long getId() {
        return id;
    }

}
