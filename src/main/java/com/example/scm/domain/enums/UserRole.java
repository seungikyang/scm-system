package com.example.scm.domain.enums;

/**
 * 사용자 권한. 기능·화면 노출과 승인 권한을 가른다.
 * 학습 모듈 01에서는 DB에 문자열로 저장되는 enum이라는 점을 먼저 확인한다.
 * 실제 권한 검사는 모듈 20과 23에서 이어서 학습한다.
 * - USER    : 발주 작성/내 발주 조회 등 일반 기능
 * - MANAGER : 승인·반려·입고 가능 (관리자발주 메뉴)
 * - ADMIN   : MANAGER 기능 + 거래처·카테고리·품목 마스터 데이터 관리
 */
public enum UserRole {
    USER,
    ADMIN,
    MANAGER
}
