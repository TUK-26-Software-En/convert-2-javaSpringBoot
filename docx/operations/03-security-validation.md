# Security Validation

## 인증 검증

- 비로그인 상태에서 관리자 Dashboard 제어 기능 차단
- 일반 사용자 권한으로 Docker 제어 API 차단

## Docker 제어 검증

- allowlist 대상 외 컨테이너 제어 차단
- allowlist 외 명령 차단
- 제어 요청 시 감사 로그 기록 확인

## 컨테이너 하드닝 검증

- `read_only` 적용 여부
- `cap_drop: [ALL]` 적용 여부
- `no-new-privileges` 적용 여부
- 자원 제한 적용 여부

## AI-Ops 보안 검증

- 로그 마스킹 적용 여부
- 외부 LLM 비활성 상태 fallback 동작 확인
- 외부 전송 대상 데이터 제한 여부 확인
