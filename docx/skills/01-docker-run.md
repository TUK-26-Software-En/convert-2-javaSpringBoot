# Docker Run

## 목적

- Compose 기반 통합 실행을 표준 실행 절차로 정의한다.

## 대상 서비스

- `library-service`
- `postgres`
- `service-monitor`

## 실행 원칙

- 메인 통합 검증은 Docker Compose로 수행한다.
- Compose 환경에서는 메인 서비스가 PostgreSQL을 사용한다.
- 모니터링 서비스는 별도 volume에 이력을 저장한다.

## 확인 항목

- 각 컨테이너 기동 여부
- healthcheck 통과 여부
- 네트워크 연결 여부
- Dashboard 접속 여부

## 초기 데이터 원칙

- `library-service`는 `docker-pg` 프로필로 실행될 때 `books`, `members`, `loans`가 모두 비어 있으면 `db/seed/demo-data-postgres.sql`을 실행한다.
- 이미 데이터가 존재하면 dummy data는 다시 넣지 않는다.
