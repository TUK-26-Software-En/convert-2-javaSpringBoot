# Container Topology

## 컨테이너 구성

- `library-service`: 메인 서비스
- `postgres`: 운영 데이터 저장소
- `service-monitor`: 모니터링 및 관리자 Dashboard

## 네트워크 원칙

- 내부 애플리케이션 통신은 전용 Docker network를 사용한다.
- `postgres`는 가능하면 외부에 직접 노출하지 않는다.
- `service-monitor`는 관리자 접근용 포트만 노출한다.

## 볼륨 원칙

- PostgreSQL 데이터는 named volume에 저장한다.
- 모니터링 이력 저장용 H2 파일은 별도 volume을 사용한다.
- 필요 시 메인 서비스 로그 저장 위치를 별도 volume으로 분리한다.

## health 흐름

- `service-monitor` -> `library-service` health API 조회
- `service-monitor` -> `postgres` JDBC probe 또는 health check
- `service-monitor` -> Docker API를 통한 컨테이너 상태 조회

## 로그 흐름

- 1차 경로: Docker API를 통한 컨테이너 로그 조회
- 보조 경로: 파일 또는 volume 기반 로그 수집

## 장애 주입 흐름

- 관리자 -> `service-monitor` Dashboard
- `service-monitor` -> Docker 제어 어댑터
- Docker 제어 어댑터 -> allowlist 대상 컨테이너에 제한 명령 실행
