# Progress Log

## 2026-05-17 기준 진행 기록

### 저장소 현황

- 루트에는 `docker-compose.yml`과 `lib_checkout/`가 존재한다.
- 기존 `docker-compose.yml`은 비어 있다.
- 기존 메인 서비스는 오래된 Spring Boot 샘플 구조다.
- 기존 코드에는 `HelloController` 중심의 샘플 API와 H2 기반 설정이 존재한다.

### 지금까지 확정된 사항

- 메인 서비스는 `Spring Boot 3.x + Java 21` 기준으로 재구성한다.
- 빌드 도구는 `Maven`을 유지한다.
- 운영 DB는 `PostgreSQL`을 사용한다.
- 초기 개발과 빠른 검증용으로 `H2` 프로필을 유지한다.
- 전체 환경은 `Docker Compose`를 기준으로 한다.
- 모니터링 서비스는 별도 컨테이너로 분리한다.
- `docker.sock` 접근은 메인 요구사항에 따라 허용한다.
- `docker.sock` 접근은 고위험 기능으로 취급하고 보안 통제를 문서화한다.
- AI-Ops는 기본 기능이 아니라 Optional 기능으로 정의한다.
- AI-Ops는 규칙 기반 RCA와 외부 LLM 연동 가능 구조를 목표로 한다.

### 문서화한 핵심 결정

- 3-Layer와 MVC를 함께 적용한다.
- 기능별 패키지 구조를 사용한다.
- `HTML`, `CSS`, `JS`는 분리한다.
- 서버 중심 비즈니스 로직 원칙을 유지한다.
- 모니터링은 health, logs, incidents, reliability metrics를 포함한다.
- Docker 제어는 allowlist 기반으로 제한한다.

### 다음 구현 순서

- `docx/` 문서를 기준선으로 고정한다.
- 메인 서비스 `pom.xml`과 실행 구조를 업그레이드한다.
- `docker-compose.yml`을 실제 구성으로 채운다.
- 모니터링 애플리케이션 `service_monitor/` 골격과 Compose 서비스 `service-monitor`를 만든다.

### 현재 코드 기준 추가 진행 결과

- `lib_checkout`는 `book`, `member`, `loan` 기능과 서버 렌더링 UI, Flyway 기반 스키마, `H2`/`PostgreSQL` 프로필 분리를 포함하는 구조로 재구성되었다.
- 메인 서비스에는 `db/seed/demo-data-postgres.sql` 기반의 dummy data 시드가 추가되었고, `docker-pg` 실행 시 DB가 비어 있을 때만 자동 주입되도록 정리되었다.
- Home 화면은 전체 책 목록, 대출 현황, 회원별 대출 통계를 함께 보여주는 대시보드 형태로 확장되었다.
- Members 화면은 회원 상태와 회원별 대출 통계를 함께 보여주는 요약 카드형 화면으로 확장되었다.
- 루트 `docker-compose.yml`은 `library-service`, `postgres`, `service-monitor` 3개 서비스를 기준으로 실제 실행 구성을 갖추도록 채워졌다.
- `service_monitor/`는 별도 Spring Boot 애플리케이션으로 추가되었고 Dashboard, health summary API, incident 이력, reliability metrics 기본 골격을 포함한다.
- Docker logs 수집과 Failure Injection 실제 제어 기능은 다음 구현 단계로 남겨둔다.

### 현재 상태 요약

- 계획 수립 단계는 종료되었다.
- 메인 서비스 현대화와 도메인 기본 기능 구축은 반영되었다.
- 다음 단계는 Compose 통합 실행 검증과 모니터링/보안 기능의 후속 확장이다.
