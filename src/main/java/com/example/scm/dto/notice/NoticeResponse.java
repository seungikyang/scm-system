package com.example.scm.dto.notice;
import com.example.scm.domain.Notice;
import java.time.LocalDateTime;
/**
 * 공지의 제목, 내용, 작성자 이름, 조회수 등을 보여 줍니다.
 * from 함수는 저장된 공지와 작성자 이름을 한 결과 양식에 모읍니다.
 */
public record NoticeResponse(Long id, String title, String content, Long writerId, String writerName,
        boolean important, long viewCount, LocalDateTime createdAt, LocalDateTime updatedAt) {
    public static NoticeResponse from(Notice n, String writerName) {
        return new NoticeResponse(n.getId(), n.getTitle(), n.getContent(), n.getWriterId(), writerName,
                n.isImportant(), n.getViewCount(), n.getCreatedAt(), n.getUpdatedAt());
    }
}
