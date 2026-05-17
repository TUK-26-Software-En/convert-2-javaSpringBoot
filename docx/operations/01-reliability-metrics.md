# Reliability Metrics

## 계산 대상

- `MTBF`
- `MTTR`
- `MTTF`
- `Availability`

## 이벤트 정의

- 장애 시작: 상태가 `UP -> DOWN`으로 전환된 시점
- 복구 완료: 상태가 `DOWN -> UP`으로 전환된 시점
- 정상 가동 구간: 복구 완료 후 다음 장애 시작 전까지의 시간

## 계산식

- `MTTF = 평균 정상 동작 시간`
- `MTTR = 평균 복구 시간`
- `MTBF = 평균 고장 간격`
- `Availability = MTTF / (MTTF + MTTR)`

## 저장 원칙

- 상태 변화 이벤트를 영속 저장한다.
- 계산 결과는 스냅샷 형태로 보관할 수 있다.
- 수집 실패 자체도 계산 근거 이벤트가 될 수 있다.
