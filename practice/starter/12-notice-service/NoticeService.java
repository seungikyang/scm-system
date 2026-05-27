// 실제 구현 위치 예: src/main/java/com/example/scm/service/NoticeService.java
// 목표: 공지사항 CRUD + important 정렬 + 조회수 증가 정책을 채우세요.
//       TRD 3.8.4 참고.

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;

    // ============ 등록 (ADMIN) ============
    @Transactional
    public NoticeResponse create(Long currentUserId, NoticeCreateRequest request) {
        // TODO 01: ADMIN 권한 확인.
        User writer = userRepository.findById(currentUserId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        if (writer.getRole() != UserRole.____) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        Notice notice = Notice.create(
            writer.getId(),
            request.getTitle(),
            request.getContent(),
            request.getImportant()
        );

        return NoticeResponse.from(noticeRepository.save(notice));
    }

    // ============ 목록 ============
    @Transactional(readOnly = true)
    public Page<NoticeResponse> list(Pageable pageable) {
        // TODO 02: 중요 공지 우선 + 최신순 정렬을 추가하세요.
        Sort sort = Sort.by(Sort.Direction.____, "important")
                        .and(Sort.by(Sort.Direction.____, "createdAt"));
        Pageable sortedPageable = PageRequest.of(
            pageable.getPageNumber(), pageable.getPageSize(), sort
        );
        return noticeRepository.findAll(sortedPageable).map(NoticeResponse::from);
    }

    // ============ 상세 (조회수 증가) ============
    @Transactional
    public NoticeResponse get(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
            .orElseThrow(() -> new BusinessException(ErrorCode.____));

        // TODO 03: 조회수를 증가시키는 두 가지 방법 중 어떤 걸 선택할지 적어 보세요.
        //   (a) 도메인 메서드 increaseViewCount() → race 가능
        //   (b) Repository @Modifying @Query 원자 UPDATE
        //   (c) 별도 PATCH /view 엔드포인트
        // 선택: ____
        notice.increaseViewCount();

        return NoticeResponse.from(notice);
    }

    // ============ 수정 / 삭제 ============
    @Transactional
    public NoticeResponse update(Long currentUserId, Long noticeId, NoticeUpdateRequest request) {
        User writer = userRepository.findById(currentUserId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        if (writer.getRole() != UserRole.ADMIN) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        Notice notice = noticeRepository.findById(noticeId)
            .orElseThrow(() -> new BusinessException(ErrorCode.NOTICE_NOT_FOUND));

        // TODO 04: 도메인 메서드 호출.
        notice.____(request.getTitle(), request.getContent(), request.getImportant());

        return NoticeResponse.from(notice);
    }

    @Transactional
    public void delete(Long currentUserId, Long noticeId) {
        User user = userRepository.findById(currentUserId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        if (user.getRole() != UserRole.ADMIN) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        // TODO 05: 존재 여부를 먼저 확인할지, 바로 deleteById 를 호출할지 결정.
        if (!noticeRepository.existsById(noticeId)) {
            throw new BusinessException(ErrorCode.____);
        }
        noticeRepository.deleteById(noticeId);
    }
}

// 학습 질문:
// Q1. 권한 검증을 Service 에서 한 이유는?
//     A:
// Q2. important DESC 정렬에서 createdAt 까지 같이 정렬해야 결과가 안정적인 이유는?
//     A:
// Q3. 조회수 증가가 GET 안에 있을 때의 단점은?
//     A:
