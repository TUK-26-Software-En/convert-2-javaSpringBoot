# Roadmap

## Phase 0. 문서 기준선 수립

- `docx/` 구조 생성
- 규칙, 아키텍처, 보안, 운영, 도메인 문서 작성
- Optional AI-Ops와 기본 범위를 분리 명시

## Phase 1. 메인 서비스 현대화

- 기존 `lib_checkout`를 `Spring Boot 3.x + Java 21`로 업그레이드
- 기존 샘플 코드를 정리하고 패키지 구조를 재구성
- `H2`와 `PostgreSQL` 프로필을 분리

## Phase 2. 도메인 기능 구축

- `book` 기능 구현
- `member` 기능 구현
- `loan` 기능 구현
- 서버 렌더링 기반 View와 정적 리소스 분리

## Phase 3. Docker Compose 환경 구축

- `library-service` 컨테이너 작성
- `postgres` 컨테이너 작성
- 모니터링 애플리케이션 `service_monitor/`와 Compose 서비스 `service-monitor` 골격 작성
- 네트워크, 볼륨, healthcheck 구성

## Phase 4. 모니터링 기본 기능 구축

- 서비스 health 수집
- DB 상태 수집
- 로그 수집
- 장애 탐지
- 장애 이력 저장
- `MTBF`, `MTTR`, `MTTF`, `Availability` 계산

## Phase 5. 보안 통제 구축

- 관리자 인증과 권한 분리
- Docker 제어 allowlist 적용
- 감사 로그 저장
- 컨테이너 하드닝 적용
- 보안 검증 시나리오 작성

## Phase 6. Optional AI-Ops 틀 구축

- Incident 탐지 파이프라인
- 규칙 기반 RCA 엔진
- 외부 LLM 연동 가능한 인터페이스
- 대시보드 RCA 탭 구성

## Phase 7. 검증 및 정리

- 단위 테스트, 통합 테스트, 보안 검증
- Docker Compose 기반 실행 확인
- 문서와 코드의 정합성 점검
