# Implementation Gap

## 비교 기준

- As-Is: 현재 `lib_checkout` 코드
- To-Be: `docx/context/*`, `docx/domains/*`, `docx/codex/*`

## 기능 갭 표

| 영역 | 문서 기준 목표 | 현재 구현 | 상태 |
|---|---|---|---|
| Book | 목록/상세/등록/수정/상태 변경 | 목록/등록 | 부분 구현 |
| Member | 목록/상세/등록/수정/상태 변경 | 목록/등록/대시보드 통계 | 부분 구현 |
| Loan | 신청/반납/목록/상세/연체 조회 | 신청/반납/목록, 연체 집계 | 부분 구현 |
| Home | 시스템 요약 화면 | 상세 대시보드형 Home | 구현 확장 |
| Members UI | 단순 목록 이상 통계 제공 가능 | 상태/대출 통계형 대시보드 | 구현 확장 |
| DB Seed | 실행 검증용 초기 데이터 | `docker-pg` 빈 DB 조건부 SQL 시드 | 구현 완료 |

## 구조 갭 표

| 항목 | 문서 기준 목표 | 현재 코드 상태 |
|---|---|---|
| Package | feature-first + 3-layer | 일치 |
| View | Thymeleaf 기반 서버 렌더링 | 일치 |
| JS 역할 | UI 보조 역할 | 대체로 일치 |
| Monitoring integration | monitor가 health/logs/incidents 활용 | library-service는 health 제공 수준 |

## 현재 문서와 코드의 차이

- 문서는 `상세 조회`, `수정`, `상태 변경`을 포함하지만, 현재 메인 서비스는 `조회/등록/대출/반납` 중심이다.
- 문서는 더 넓은 교육용 목표 범위를 설명하고, 현재 코드는 실행 가능한 최소 핵심 흐름과 대시보드 가시성을 먼저 구현했다.

## 후속 작성/개발 추천

1. Book/Member/Loan 상세 조회 화면 추가
2. 수정/상태 변경 시나리오 추가
3. Loan 연체 전용 필터 또는 탭 추가
4. To-Be usecase를 기준으로 Controller/API 범위 재정렬

## 관련 다이어그램

- `../diagrams/to-be/16-scope-gap-map.puml`
