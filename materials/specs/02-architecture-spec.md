# Architecture Specification

## 아키텍처 스타일

- `Feature-first`
- `MVC`
- `3-Layer Architecture`

## 최상위 패키지

```text
org.tukorea.libcheckout
  global
  book
  member
  loan
```

## 계층 책임

| 계층 | 책임 | 현재 코드 예시 |
|---|---|---|
| presentation | 요청 처리, 입력 검증, Model 구성, 화면 선택 | `BookController`, `MemberController`, `LoanController`, `HomeController` |
| business | 비즈니스 규칙, 트랜잭션 경계, 도메인 조합 | `BookService`, `MemberService`, `LoanService`, `HomeDashboardService` |
| dataaccess | 엔티티 매핑, 저장소 추상화, 질의 수행 | `BookRepository`, `MemberRepository`, `LoanRepository`, `BookEntity`, `MemberEntity`, `LoanEntity` |
| global | 공통 화면/초기화/대시보드 지원 | `HomeDashboardService`, `DockerDemoDataSqlInitializer` |

## 기능별 구조

### Book

- Controller: 목록/등록 요청 처리
- Service: ISBN 중복 검증, 등록, 대출 가능 도서 조회
- Repository: 책 목록 정렬 조회, 대출 가능 재고 조회

### Member

- Controller: 목록/등록 요청 처리
- Service: 이메일 중복 검증, 활성 회원 조회, 회원 대시보드 집계
- Repository: 상태별 정렬 조회

### Loan

- Controller: 목록/등록/반납 요청 처리
- Service: 도서/회원 조회, 활성 회원 검증, 재고 감소/복구, 대출/반납 상태 전이
- Repository: 도서/회원 join fetch 기반 조회

## 트랜잭션 경계

- `BookService.createBook()`
- `MemberService.registerMember()`
- `LoanService.createLoan()`
- `LoanService.returnLoan()`

읽기 전용 대시보드와 목록 조회는 `@Transactional(readOnly = true)`로 유지된다.

## 화면 조립 패턴

- Home 화면은 `HomeDashboardService`가 책/회원/대출 데이터를 집계한다.
- Members 화면은 `MemberService.loadDashboard()`가 회원별 대출 통계를 집계한다.
- Controller는 집계 결과를 그대로 템플릿에 전달한다.

## 초기 데이터 주입

- `DockerDemoDataSqlInitializer`는 `docker-pg` 프로필에서만 동작한다.
- `books`, `members`, `loans`가 모두 비어 있을 때만 SQL 시드를 실행한다.

## 관련 다이어그램

- `../diagrams/as-is/02-library-package-structure.puml`
- `../diagrams/to-be/12-library-layered-architecture.puml`
