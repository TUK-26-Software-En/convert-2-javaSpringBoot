# Materials Index

## 목적

- 이 디렉토리는 현재 `library-service` Java 프로젝트를 기준으로 다이어그램과 명세서를 정리한 산출물 공간이다.
- 다이어그램은 `코드 기반 As-Is`와 `문서 기반 To-Be`를 분리한다.

## 범위

- 중심 범위: `lib_checkout/` Spring Boot 메인 서비스
- 연관 범위: `docker-compose.yml`, `PostgreSQL`, `docx/`의 아키텍처/도메인 문서
- 제외 범위: `service_monitor/`의 상세 설계와 운영 플로우

## 분류 원칙

- `diagrams/as-is/`: 현재 코드에서 직접 확인 가능한 구조와 흐름
- `diagrams/to-be/`: `docx/`에 적힌 목표 구조와 기능 범위
- `specs/`: 다이어그램을 설명하는 텍스트 명세서

## 디렉토리 구조

```text
materials/
  00-index.md
  diagrams/
    as-is/
      01-library-runtime-context.puml
      02-library-package-structure.puml
      03-library-erd.puml
      04-seq-book-registration.puml
      05-seq-loan-checkout.puml
      06-seq-loan-return.puml
      07-seq-home-dashboard-read.puml
      08-seq-member-dashboard-read.puml
    to-be/
      11-library-target-context.puml
      12-library-layered-architecture.puml
      13-book-usecase.puml
      14-member-usecase.puml
      15-loan-usecase.puml
      16-scope-gap-map.puml
  specs/
    01-system-overview.md
    02-architecture-spec.md
    03-domain-spec.md
    04-database-spec.md
    05-web-flow-spec.md
    06-runtime-spec.md
    07-implementation-gap.md
```

## 소스 기준

### 코드 기반 As-Is

- `lib_checkout/src/main/java/org/tukorea/libcheckout/**`
- `lib_checkout/src/main/resources/**`
- `lib_checkout/src/main/resources/db/migration/V1__init_schema.sql`
- `docker-compose.yml`

### 문서 기반 To-Be

- `docx/context/01-target-architecture.md`
- `docx/context/03-container-topology.md`
- `docx/codex/01-package-structure.md`
- `docx/domains/00-book-management.md`
- `docx/domains/01-member-management.md`
- `docx/domains/02-loan-management.md`

## 권장 읽기 순서

1. `specs/01-system-overview.md`
2. `diagrams/as-is/01-library-runtime-context.puml`
3. `diagrams/as-is/02-library-package-structure.puml`
4. `diagrams/as-is/03-library-erd.puml`
5. `specs/03-domain-spec.md`
6. `specs/05-web-flow-spec.md`
7. `diagrams/to-be/*.puml`
8. `specs/07-implementation-gap.md`

## 해석 주의

- As-Is는 현재 구현을 설명한다.
- To-Be는 목표 범위를 설명한다.
- 기능이 To-Be에 있어도, As-Is에 없으면 현재 구현된 기능이 아니다.
