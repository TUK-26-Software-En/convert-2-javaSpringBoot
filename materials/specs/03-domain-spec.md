# Domain Specification

## Book Domain

### 핵심 속성

- ID
- Title
- Author
- ISBN
- Publisher
- Published Date
- Total Quantity
- Available Quantity
- Status

### 현재 구현 기능

- 도서 목록 조회
- 도서 등록
- 대출 가능 도서 필터링

### 현재 비즈니스 규칙

- ISBN은 중복될 수 없다.
- `availableQuantity <= 0` 이거나 `status != AVAILABLE` 이면 대출할 수 없다.
- 반납 시 `availableQuantity`를 1 증가시키고 `status`를 `AVAILABLE`로 바꾼다.

## Member Domain

### 핵심 속성

- ID
- Name
- Email
- Phone Number
- Status

### 현재 구현 기능

- 회원 목록 조회
- 회원 등록
- 활성 회원 조회
- 회원별 대출 통계 조회

### 현재 비즈니스 규칙

- Email은 중복될 수 없다.
- `INACTIVE` 회원은 신규 대출이 불가하다.

## Loan Domain

### 핵심 속성

- ID
- Book ID
- Member ID
- Loaned At
- Due Date
- Returned At
- Status

### 현재 구현 기능

- 대출 목록 조회
- 대출 등록
- 반납 처리
- Home/Members 화면용 연체 집계

### 현재 비즈니스 규칙

- 도서가 대출 가능 상태여야 한다.
- 회원이 활성 상태여야 한다.
- 반납은 `ACTIVE` 대출에만 가능하다.
- 반납 시 대출 상태와 도서 재고가 함께 갱신된다.

## 대시보드 읽기 모델

현재 코드에는 업무 엔티티 외에 화면용 읽기 모델이 별도로 존재한다.

- `HomeDashboardSummary`
- `MemberDashboardView`

이 모델들은 집계/정렬/연체 판정을 포함하는 화면 전용 DTO 역할을 수행한다.

## 관련 다이어그램

- `../diagrams/as-is/03-library-erd.puml`
- `../diagrams/to-be/13-book-usecase.puml`
- `../diagrams/to-be/14-member-usecase.puml`
- `../diagrams/to-be/15-loan-usecase.puml`
