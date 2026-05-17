# Test Strategy

## 테스트 계층

- 단위 테스트: 도메인 규칙과 서비스 정책
- 통합 테스트: DB 연동, 컨트롤러, 프로필 구동
- 시스템 테스트: Docker Compose 기반 서비스 기동과 연결
- 보안 테스트: 인증, 권한, Docker 제어 제한

## 우선 검증 항목

- 도서 등록/조회/수정 흐름
- 회원 등록/상태 변경 흐름
- 대출/반납/연체 흐름
- H2와 PostgreSQL 프로필 구동
- 모니터링 health와 로그 수집

## Optional 검증 항목

- 장애 주입 기록
- RCA 분석 결과 표시
- LLM fallback 동작
