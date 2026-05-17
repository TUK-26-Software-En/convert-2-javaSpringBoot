# Target Architecture

## 목표 시스템

- 메인 서비스: 도서 대출 업무 처리와 사용자 화면 제공
- 모니터링 서비스: 상태 수집, 장애 탐지, 대시보드, 장애 주입, Optional AI-Ops 제공
- DB 서비스: PostgreSQL 운영 저장소

## 전체 구조

- `library-service`
- `service-monitor`
- `postgres`

## 메인 서비스 아키텍처

- 구조 원칙: `Feature-first + 3-Layer + MVC`
- 기능: `book`, `member`, `loan`
- 각 기능 내부 계층:
  - `presentation`
  - `business`
  - `dataaccess`

## 모니터링 서비스 아키텍처

- 기능: `dashboard`, `health`, `logs`, `dockercontrol`, `incidents`, `metrics`, `aiops`
- 기본 범위:
  - health 수집
  - 로그 수집
  - 장애 탐지
  - 신뢰성 지표 계산
- Optional 범위:
  - AI-Ops RCA
  - 외부 LLM 연동

## 설계 원칙

- Controller는 얇게 유지한다.
- Service가 시스템 정책과 도메인 규칙을 통합한다.
- Data access는 기술 상세를 감춘다.
- View는 서버 데이터에 의존하고 JS는 UI 보조 역할만 수행한다.
