package com.example.sidemanagementbe.post.entity.value;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Title {

    @Column(name = "title")
    private String value;

}
