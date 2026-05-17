# Monitoring Contract

## 메인 서비스 노출 대상

- `/actuator/health`
- `/actuator/info`
- 필요 시 업무 상태 확인용 내부 API

## 모니터링 서비스 수집 대상

- 메인 서비스 health 결과
- PostgreSQL 연결 상태
- Docker 컨테이너 상태
- 메인 서비스 로그
- 장애 주입 이력
- 관리자 조작 이력

## 모니터링 서비스 제공 대상

- 관리자 Dashboard 페이지
- health 요약 API
- 로그 조회 API
- incident 조회 API
- reliability metrics 조회 API
- 관리자용 장애 주입 API

## 계약 원칙

- 수집 실패도 이벤트로 저장한다.
- 장애 판정 기준은 문서화한다.
- Dashboard 표시 항목은 지표 계산 기준과 일치해야 한다.
