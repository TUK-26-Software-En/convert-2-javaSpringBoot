# Database Specification

## 저장소 개요

- 운영 DB: PostgreSQL
- 로컬/테스트 DB: H2
- 스키마 생성 방식: Flyway `V1__init_schema.sql`

## 테이블 구성

### books

| 컬럼 | 타입 | 설명 |
|---|---|---|
| id | bigint | PK |
| title | varchar(255) | 도서명 |
| author | varchar(255) | 저자 |
| isbn | varchar(32) | 유니크 식별자 |
| publisher | varchar(255) | 출판사 |
| published_date | date | 출판일 |
| total_quantity | integer | 총 수량 |
| available_quantity | integer | 현재 대출 가능 수량 |
| status | varchar(32) | `AVAILABLE` / `UNAVAILABLE` |
| created_at | timestamp | 생성 시각 |
| updated_at | timestamp | 수정 시각 |

### members

| 컬럼 | 타입 | 설명 |
|---|---|---|
| id | bigint | PK |
| name | varchar(255) | 회원명 |
| email | varchar(255) | 유니크 이메일 |
| phone_number | varchar(64) | 연락처 |
| status | varchar(32) | `ACTIVE` / `INACTIVE` |
| created_at | timestamp | 생성 시각 |
| updated_at | timestamp | 수정 시각 |

### loans

| 컬럼 | 타입 | 설명 |
|---|---|---|
| id | bigint | PK |
| book_id | bigint | FK -> books.id |
| member_id | bigint | FK -> members.id |
| loaned_at | timestamp | 대출 시각 |
| due_date | date | 반납 예정일 |
| returned_at | timestamp | 반납 시각 |
| status | varchar(32) | `ACTIVE` / `RETURNED` |

## 관계

- `books 1 --- N loans`
- `members 1 --- N loans`

## 정합성 규칙

- `isbn`은 유일해야 한다.
- `email`은 유일해야 한다.
- `loans.book_id`는 유효한 `books.id`를 참조해야 한다.
- `loans.member_id`는 유효한 `members.id`를 참조해야 한다.

## 더미 데이터 정책

- 파일: `lib_checkout/src/main/resources/db/seed/demo-data-postgres.sql`
- 대상 프로필: `docker-pg`
- 실행 조건:
  - `books` 비어 있음
  - `members` 비어 있음
  - `loans` 비어 있음
- 실행 효과:
  - 샘플 책/회원/대출 삽입
  - `books.available_quantity`, `books.status` 재계산

## 관련 다이어그램

- `../diagrams/as-is/03-library-erd.puml`
