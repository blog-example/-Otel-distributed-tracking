# Open-Tel

Spring Cloud Gateway와 OpenTelemetry를 활용한 분산 시스템 모니터링 학습 프로젝트

## 실행

```bash
docker-compose up --build
```

## 대시보드

| 서비스 | URL | 계정 |
|--------|-----|------|
| Grafana | http://localhost:3000 | admin / admin |
| Kafka UI | http://localhost:8080 | - |

## API 엔드포인트

| 서버 | URL | 설명 |
|------|-----|------|
| server-a | http://localhost:8081/api/a/air-quality | MongoDB 직접 저장 |
| server-b | http://localhost:8082/api/b/air-quality | Kafka로 발행 |

## 인프라 포트

| 서비스 | 포트 |
|--------|------|
| Kafka | 9092 |
| MongoDB | 27017 |
| Loki | 3100 |
