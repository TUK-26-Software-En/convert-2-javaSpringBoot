# Applied Design Patterns

## 개요

현재 프로젝트에는 전형적인 GoF 패턴과 Spring 기반 아키텍처 패턴이 함께 적용되어 있다.
여기서는 실제 코드에서 역할이 분명한 패턴만 선정한다.

## 1. MVC Pattern

### 적용 이유

- 요청 처리(`Controller`), 화면(`Thymeleaf template`), 비즈니스 로직(`Service`)가 분리되어 있다.
- 웹 화면이 서버 렌더링 중심으로 구성되고, JS는 보조 역할만 수행한다.

### 적용 위치

- `lib_checkout/src/main/java/org/tukorea/libcheckout/book/presentation/controller/BookController.java`
- `lib_checkout/src/main/java/org/tukorea/libcheckout/member/presentation/controller/MemberController.java`
- `lib_checkout/src/main/java/org/tukorea/libcheckout/loan/presentation/controller/LoanController.java`
- `lib_checkout/src/main/java/org/tukorea/libcheckout/global/presentation/controller/HomeController.java`
- `lib_checkout/src/main/resources/templates/**`

### 코드에서 보이는 특징

- Controller는 Model에 필요한 데이터만 담고 템플릿 이름을 반환한다.
- View는 `home`, `book`, `member`, `loan` 단위 Thymeleaf 템플릿으로 분리되어 있다.
- Service가 화면에서 필요한 집계 데이터를 만들어 Controller에 전달한다.

### 얻는 이점

- 화면 변경이 비즈니스 로직 변경으로 번지지 않는다.
- 웹 요청 흐름을 이해하기 쉽다.
- 서버 렌더링 화면 추가 시 기존 구조를 재사용하기 쉽다.

## 2. Service Layer Pattern

### 적용 이유

- 비즈니스 규칙과 트랜잭션 경계를 Controller 밖으로 분리했다.
- 화면 단위 집계와 도메인 규칙 처리를 서비스가 담당한다.

### 적용 위치

- `lib_checkout/src/main/java/org/tukorea/libcheckout/book/business/service/BookService.java`
- `lib_checkout/src/main/java/org/tukorea/libcheckout/member/business/service/MemberService.java`
- `lib_checkout/src/main/java/org/tukorea/libcheckout/loan/business/service/LoanService.java`
- `lib_checkout/src/main/java/org/tukorea/libcheckout/global/business/service/HomeDashboardService.java`
- `service_monitor/src/main/java/org/tukorea/servicemonitor/dashboard/business/service/DashboardService.java`
- `service_monitor/src/main/java/org/tukorea/servicemonitor/dockercontrol/business/service/DockerControlService.java`

### 코드에서 보이는 특징

- `LoanService.createLoan()`은 도서 조회, 회원 조회, 활성 회원 검증, 재고 감소, 대출 저장을 하나의 트랜잭션 안에서 처리한다.
- `MemberService.loadDashboard()`와 `HomeDashboardService.loadSummary()`는 여러 저장소를 조합해 대시보드 읽기 모델을 만든다.
- `DockerControlService`는 허용 대상/명령 검증과 실행 결과 메시지 생성을 한곳에 모은다.

### 얻는 이점

- Controller가 얇아진다.
- 트랜잭션과 정책이 한 곳에 모여 유지보수가 쉬워진다.
- 화면/입력 방식이 바뀌어도 핵심 비즈니스 로직을 재사용할 수 있다.

## 3. Repository Pattern

### 적용 이유

- 데이터 접근을 비즈니스 로직에서 분리했다.
- JPA 저장소 인터페이스를 통해 조회/저장을 추상화했다.

### 적용 위치

- `lib_checkout/src/main/java/org/tukorea/libcheckout/book/dataaccess/repository/BookRepository.java`
- `lib_checkout/src/main/java/org/tukorea/libcheckout/member/dataaccess/repository/MemberRepository.java`
- `lib_checkout/src/main/java/org/tukorea/libcheckout/loan/dataaccess/repository/LoanRepository.java`
- `service_monitor/src/main/java/org/tukorea/servicemonitor/health/dataaccess/repository/HealthEventRepository.java`
- `service_monitor/src/main/java/org/tukorea/servicemonitor/incidents/dataaccess/repository/IncidentRepository.java`

### 코드에서 보이는 특징

- 서비스는 SQL 문자열을 직접 다루지 않고 저장소 메서드를 호출한다.
- `LoanRepository.findAllWithBookAndMemberOrderByLoanedAtDesc()` 같은 질의가 저장소로 캡슐화되어 있다.
- 엔티티와 저장소가 `dataaccess` 계층에 모여 있다.

### 얻는 이점

- 저장 기술 세부사항이 서비스 계층에 퍼지지 않는다.
- 테스트와 리팩터링 시 영향 범위를 줄일 수 있다.
- 조회 전략을 저장소 내부에서 조정할 수 있다.

## 4. Adapter Pattern

### 적용 이유

- 외부 시스템인 Docker Engine API 접근을 내부 서비스 로직과 분리했다.
- 모니터링 서비스가 Docker와 통신하는 저수준 구현을 별도 어댑터에 숨겼다.

### 적용 위치

- `service_monitor/src/main/java/org/tukorea/servicemonitor/dockercontrol/dataaccess/adapter/DockerEngineAdapter.java`
- `service_monitor/src/main/java/org/tukorea/servicemonitor/dockercontrol/business/service/DockerControlService.java`

### 코드에서 보이는 특징

- `DockerEngineAdapter`는 Docker socket 호출, JSON 파싱, 로그 디코딩, 상태 포맷팅을 담당한다.
- `DockerControlService`는 정책 검사 후 어댑터를 호출한다.
- Controller는 Docker API를 직접 모르고 서비스만 호출한다.

### 얻는 이점

- 외부 API 변경의 영향이 한곳에 모인다.
- 보안 정책 검사와 실제 호출 구현을 분리할 수 있다.
- 향후 Docker SDK 교체가 비교적 쉬워진다.

## 5. DTO / View Model Pattern

### 적용 이유

- 엔티티를 그대로 화면에 노출하지 않고, 요청/응답/화면 전용 모델을 분리했다.
- 대시보드처럼 집계가 필요한 화면을 엔티티와 분리된 읽기 모델로 표현했다.

### 적용 위치

- 요청 DTO
  - `lib_checkout/src/main/java/org/tukorea/libcheckout/book/presentation/dto/BookCreateRequest.java`
  - `lib_checkout/src/main/java/org/tukorea/libcheckout/member/presentation/dto/MemberCreateRequest.java`
  - `lib_checkout/src/main/java/org/tukorea/libcheckout/loan/presentation/dto/LoanCreateRequest.java`
- 요약/조회 모델
  - `lib_checkout/src/main/java/org/tukorea/libcheckout/book/model/BookSummary.java`
  - `lib_checkout/src/main/java/org/tukorea/libcheckout/member/model/MemberSummary.java`
  - `lib_checkout/src/main/java/org/tukorea/libcheckout/loan/model/LoanSummary.java`
- 대시보드 읽기 모델
  - `lib_checkout/src/main/java/org/tukorea/libcheckout/global/model/HomeDashboardSummary.java`
  - `lib_checkout/src/main/java/org/tukorea/libcheckout/member/model/MemberDashboardView.java`
  - `service_monitor/src/main/java/org/tukorea/servicemonitor/dashboard/model/DashboardView.java`

### 코드에서 보이는 특징

- 요청 DTO는 validation annotation과 `toRegistration()` 변환 메서드를 가진다.
- 요약 모델은 엔티티 전체가 아니라 화면에 필요한 값만 담는다.
- 대시보드 모델은 집계/정렬 결과를 표현하는 전용 구조다.

### 얻는 이점

- 엔티티 변경이 화면 모델에 바로 전파되지 않는다.
- 입력 검증 위치가 명확해진다.
- 집계형 화면을 독립적으로 설계할 수 있다.

## 6. Initializer / Bootstrap Pattern

### 적용 이유

- 애플리케이션 시작 시 특정 조건을 검사하고 필요한 초기화 작업을 수행한다.
- Docker 실행 환경에서 초기 DB가 비어 있을 때만 SQL 더미 데이터를 넣는다.

### 적용 위치

- `lib_checkout/src/main/java/org/tukorea/libcheckout/global/business/service/DockerDemoDataSqlInitializer.java`
- `lib_checkout/src/main/resources/db/seed/demo-data-postgres.sql`

### 코드에서 보이는 특징

- `ApplicationRunner` 구현체가 기동 시점에 실행된다.
- `docker-pg` 프로필에서만 활성화된다.
- `books`, `members`, `loans`가 모두 비어 있을 때만 SQL 시드를 수행한다.

### 얻는 이점

- 실행 검증 환경을 빠르게 준비할 수 있다.
- 이미 운영/실습 데이터가 있으면 다시 덮어쓰지 않는다.
- 더미 데이터가 코드가 아니라 SQL로 관리되어 추적과 수정이 쉽다.

## 패턴 적용 요약 표

| 패턴 | 대표 영역 | 대표 파일 |
|---|---|---|
| MVC | `lib_checkout` 웹 화면 | `BookController`, `MemberController`, `LoanController`, `HomeController` |
| Service Layer | 비즈니스 규칙/집계 | `LoanService`, `MemberService`, `HomeDashboardService` |
| Repository | DB 접근 추상화 | `BookRepository`, `MemberRepository`, `LoanRepository` |
| Adapter | 외부 Docker API 연결 | `DockerEngineAdapter` |
| DTO / View Model | 요청/응답/화면 모델 분리 | `BookCreateRequest`, `HomeDashboardSummary`, `MemberDashboardView` |
| Initializer / Bootstrap | 조건부 초기화 | `DockerDemoDataSqlInitializer` |

## 비고

- `service_monitor`에는 `DockerControlCommand`와 같은 command-like 구조가 있지만, 현재는 완전한 Command 객체 계층으로 분리된 형태는 아니므로 정식 Command Pattern으로 분류하지 않았다.
- `@Scheduled` 기반 `ScheduledHealthCollector`는 주기 실행 구조이지만, 이번 문서에서는 보조 실행 메커니즘으로 보고 핵심 패턴 목록에는 포함하지 않았다.
