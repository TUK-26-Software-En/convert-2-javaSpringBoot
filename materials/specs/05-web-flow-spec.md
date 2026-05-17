# Web Flow Specification

## 엔드포인트 목록

| Method | Path | 역할 | 템플릿/결과 |
|---|---|---|---|
| GET | `/` | Home 대시보드 조회 | `home/index` |
| GET | `/books` | 도서 목록 조회 | `book/list` |
| GET | `/books/new` | 도서 등록 폼 | `book/form` |
| POST | `/books` | 도서 등록 | redirect `/books` |
| GET | `/members` | 회원 목록/통계 조회 | `member/list` |
| GET | `/members/new` | 회원 등록 폼 | `member/form` |
| POST | `/members` | 회원 등록 | redirect `/members` |
| GET | `/loans` | 대출 목록 조회 | `loan/list` |
| GET | `/loans/new` | 대출 등록 폼 | `loan/form` |
| POST | `/loans` | 대출 등록 | redirect `/loans` |
| POST | `/loans/{loanId}/return` | 반납 처리 | redirect `/loans` |

## Home 화면 흐름

1. Browser가 `GET /` 요청
2. `HomeController`가 `HomeDashboardService.loadSummary()` 호출
3. 서비스가 책/회원/대출 목록을 읽고 집계 DTO 생성
4. Thymeleaf가 `home/index` 렌더링

## Book 등록 흐름

1. Browser가 `GET /books/new` 요청
2. `BookController`가 폼 모델과 상태 enum 준비
3. Browser가 `POST /books` 제출
4. 입력 검증 후 `BookService.createBook()` 호출
5. ISBN 중복 검증 후 `BookEntity` 저장
6. 성공 시 `/books`로 redirect

## Member 등록 흐름

1. Browser가 `GET /members/new` 요청
2. `MemberController`가 상태 enum과 폼 모델 준비
3. Browser가 `POST /members` 제출
4. 입력 검증 후 `MemberService.registerMember()` 호출
5. Email 중복 검증 후 `MemberEntity` 저장
6. 성공 시 `/members`로 redirect

## Loan 등록 흐름

1. Browser가 `GET /loans/new` 요청
2. `LoanController`가 대출 가능 도서와 활성 회원 목록 준비
3. Browser가 `POST /loans` 제출
4. `LoanService.createLoan()` 호출
5. 도서/회원 조회, 상태 검증, 재고 감소, 대출 저장
6. 성공 시 `/loans`로 redirect

## Loan 반납 흐름

1. Browser가 `POST /loans/{loanId}/return` 요청
2. `LoanService.returnLoan()` 호출
3. 대출 상태를 `RETURNED`로 변경
4. 연결된 도서 재고 복구
5. `/loans`로 redirect

## 화면별 데이터 조립 특징

- Home: 집계 중심 화면
- Members: 상태/통계 중심 화면
- Books: 재고/메타데이터 중심 화면
- Loans: 상태 전이 중심 화면

## 관련 다이어그램

- `../diagrams/as-is/04-seq-book-registration.puml`
- `../diagrams/as-is/05-seq-loan-checkout.puml`
- `../diagrams/as-is/06-seq-loan-return.puml`
- `../diagrams/as-is/07-seq-home-dashboard-read.puml`
- `../diagrams/as-is/08-seq-member-dashboard-read.puml`
