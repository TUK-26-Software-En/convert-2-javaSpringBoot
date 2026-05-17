# Runtime Specification

## 실행 프로필

| 프로필 | 저장소 | 목적 |
|---|---|---|
| `local-h2` | H2 in-memory | 빠른 로컬 개발 |
| `test` | H2 in-memory | 자동 테스트 |
| `docker-pg` | PostgreSQL | Docker Compose 통합 실행 |

## Docker Compose 기준 구성

### postgres

- image: `postgres:16-alpine`
- DB 이름: `lib_checkout`
- 사용자: `lib_checkout`
- volume: `postgres-data`

### library-service

- build context: `./lib_checkout`
- profile: `docker-pg`
- port: `8080`
- DB 연결:
  - `DB_URL`
  - `DB_USERNAME`
  - `DB_PASSWORD`

## 기동 순서

1. `postgres` healthcheck 통과
2. `library-service` 기동
3. `docker-pg` 프로필로 PostgreSQL 연결
4. Flyway schema validation/migration 수행
5. 테이블이 모두 비어 있으면 SQL 더미 데이터 주입
6. HTTP 요청 처리 시작

## 더미 데이터 주입 정책

- 트리거 클래스: `DockerDemoDataSqlInitializer`
- 프로필 제한: `docker-pg`
- 입력 SQL: `classpath:db/seed/demo-data-postgres.sql`
- 재실행 방지: `books`, `members`, `loans` 모두 0건일 때만 수행

## Health 관련 노출

- `GET /actuator/health`
- `GET /actuator/info`

## 운영 관점 참고

- 현재 `library-service`는 자체 운영 대시보드를 Home/Members 화면에 포함한다.
- 모니터링/장애 주입은 별도 `service-monitor` 범위다.

## 관련 다이어그램

- `../diagrams/as-is/01-library-runtime-context.puml`
- `../diagrams/to-be/11-library-target-context.puml`
