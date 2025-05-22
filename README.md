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

## Manual Testing
1. To setup manual testing in the db, all credentials are already provided in the `application-test.yml` file
2. To run the db in-memory, simply run:
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=test
```

This will run server in test mode using that yml.
3. To view the db, navigate to: `http://localhost:8080/h2-console` to view h2 console.
4. Once there, just change the JBDC url to: `jdbc:h2:mem:testdb` to connect to the DB in memory
5. Once logged in, simple SQL queries will show the data.