# Failure Injection Playbook

## 위치

- 장애 주입은 실습용 관리자 기능이다.
- 운영 기본 경로가 아니라 검증과 교육 목적의 제어된 절차다.

## 허용 시나리오 초안

- 메인 서비스 일시 정지
- 메인 서비스 재시작
- DB 중단 및 복구
- 응답 지연 시뮬레이션

## 실행 원칙

- 장애 주입 전 정상 상태를 기록한다.
- 장애 주입 중 로그와 health 변화를 추적한다.
- 복구 후 신뢰성 지표와 incident 기록을 확인한다.

## 활성화 조건

- `service-monitor` 컨테이너에 `DOCKER_CONTROL_ENABLED=true`가 설정되어야 한다.
- `service-monitor` 컨테이너에 `FAULT_INJECTION_ENABLED=true`가 설정되어야 한다.
- `docker.sock`은 `service-monitor`에 writable mount 되어야 한다.

## 현재 제어 버튼

- `pause`
- `unpause`
- `start`
- `stop`
- `restart`

## 금지 사항

- allowlist 밖 대상 제어
- 파괴적 데이터 삭제
- 승인 없는 자동 장애 주입 반복
