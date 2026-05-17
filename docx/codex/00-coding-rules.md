# Coding Rules

## 공통 규칙

- Java는 `21` 문법을 사용한다.
- Spring Boot는 `3.x` 기준으로 구현한다.
- 패키지는 기능 우선 구조를 사용한다.
- Controller는 얇고 명확하게 유지한다.
- Service는 비즈니스 정책을 모은다.
- Repository는 저장소 접근만 담당한다.

## 계층 규칙

- `presentation`은 HTTP, View, DTO 조립을 담당한다.
- `business`는 use case와 정책을 담당한다.
- `dataaccess`는 JPA, JDBC, 외부 API, Docker API 접근을 담당한다.
- 상위 계층이 하위 계층을 직접 우회하지 않는다.

## UI 규칙

- `HTML`, `CSS`, `JS`는 분리한다.
- JS는 데이터 검증 보조와 UI 상호작용만 수행한다.
- 핵심 비즈니스 검증은 반드시 서버에서 다시 수행한다.

## 테스트 규칙

- 비즈니스 규칙은 단위 테스트를 우선 작성한다.
- DB 연동 기능은 통합 테스트로 검증한다.
- Docker 제어와 보안 정책은 별도 검증 시나리오를 가진다.

## 문서 규칙

- 패키지 구조 변경 시 `docx/codex/01-package-structure.md`를 갱신한다.
- 보안 규칙 변경 시 `docx/context/05-monitoring-security-model.md`와 `docx/operations/03-security-validation.md`를 함께 갱신한다.
