# Design Patterns

## 필수 패턴

- `MVC`: 요청, 화면, 모델을 분리한다.
- `Service Layer`: 비즈니스 규칙을 응용 서비스로 모은다.
- `Repository Pattern`: 데이터 접근 기술을 숨긴다.
- `DTO Pattern`: View와 Entity를 분리한다.

## 모니터링 특화 패턴

- `Strategy`: health probe 방식, 장애 주입 방식, RCA 분석기 교체를 지원한다.
- `Facade`: AI-Ops 진입점을 단순화한다.
- `Observer/Event`: 상태 변화 발생 시 지표 계산과 RCA를 트리거한다.
- `Chain of Responsibility`: 여러 RCA 규칙을 순차 적용한다.
- `Adapter`: Docker API, LLM API 등 외부 시스템 연동을 캡슐화한다.

## 적용 원칙

- 패턴은 단순화와 역할 분리를 위해 사용한다.
- 불필요한 추상화는 피한다.
- Optional 기능의 확장 지점에만 유연성을 집중한다.
