# Auction House

Plataforma de leilões em tempo real. Projeto de aprendizagem: o domínio é um pretexto, o objetivo é a tecnologia.

**Stack:** Java 21 · Spring Boot 4 · Hibernate/JPA · Arquitetura hexagonal · PostgreSQL · Kafka · Redis · SSE · React

## Arquitetura

```
backend/
  domain/          Modelo de negócio puro (Auction, Bid, Money...). Zero frameworks.
  application/     Casos de uso + ports.
                     port/in  -> o que o mundo pede à aplicação (ex: PlaceBidUseCase)
                     port/out -> o que a aplicação precisa do mundo (ex: AuctionRepository, EventPublisher)
  infrastructure/  Adapters + Spring Boot.
                     adapter/in/web          REST controllers, SSE
                     adapter/in/messaging    Kafka consumers
                     adapter/out/persistence JPA entities + repos (implementam ports out)
                     adapter/out/messaging   Kafka producers / outbox
                     adapter/out/cache       Redis
frontend/          React + Vite + TypeScript (fase 6)
```

Regra de ouro: as dependências apontam sempre **para dentro** (infrastructure → application → domain).
Os módulos Maven garantem isto em compilação; o `HexagonalArchitectureTest` (ArchUnit) garante o resto.

## Correr localmente

```bash
docker compose up -d            # Postgres :5432, Kafka :9092, Kafka UI :8081, Redis :6379
cd backend && mvn verify        # build + testes (inclui ArchUnit)
mvn -pl infrastructure spring-boot:run
curl localhost:8080/actuator/health
```

## Roadmap

- [x] **Fase 1: Setup.** Monorepo, módulos hexagonais, docker-compose, ArchUnit.
- [ ] **Fase 2: Domínio e persistência.** Agregado `Auction` com regras de licitação, casos de uso, JPA + Flyway, REST, Testcontainers.
      Conceitos: aggregate, value objects, mapeamento domínio↔entidade, optimistic locking (`@Version`), N+1, transações.
- [ ] **Fase 3: Kafka.** Eventos `BidPlaced`/`AuctionClosed`, Transactional Outbox, consumidores idempotentes, DLQ.
      Conceitos: partições e chave (`auctionId`) → ordem, consumer groups, at-least-once, retries.
- [ ] **Fase 4: Redis.** Cache da maior licitação, rate limiting por utilizador, lock distribuído para fechar leilões.
      Conceitos: cache-aside vs write-through, TTL, `SET NX PX`, scripts Lua/atomicidade.
- [ ] **Fase 5: SSE.** `GET /auctions/{id}/stream`, fan-out entre várias instâncias, heartbeats, `Last-Event-ID`.
- [ ] **Fase 6: Frontend.** React: lista de leilões, página de leilão ao vivo com `EventSource`, countdown.
- [ ] **Fase 7: Nível sénior.** Spring Security + JWT, observabilidade (Micrometer/OpenTelemetry), testes de carga (k6/Gatling), 2 instâncias atrás de um proxy.
