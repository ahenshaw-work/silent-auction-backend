# AGENTS.md

Quarkus 3 / Java 17 API for the silent auction. REST resources live under `/api/v1`. The frontend proxies those paths; the infra repo deploys this service.

## Commands

Use the Maven wrapper from the repo root. Do not call a system `mvn`.

```bash
./mvnw compile quarkus:dev
./mvnw test
./mvnw package
```

`./mvnw package` writes `target/quarkus-app/quarkus-run.jar`. Integration tests are skipped (`skipITs` in `pom.xml`). There is no test suite yet; `quarkus-junit5` is available when you add one.

Dev mode listens on the port in `src/main/resources/application.properties` (`quarkus.http.port`, currently 6443). OpenAPI is `/openapi`. Swagger UI is `/swagger-ui`. Dev Services starts PostgreSQL and runs Liquibase from `devservices/changeLog.yaml`.

## Layout

```
src/main/java/controllers/{auctions,bids,users}   JAX-RS resources and request/response types
src/main/java/repositories/{auctions,bids,users}  repository interfaces and EntityManager implementations
src/main/resources/db/changeLogs/                 schema changesets (prod and dev)
src/main/resources/devservices/changeLogs/        dev-only seed data
src/main/resources/application.properties         config, including %dev and %prod profiles
src/main/docker/Dockerfile.jvm                    image used by .tekton/pipelinerun.yaml
```

Packages stay as `controllers.*` and `repositories.*`. Do not introduce a new root package.

## Conventions

- Match the file you are editing: field `@Inject`, Jakarta REST annotations, and existing path parameter names such as `auction_id`.
- Mutations that write to the database are `@Transactional`. Admin-only operations use `@RolesAllowed("admin")`. Other API methods stay `@Authenticated`.
- New schema changesets go in `src/main/resources/db/changeLogs/`. Prod loads that directory from `db/changeLog.yaml`. Dev loads the same directory, then `devservices/changeLogs/`, from `devservices/changeLog.yaml`. Seed data stays in the devservices changelog.
- Hibernate schema generation is `none`. Liquibase owns the schema (`auction`).
- Endpoint shape is documented in `README.md`. Update that list when you add or change a route.

## Guardrails

- Do not copy secrets from `application.properties`, `devservices/realm.json`, or logs into new files, commits, or chat.
- Do not commit `.env` or `target/`.
- Pipeline changes belong in `.tekton/pipelinerun.yaml`. The image build uses `src/main/docker/Dockerfile.jvm`.

## Skills

Reusable workflows belong in `.agents/skills/`. Read [`.agents/skills/README.md`](.agents/skills/README.md) before adding one. Load a skill only when the task matches its description.
