package com.example.sidemanagementbe.post.entity.value;


import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

@Embeddable
public class Content {


    @Column(name = "content")
    @Lob
    private String value;
}
