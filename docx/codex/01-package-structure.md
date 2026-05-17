# Package Structure

## 메인 서비스 패키지 초안

```text
org.tukorea.libcheckout
  global
    config
    common
    exception
  book
    model
    presentation
      controller
      dto
    business
      service
      policy
    dataaccess
      entity
      repository
  member
    model
    presentation
      controller
      dto
    business
      service
      policy
    dataaccess
      entity
      repository
  loan
    model
    presentation
      controller
      dto
    business
      service
      policy
    dataaccess
      entity
      repository
```

## 모니터링 서비스 패키지 초안

```text
org.tukorea.servicemonitor
  global
    config
    common
    exception
    security
  dashboard
  health
  logs
  dockercontrol
  incidents
  metrics
  aiops
```

## 리소스 구조 초안

```text
src/main/resources
  templates
    home
    book
    member
    loan
    dashboard
  static
    css
    js
    image
```

## 규칙

- 기능 단위 패키지 안에서 다시 계층으로 분리한다.
- 공통 코드는 `global`에만 둔다.
- 기능과 무관한 util 확산을 피한다.
