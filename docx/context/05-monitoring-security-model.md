# Monitoring Security Model

## 전제

- `service-monitor`는 `docker.sock` 접근 권한을 가진다.
- 이 권한은 매우 강력하며, 컨테이너 제어 권한을 사실상 호스트 수준으로 확장할 수 있다.

## 보안 목표

- Docker 제어를 없애는 것이 아니라 통제된 범위로 제한한다.
- 관리자 외 접근을 차단한다.
- 허용 대상과 허용 명령만 수행한다.
- 모든 민감 동작을 감사 가능하게 만든다.

## 필수 통제

- 관리자 인증 적용
- 역할 기반 권한 분리
- CSRF 보호 유지
- 대상 컨테이너 allowlist 적용
- 허용 명령 allowlist 적용
- 감사 로그 저장
- 민감 기능 feature flag 적용
- 컨테이너 하드닝 적용

## 컨테이너 하드닝 초안

- `read_only: true`
- `cap_drop: [ALL]`
- `security_opt: ["no-new-privileges:true"]`
- `tmpfs` 사용
- CPU, memory, PID 제한
- 최소 포트 노출

## 검증 항목

- 인증 없는 Docker 제어 요청 차단
- 일반 사용자 접근 차단
- allowlist 밖 컨테이너 제어 차단
- allowlist 밖 명령 차단
- 감사 로그 기록 여부 확인
- 하드닝 설정 적용 여부 확인
