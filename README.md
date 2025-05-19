# Communication Service

This Spring Boot service sends messages via WhatsApp (Meta Business API, Gupshup) and SMS/WhatsApp (Twilio) with provider fallback.

## Prerequisites
- Java 17+
- Maven 3.6+

## Setup
1. Clone the repo
2. Update credentials in `src/main/resources/application.yml`
3. Run `./mvnw spring-boot:run`
4. POST to `http://localhost:8080/api/messages`

## Example Request
```bash
curl -X POST http://localhost:8080/api/messages \
  -H "Content-Type: application/json" \
  -d '{"to":"+919876543210","content":"Hello!","channel":"WHATSAPP"}'
```
