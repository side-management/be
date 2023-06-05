package com.example.sidemanagementbe.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author ccik2
 * @class Post
 * @date 2023-06-05
 **/
@Entity
@Getter
@NoArgsConstructor
@Table(name = "Post")
public class Post {

    @Id
    @GeneratedValue
    @Column(name = "post_id",nullable=false)
    private String post_id;

    @Column(name = "title")
    private String title;

    @Column(name = "member_id")
    private String member_id;

    @Column(name = "category")
    private String category;

    @Column(name = "created_at")
    private String created_at;

    @Column(name = "updated_at")
    private String updated_at;

    @Builder
    public Post(String post_id, String title, String member_id, String category, String created_at, String updated_at) {
        this.post_id = post_id;
        this.title = title;
        this.member_id = member_id;
        this.category = category;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
}
