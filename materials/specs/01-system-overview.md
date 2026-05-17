# System Overview

## 목적

- 소규모 도서 대출 시스템을 `Spring Boot + MVC + 3-Layer` 구조로 구현한다.
- 웹 화면을 통해 도서, 회원, 대출을 관리한다.
- Docker 기반 통합 실행에서 PostgreSQL을 운영 저장소로 사용한다.

## 시스템 경계

- 주 시스템: `library-service`
- 저장소: `postgres`
- 보조 시스템: `service-monitor`는 별도 범위지만 `library-service`의 health 상태를 조회한다.

## 현재 구현 범위

- Home 대시보드
- Book 목록/등록
- Member 목록/등록
- Loan 목록/등록/반납
- Members 대시보드형 통계 화면
- Docker 실행 시 빈 PostgreSQL 초기 데이터 자동 삽입

## 주요 기술 스택

| 구분 | 기술 |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.3.x |
| View | Thymeleaf |
| Build | Maven |
| ORM | Spring Data JPA / Hibernate |
| Migration | Flyway |
| DB | PostgreSQL, H2 |
| Runtime | Docker Compose |

## 사용자 관점 기능

| 영역 | 현재 제공 기능 |
|---|---|
| Home | 전체 도서/회원/대출 요약, 책 목록, 대출 현황, 회원별 대출 통계 |
| Books | 도서 목록 조회, 도서 등록 |
| Members | 회원 목록 조회, 회원 등록, 회원별 대출 통계 조회 |
| Loans | 대출 목록 조회, 대출 등록, 반납 처리 |

## 핵심 런타임 관계

- Browser -> `library-service` : HTTP 요청
- `library-service` -> `postgres` : JPA/JDBC 기반 DB 접근
- `library-service` -> Thymeleaf template/static resource : 서버 렌더링 응답 구성

## 관련 다이어그램

- `../diagrams/as-is/01-library-runtime-context.puml`
- `../diagrams/as-is/02-library-package-structure.puml`
- `../diagrams/to-be/11-library-target-context.puml`
