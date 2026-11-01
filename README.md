# Coworking Booking

A learning project exploring **Hexagonal Architecture (Ports & Adapters)** with Spring Boot.

The application manages room reservations in a coworking space: check availability,
book a room, cancel a reservation, and list a user's reservations.

## Why this project

The goal is not to build a production-ready booking platform, but to practice
separating business logic from technical concerns. The domain is pure Java, the
application layer orchestrates use cases, and the infrastructure plugs in REST,
JPA and notifications. Dependencies always point inward.

## Architecture

- **`domain`** — aggregates, value objects and business rules. Fully unit-tested,
  no framework dependency.
- **`application`** — inbound ports (use cases), outbound ports (repositories,
  notification), commands, and service implementations. No Spring annotations.
- **`infrastructure`** — adapters that connect the core to the outside world:
  REST controllers, JPA entities and repositories, mappers, and Spring configuration.

The architecture is enforced by **ArchUnit** tests: the domain cannot depend on
Spring or JPA, the application cannot depend on the infrastructure, and adapters
must implement ports.

## Project structure
src/main/java/com/coworkinglab/coworkingbooking/
├── domain/
│   ├── model/            # Room, Reservation, TimeSlot, ids, status
│   └── exception/        # Domain exceptions
├── application/
│   ├── port/in/          # Use case interfaces
│   ├── port/out/         # Repository and notification interfaces
│   ├── command/          # Input objects for use cases
│   └── service/          # Use case implementations
└── infrastructure/
├── adapter/in/web/   # REST controllers, DTOs, mappers, exception handler
├── adapter/out/      # JPA persistence, notification
└── config/           # Spring bean configuration

## API

### Check room availability

**Endpoint**
```http
GET /api/rooms/available?from=2026-11-15T14:00:00&to=2026-11-15T16:00:00
```

Returns the rooms that are free on the given time slot.

### Book a room

**Endpoint**
```http
POST /api/reservations
Content-Type: application/json
```

**Request body**
```json
{
  "roomId": "11111111-1111-1111-1111-111111111111",
  "userId": "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa",
  "start": "2026-11-15T14:00:00",
  "end": "2026-11-15T16:00:00"
}
```

**Responses**

Returns `201 Created` with the reservation, or:

- `400 Bad Request` — invalid slot (too short, too long, or in the past).
- `409 Conflict` — the room is already booked on an overlapping slot.

### Cancel a reservation

**Endpoint**
```http
DELETE /api/reservations/{id}
```

**Responses**

Returns the reservation with status `CANCELLED`, or:

- `422 Unprocessable Entity` — cancellation is less than 24 hours before the start, or the reservation is already cancelled.

### List a user's reservations

**Endpoint**
```http
GET /api/reservations?userId={userId}
```

Returns all reservations belonging to the specified user.

## Tech stack

- Java 25
- Spring Boot 4.x
- Spring Web, Spring Data JPA
- H2 (in-memory database for development)
- JUnit 5, AssertJ
- ArchUnit


## Getting started

### Prerequisites

- JDK 25
- Maven (or use the included `./mvnw` wrapper)

### Run the application

```bash
./mvnw spring-boot:run


