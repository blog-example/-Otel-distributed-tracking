# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 프로젝트 개요

Spring Cloud Gateway와 OpenTelemetry를 활용한 분산 시스템 모니터링 학습 프로젝트. 대기질 모니터링 시스템을 예제로 구현 중.

## 빌드 및 실행

```bash
# 전체 서비스 실행 (Docker)
docker-compose up --build

# 개별 서버 로컬 실행
cd server-a && ./gradlew bootRun   # 포트 8081
cd server-b && ./gradlew bootRun   # 포트 8082
cd server-bb && ./gradlew bootRun  # 포트 8083
```

## 서버 구성

| 서버 | 포트 | 역할 | 기술 스택 |
|------|------|------|-----------|
| server-a | 8081 | 대기질 데이터 저장 API | Spring MVC, MongoDB |
| server-b | 8082 | 대기질 데이터 Kafka 발행 | WebFlux, Reactor Kafka |
| server-bb | 8083 | Kafka 메시지 수신 및 저장 | Spring MVC, Spring Kafka, MongoDB |

## 아키텍처

### 패키지 구조 (헥사고날 아키텍처)
```
server-*/src/main/kotlin/com/example/server*/
├── application/
│   ├── domain/           # 도메인 모델, Store/Publisher 인터페이스
│   └── service/
│       ├── airquality/   # AirQualityService 인터페이스 및 구현체
│       └── etc/          # HelloService 등 기타 서비스
└── infrastructure/
    ├── mongodb/          # Repository, Adapter (Store 구현)
    ├── kafka/            # Producer/Consumer, Adapter (Publisher 구현)
    │   ├── config/       # Kafka 설정
    │   ├── message/      # Message<T>, Payload 정의
    │   └── producer/     # Producer 인터페이스 및 구현체 (server-b)
    └── web/              # API 인터페이스, Controller
```

### 인터페이스 패턴
- **Service**: `application/service/`에 인터페이스 정의, Payload/Result 내부 클래스로 정의
- **Store** (저장소): `application/domain/`에 인터페이스 정의, `infrastructure/mongodb/`에서 Adapter로 구현
- **Publisher** (발행): `application/domain/`에 인터페이스 정의, `infrastructure/kafka/`에서 Adapter로 구현
- **API**: `infrastructure/web/`에 인터페이스 정의, Request/Response 내부 클래스로 정의

### Kafka 메시지 구조
```kotlin
data class Message<T>(
    val messageId: String,
    val correlationId: String,
    val timestamp: Instant,
    val source: String,
    val payload: T
)
```

### 데이터 흐름
```
[server-b] POST /api/b/air-quality
  → AirQualityController → AirQualityService → AirQualityPublisher
    → AirQualityProducer → Kafka (air-quality-topic)

[server-bb] Kafka Consumer
  → AirQualityListener → AirQualityService → AirQualityStore → MongoDB
```

## 인프라 구성 (docker-compose)

| 서비스 | 포트 | 설명 |
|--------|------|------|
| Grafana | 3000 | 모니터링 대시보드 (admin/admin) |
| Loki | 3100 | 로그 저장소 |
| Promtail | - | 로그 수집기 (logging=promtail 라벨 필터) |
| Kafka | 9092 | 메시지 큐 (KRaft 모드) |
| MongoDB | 27017 | 데이터베이스 (root/root) |

## API 엔드포인트

모든 서버의 air-quality API는 동일한 시그니처 사용:
```
POST /api/{server}/air-quality
Request: { longitude, latitude, pollutants: [{type, value}], deviceId }
Response: { id }
```

## Kafka 토픽

- `air-quality-topic`: 대기질 데이터 메시지
- `air-quality-topic.DLT`: Dead Letter Topic

## 남은 작업

- Spring Cloud Gateway 추가
- OpenTelemetry 설정
- Grafana 대시보드 구성
