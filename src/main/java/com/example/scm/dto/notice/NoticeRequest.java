package com.example.scm.dto.notice;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
/**
 * 공지 글을 쓰거나 고칠 때 제목, 내용, 중요 표시를 받습니다.
 * 입력한 글이 비어 있거나 너무 길면 저장 전에 알려 줍니다.
 */
@Getter @Setter
public class NoticeRequest {
    @NotBlank(message = "제목은 필수입니다.") @Size(max = 200)
    private String title;
    // utf8mb4 TEXT의 최대 바이트 수 내에서 보관 가능한 길이로 제한한다.
    @NotBlank(message = "내용은 필수입니다.") @Size(max = 10000)
    private String content;
    private boolean important;
}
