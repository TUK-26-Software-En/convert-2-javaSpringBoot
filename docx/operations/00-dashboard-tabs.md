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
- `Main Service`: 메인 서비스 상태, health, recent incidents
- `Database`: PostgreSQL 상태와 장애 이력
- `Logs`: 오류 로그와 최근 이벤트
- `Reliability Metrics`: MTBF, MTTR, MTTF, Availability 표시
- `Failure Injection`: 관리자 대상 제어형 장애 주입

## UI 원칙

- 탭별 책임을 명확히 분리한다.
- 상태 표시와 제어 기능을 혼합하지 않는다.
- 관리자 조작은 확인 절차와 감사 로그를 동반한다.
