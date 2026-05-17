# Database Strategy

## 기본 방침

- 운영 기준 DB는 `PostgreSQL`이다.
- 빠른 개발과 테스트를 위해 `H2`를 보조 DB로 사용한다.

## 메인 서비스 저장 전략

- `local-h2`: 개발 편의성 확보
- `docker-pg`: Compose 기본 실행 환경
- 스키마 관리는 `Flyway` 도입을 우선 검토한다.

## 모니터링 서비스 저장 전략

- 메인 PostgreSQL과 분리된 저장소를 사용한다.
- 파일 기반 `H2` + volume을 기본안으로 한다.
- 이유: 메인 DB 장애 중에도 이벤트와 지표 이력을 유지하기 위함이다.

## 메인 도메인 테이블 초안

- `books`
- `members`
- `loans`

## 모니터링 테이블 초안

- `service_status_history`
- `incident_history`
- `admin_action_audit`
- `reliability_snapshot`
- `aiops_analysis_history`

## 데이터 관리 원칙

- 엔티티와 DTO를 분리한다.
- 서비스 계층에서 트랜잭션 경계를 관리한다.
- 지표 계산의 기준 이벤트는 영속 저장한다.
