package com.example.scm.service;
import com.example.scm.common.auth.*;
import com.example.scm.common.exception.*;
import com.example.scm.common.validation.PageableValidator;
import com.example.scm.domain.Notice;
import com.example.scm.domain.User;
import com.example.scm.domain.enums.UserRole;
import com.example.scm.dto.notice.*;
import com.example.scm.repository.*;
import com.example.scm.repository.spec.LikePatterns;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * 게시판의 공지 글을 등록하고 찾아 보여 줍니다.
 * 글 변경은 관리자만 할 수 있고, 목록은 중요 공지를 앞에 두고 같은 중요도에서는 최신 글부터 보여 줍니다.
 */
@Service @RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository notices;
    private final UserRepository users;
    private final Validator validator;
    // 공지 등록·수정·삭제는 여러 화면에서 요청해도 여기서 관리자 여부를 확인합니다.
    @Transactional
    public NoticeResponse create(NoticeRequest request, LoginUser user) {
        // 공지는 모든 직원이 보는 글이라, 잘못된 안내가 퍼지지 않도록 관리자(ADMIN)만 쓸 수 있습니다.
        Authz.requireRole(user, UserRole.ADMIN); validate(request);
        // 제목만 trim으로 앞뒤 공백을 지웁니다. 목록에서 " 공지"와 "공지"가 다르게 보이거나 정렬되는 일을 막기 위해서입니다.
        // 본문은 줄바꿈·들여쓰기가 내용의 일부일 수 있어 그대로 저장합니다.
        return response(notices.save(new Notice(request.getTitle().trim(), request.getContent(), user.id(), request.isImportant())));
    }
    @Transactional
    public NoticeResponse update(Long id, NoticeRequest request, LoginUser user) {
        // 권한과 입력값을 먼저 확인한 다음 기존 글을 고칩니다. 성공하면 JPA가 바뀐 내용을 저장합니다.
        Authz.requireRole(user, UserRole.ADMIN); validate(request);
        Notice notice = entity(id); notice.update(request.getTitle().trim(), request.getContent(), request.isImportant());
        return response(notice);
    }
    @Transactional
    public void delete(Long id, LoginUser user) {
        Authz.requireRole(user, UserRole.ADMIN); notices.delete(entity(id));
    }
    // 상세 보기는 조회수를 1 올리는 "쓰기" 작업이 섞여 있어 readOnly가 아닌 일반 @Transactional을 씁니다.
    @Transactional
    public NoticeResponse detail(Long id, LoginUser user) {
        Authz.requireLogin(user);
        // 데이터베이스에서 바로 1을 더해, 두 사람이 동시에 읽어도 조회수가 한 번만 늘어나는 일을 막습니다.
        // 수정된 글이 0개라면 해당 번호의 공지가 없다는 뜻입니다.
        if (notices.incrementViews(id) == 0) throw new BusinessException(ErrorCode.NOTICE_NOT_FOUND);
        return response(entity(id));
    }
    @Transactional(readOnly = true)
    // 수정 화면을 여는 것은 글을 읽는 조회와 구분해 조회수를 올리지 않습니다.
    public NoticeResponse editDetail(Long id, LoginUser user) {
        Authz.requireRole(user, UserRole.ADMIN); return response(entity(id));
    }
    @Transactional(readOnly = true)
    public Page<NoticeResponse> list(String keyword, Pageable pageable, LoginUser user) {
        Authz.requireLogin(user);
        PageableValidator.validateOffset(pageable);
        // 정렬 순서: 중요 공지 먼저 → 작성 시각이 최신인 글 → 시각도 같으면 번호가 큰 글.
        // 사용자가 보낸 정렬 조건은 일부러 무시하고 이 순서로 고정합니다. 중요 공지가 항상 맨 위에 있어야 하고,
        // 마지막에 id까지 넣어야 같은 시각의 글이 페이지를 넘길 때 중복되거나 빠지지 않습니다.
        Pageable sorted = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(Sort.Order.desc("important"), Sort.Order.desc("createdAt"), Sort.Order.desc("id")));
        // 제목 검색은 대소문자를 무시하고, 검색어 속 %·_는 글자 그대로 찾는다.
        return notices.search(LikePatterns.normalizedKeyword(keyword), sorted).map(this::response);
    }
    private void validate(NoticeRequest request) {
        if (request == null || !validator.validate(request).isEmpty()) throw new BusinessException(ErrorCode.INVALID_INPUT);
    }
    private Notice entity(Long id) {
        return notices.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.NOTICE_NOT_FOUND));
    }
    private NoticeResponse response(Notice n) {
        // 글에는 작성자 번호만 있으므로, 보여 줄 이름을 찾아 결과 양식에 붙입니다.
        return NoticeResponse.from(n, users.findById(n.getWriterId()).map(User::getName).orElse("알 수 없음"));
    }
}
