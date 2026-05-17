# Docker Control Policy

## 목적

- 모니터링 서비스가 실습 목적의 제한된 Docker 제어를 수행하도록 한다.

## 허용 대상 컨테이너

- `library-service`
- `postgres`

## 허용 명령

- `inspect`
- `logs`
- `stats`
- `pause`
- `unpause`
- `start`
- `stop`
- `restart`

## 금지 명령

- `exec`
- `rm`
- `build`
- `pull`
- 임의 컨테이너 생성
- allowlist 밖 컨테이너 제어

## 구현 규칙

- Controller에서 Docker API를 직접 호출하지 않는다.
- Service는 허용 정책을 검사한다.
- Data access 어댑터가 실제 Docker API 호출을 수행한다.
- 모든 제어 요청은 감사 로그를 남긴다.
