package com.example.scm.domain;
import com.example.scm.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
/**
 * 공지 글 한 개의 제목, 내용, 작성자, 중요 표시와 조회수를 저장합니다.
 * 조회수는 글을 읽은 횟수입니다. 내용을 고치는 작업과 조회수 증가가 서로 덮어쓰지 않게 처리합니다.
 */
// 실제로 바뀐 칸만 저장합니다. 공지 내용을 고칠 때 다른 조회가 늘린 조회수까지 덮어쓰지 않습니다.
@org.hibernate.annotations.DynamicUpdate
@Entity @Getter @Table(name = "notices")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 200)
    private String title;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    @Column(name = "writer_id", nullable = false)
    private Long writerId;
    @Column(nullable = false)
    private boolean important;
    @Column(name = "view_count", nullable = false)
    private long viewCount;
    public Notice(String title, String content, Long writerId, boolean important) {
        this.writerId = writerId; update(title, content, important);
    }
    public void update(String title, String content, boolean important) {
        this.title = title; this.content = content; this.important = important;
    }
}
