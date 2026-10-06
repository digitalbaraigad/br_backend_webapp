# Ba Raigad backend pre-production

## Profile

Use the `preprod` profile supplied in `src/main/resources/application-preprod.properties`:

```powershell
$env:SPRING_PROFILES_ACTIVE = "preprod"
.\mvnw.cmd spring-boot:run
```

For a deployed server, set the variables in `.env.preprod.example` through the host's encrypted secret manager. Do not create a committed file containing actual credentials.

The pre-production profile sets `spring.jpa.hibernate.ddl-auto=validate`; database schema changes must be deployed through reviewed migrations rather than Hibernate changing the schema at application startup.

## Deployment check

1. Build: `.\mvnw.cmd clean package -DskipTests`
2. Run the generated JAR with `SPRING_PROFILES_ACTIVE=preprod`.
3. Set `CORS_ALLOWED_ORIGINS` to the exact pre-production frontend domain, without a trailing slash.
4. Set all database, JWT, email, and WhatsApp values using the hosting provider's secret store.
5. Confirm HTTPS is terminated in front of the API and only trusted reverse-proxy traffic can reach it.

## Before production

- Rotate the WhatsApp access token that was previously stored in source configuration.
- Use separate database and service credentials for pre-production and production.
- Add a database migration tool such as Flyway before making further schema changes.
- Protect the admin portal with real admin authentication; never distribute automatic admin credentials to browsers.
