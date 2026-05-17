# LLM Fallback Strategy

## 목적

- 외부 LLM이 비활성, 지연, 실패 상태여도 AI-Ops 파이프라인이 중단되지 않게 한다.

## 기본 원칙

- 규칙 기반 RCA가 기본 경로다.
- LLM은 부가 설명과 요약을 보강하는 선택 경로다.

## fallback 시나리오

- `LLM disabled`: 규칙 기반 결과만 표시
- `LLM timeout`: 규칙 기반 결과만 표시하고 timeout 이벤트 기록
- `LLM error`: 규칙 기반 결과만 표시하고 오류 이력 저장
- `LLM invalid response`: 결과 폐기 후 규칙 기반 결과 유지

## Dashboard 표시 규칙

- LLM 결과가 없을 때도 incident와 RCA 화면은 정상 동작해야 한다.
- LLM 결과 사용 여부를 화면에 명확히 표시한다.
