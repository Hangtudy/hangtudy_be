# HangTudy BE

## 기술 스택
- Kotlin
- Spring Boot 3.4.5
- Swagger (SpringDoc OpenAPI 2.8.6)

### 필수 조건
- JDK 21 이상
- Gradle


애플리케이션은 기본적으로 `http://localhost:8080`에서 실행됩니다.

## API 엔드포인트

### 헬스 체크 API
- `GET /v1/api/health/check`: 시스템 상태 확인

## Swagger UI 접속
애플리케이션 실행 후, 다음 URL에서 Swagger UI를 확인할 수 있습니다:
```
http://localhost:8080/swagger-ui/index.html
```