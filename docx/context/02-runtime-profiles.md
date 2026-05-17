# Runtime Profiles

## 메인 서비스 프로필

### `local-h2`

- 목적: 초기 개발, 빠른 단독 실행, 기능 검증
- DB: `H2`
- 특징: 설치 부담이 적고 개발 속도가 빠르다.

### `docker-pg`

- 목적: Docker Compose 기반 기본 실행 환경
- DB: `PostgreSQL`
- 특징: 운영 환경과 유사한 저장소를 사용한다.

### `test`

- 목적: 테스트 자동화
- 초기 구현: `H2`
- 확장 가능성: PostgreSQL 호환성 검증 테스트 추가

## 모니터링 서비스 프로필

### `monitor-local`

- 목적: 단독 개발과 UI 작업
- 저장소: 파일 기반 `H2`

### `monitor-docker`

- 목적: Docker Compose 기반 실행
- 저장소: 파일 기반 `H2`와 volume
- 특징: 메인 DB 장애와 분리된 장애 이력 보존

## 환경 변수 초안

- `SPRING_PROFILES_ACTIVE`
- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `MONITOR_DB_PATH`
- `DOCKER_CONTROL_ENABLED`
- `FAULT_INJECTION_ENABLED`
- `AIOPS_ENABLED`
- `LLM_ENABLED`
