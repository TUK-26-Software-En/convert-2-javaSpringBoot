# Monitoring Ops

## 목적

- 관리자 관점의 모니터링 사용 절차를 정의한다.

## 기본 운영 흐름

1. Overview 탭에서 전체 상태 확인
2. Main Service 탭에서 health와 최근 이벤트 확인
3. Database 탭에서 연결 상태와 장애 이력 확인
4. Logs 탭에서 오류 로그 추적
5. Reliability 탭에서 MTBF, MTTR, MTTF, Availability 확인

## 관리자 제어 원칙

- Docker 제어는 관리자만 수행한다.
- 제어 전 대상과 목적을 확인한다.
- 제어 후 감사 로그와 상태 변화를 즉시 확인한다.
