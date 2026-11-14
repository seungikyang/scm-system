package com.example.scm.repository;
import com.example.scm.domain.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
/**
 * 공지 자료를 데이터베이스에서 찾거나 저장하는 창구입니다.
 * 데이터베이스는 자료를 모아 두는 전자 장부입니다. Spring이 함수 이름이나 조회식을 보고 실제 작업을 준비합니다.
 */
public interface NoticeRepository extends JpaRepository<Notice, Long> {
    // 검색어가 빈 문자열이면 전체 목록을 돌려줍니다. LikePatterns.normalizedKeyword가 null을 ''로 바꿔 주므로 여기서는 ''만 확인합니다.
    // lower로 제목을 소문자로 맞춰 대소문자 구분 없이 찾고, escape '!'는 검색어 속 %·_를 특수 기호가 아닌 글자로 다루게 합니다.
    @Query("select n from Notice n where :keyword = '' or lower(n.title) like concat('%', :keyword, '%') escape '!'")
    Page<Notice> search(@Param("keyword") String keyword, Pageable pageable);
    // select는 찾기, update는 고치기입니다. 조회수는 찾고 다시 쓰는 대신 장부에서 바로 1 더합니다.
    // flush는 대기 중인 변경을 먼저 반영하고, clear는 메모리의 옛 자료를 비워 새 조회수를 읽게 합니다.
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Notice n set n.viewCount = n.viewCount + 1 where n.id = :id")
    int incrementViews(@Param("id") Long id);
}
