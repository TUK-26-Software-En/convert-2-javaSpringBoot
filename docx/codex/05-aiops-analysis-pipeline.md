# AI-Ops Analysis Pipeline

## 1차 목표

- 규칙 기반 RCA를 우선 구현한다.
- 외부 LLM 연동이 가능한 인터페이스를 함께 준비한다.

## 파이프라인 단계

1. `IncidentDetector`
2. `EvidenceCollector`
3. `RuleBasedRcaAnalyzer`
4. `LlmRcaAdapter`
5. `HybridRcaOrchestrator`
6. `RecommendationService`
7. `AiOpsDashboardService`

## 분석 입력

- health 이벤트
- 컨테이너 상태 변화
- 로그 오류 패턴
- 장애 주입 이벤트
- 관리자 액션 이력

## 분석 출력

- incident 요약
- 추정 원인
- 근거 로그와 이벤트
- 영향 범위
- 권장 대응

## 운영 원칙

- 규칙 기반 분석이 기본 동작 경로다.
- LLM 연동 실패 시에도 분석 파이프라인은 동작해야 한다.
- 자동 복구는 기본 비활성이다.
