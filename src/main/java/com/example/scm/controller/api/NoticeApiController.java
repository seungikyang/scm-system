package com.example.scm.controller.api;
import com.example.scm.common.auth.*;
import com.example.scm.common.response.PageResponse;
import com.example.scm.dto.notice.*;
import com.example.scm.service.NoticeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
/**
 * 다른 프로그램이 URL로 요청하는 공지 관련 작업의 접수 창구입니다.
 * 요청을 Service 담당자에게 전달하고 결과를 JSON 자료로 보냅니다. API는 프로그램끼리 요청과 결과를 주고받는 통로입니다.
 */
@RestController @RequestMapping("/api/notices") @RequiredArgsConstructor
public class NoticeApiController {
    private final NoticeService notices;
    @PostMapping
    public ResponseEntity<NoticeResponse> create(@Valid @RequestBody NoticeRequest request, @CurrentUser LoginUser user) {
        return ResponseEntity.status(201).body(notices.create(request, user));
    }
    @GetMapping
    public PageResponse<NoticeResponse> list(@RequestParam(required = false) String keyword,
            @PageableDefault(size = 20) Pageable pageable, @CurrentUser LoginUser user) {
        return PageResponse.of(notices.list(keyword, pageable, user));
    }
    @GetMapping("/{noticeId}")
    public NoticeResponse detail(@PathVariable Long noticeId, @CurrentUser LoginUser user) {
        return notices.detail(noticeId, user);
    }
    @PutMapping("/{noticeId}")
    public NoticeResponse update(@PathVariable Long noticeId, @Valid @RequestBody NoticeRequest request, @CurrentUser LoginUser user) {
        return notices.update(noticeId, request, user);
    }
    @DeleteMapping("/{noticeId}")
    public ResponseEntity<Void> delete(@PathVariable Long noticeId, @CurrentUser LoginUser user) {
        notices.delete(noticeId, user); return ResponseEntity.noContent().build();
    }
}
