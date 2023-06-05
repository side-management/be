package com.example.sidemanagementbe.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author ccik2
 * @class Comment
 * @date 2023-06-05
 **/
@Entity
@Getter
@NoArgsConstructor
@Table(name = "Comment")
public class Comment {

    @Id
    @GeneratedValue
    @Column(name = "UniqueID",nullable=false)
    private String uniqueId;

    @Column(name = "comment_id")
    private String comment_id;

    @Column(name = "post_id")
    private String post_id;

    @Column(name = "member_id")
    private String member_id;

    @Builder
    public Comment(String uniqueId,String comment_id,String post_id,String member_id){
        this.uniqueId = uniqueId;
        this.comment_id = comment_id;
        this.post_id = post_id;
        this.member_id = member_id;
    }

}
