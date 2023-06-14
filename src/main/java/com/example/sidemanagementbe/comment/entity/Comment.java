package com.example.sidemanagementbe.comment.entity;


import com.example.sidemanagementbe.common.entity.BaseEntity;
import com.example.sidemanagementbe.login.entity.Member;
import com.example.sidemanagementbe.post.entity.Post;
import com.example.sidemanagementbe.post.entity.value.Content;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "saida_comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@PrimaryKeyJoinColumn(name = "comment_id")
public class Comment extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member commenter;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    private Content content;

}
