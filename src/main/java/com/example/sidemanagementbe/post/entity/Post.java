package com.example.sidemanagementbe.post.entity;


import com.example.sidemanagementbe.comment.entity.Comment;
import com.example.sidemanagementbe.login.entity.Member;
import com.example.sidemanagementbe.post.entity.policy.PostValidationPolicy;
import com.example.sidemanagementbe.post.entity.value.PostInformation;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "saida_posts")
public class Post {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member writer;

    @Embedded
    private PostInformation postInformation;

    @OneToMany
    @JoinColumn(name = "comment_id")
    private List<Comment> comments;

    // 카테고리 추가


    @Builder
    public Post(Member writer, PostInformation postInformation) {
        this.writer = writer;
        this.postInformation = postInformation;

        PostValidationPolicy.validate(this);
    }
}
