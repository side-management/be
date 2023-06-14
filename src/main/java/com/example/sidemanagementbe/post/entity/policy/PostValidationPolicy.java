package com.example.sidemanagementbe.post.entity.policy;


import com.example.sidemanagementbe.post.entity.Post;

/**
 * 게시글 생성, 수정 정책
 */
public class PostValidationPolicy {

    /**
     * @// TODO: 2023/06/11
     * <pre>
     * 1. 작성자가 존재해야 함
     * 2. 제목이 필수적으로 들어감
     * 3. 내용이 필수적으로 들어감
     * 4. 제목 혹은 내용의 길이가 적절한 범위에 위치해야함
     * 5. 게시글의 카테고리가 설정되어야 함
     * </pre>
     */
    public static void validate(Post post) {
        // 검증 목록
    }

}
