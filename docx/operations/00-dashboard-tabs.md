# Dashboard Tabs

## 기본 탭

- `Overview`
- `Main Service`
- `Database`
- `Logs`
- `Reliability Metrics`
- `Failure Injection`

## 각 탭의 목적

- `Overview`: 전체 상태 요약
- `Main Service`: 메인 서비스 상태, health, recent incidents, runtime detail, recent Docker logs
- `Database`: PostgreSQL 상태, 장애 이력, runtime detail, recent Docker logs
- `Logs`: 최근 Docker 로그와 최근 health transition 이벤트
- `Reliability Metrics`: MTBF, MTTR, MTTF, Availability 표시
- `Failure Injection`: 관리자 대상 제어형 장애 주입과 현재 제어 상태 표시

## UI 원칙

- 탭별 책임을 명확히 분리한다.
- 상태 표시와 제어 기능을 혼합하지 않는다.
- 관리자 조작은 확인 절차와 감사 로그를 동반한다.

## 현재 반영 상태

- `Overview`는 health 요약과 현재 container state를 함께 보여준다.
- `Main Service`, `Database`는 probe target, image, state, health, port, CPU, memory, network, recent logs를 함께 보여준다.
- `Failure Injection`은 `pause`, `unpause`, `start`, `stop`, `restart`를 allowlist 대상에 한해 실행한다.
