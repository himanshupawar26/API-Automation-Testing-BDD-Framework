# API Automation Testing BDD Framework

A Cucumber BDD + REST Assured API test automation framework in Java, targeting the public [ReqRes](https://reqres.in) REST API. Built as an SDET/automation portfolio project.

> This repo previously held a 2022 course-exercise version of this framework (Java 7, a single feature file, a hardcoded Windows filesystem path, build artifacts committed to git). It has been fully rewritten; history is preserved in `git log`.

## What this demonstrates

- **BDD test design**: Gherkin feature files readable by non-engineers, backed by typed step definitions — not step definitions that just wrap raw HTTP calls with no abstraction.
- **A real resource client layer**: `UsersClient` / `AuthClient` wrap REST Assured request construction once per resource, so step definitions describe behavior, not HTTP plumbing.
- **Scenario isolation, not shared mutable state**: Cucumber's dependency-injection container gives every scenario its own `TestContext` instance. (The old version used a `static String placeId` field shared across step definition instances — a real bug under parallel execution. See `docs/` or git history for the before/after if asked.)
- **No hardcoded environment config**: base URL resolves from a system property → environment variable → classpath `config.properties`, in that order — works identically on any machine and in CI. (The old version had a hardcoded `C:\Users\...` path.)
- **Honest test design against a demo API**: see "About the target API" below — the scenarios are written to test what ReqRes actually does, not what a tutorial assumed it did.
- **CI with tagged suites**: a fast `@smoke` run on every PR, a full `@regression` run on push to `main` and nightly (catching drift in the live third-party API independently of any code change here).
- **Allure reporting**, published to GitHub Pages on every `main` build.

## About the target API

[ReqRes](https://reqres.in) is a free, publicly hosted demo REST API. Two important things about how it actually behaves, confirmed by hitting it directly rather than assumed from documentation:

- `GET /users` and `GET /users/{id}` return **real, persistent fixture data** (12 users across 2 pages) — these are safe to assert against strongly.
- `POST`, `PUT`, `PATCH`, `DELETE` on `/users` are **not actually persisted**. ReqRes accepts the request and echoes back a realistic response (generated `id`/timestamp), but a subsequent `GET` will not reflect the write. The scenarios for these endpoints validate the request/response **contract** (status code, response shape, field echoing) — not persistence — and the feature file says so explicitly.
- `GET /users/{id}` wraps the user in a `data` envelope; `POST`/`PUT`/`PATCH /users` return the user fields at the root with no wrapper. These are genuinely different shapes from the same API, modeled as two distinct POJOs (`User` vs `UserEnvelope`) rather than one "flexible" class that papers over the difference.

## Stack

| Layer | Tool |
|---|---|
| Language | Java 17 |
| Build | Maven |
| BDD | Cucumber 7 (JUnit Platform Suite engine, not the legacy JUnit4 runner) |
| HTTP | REST Assured 5 |
| Serialization | Jackson |
| Assertions | AssertJ |
| Reporting | Allure (+ Cucumber's own HTML/JSON report) |
| CI | GitHub Actions |

## Project structure

```
src/main/java/com/himanshupawar/api/
├── clients/         # UsersClient, AuthClient — thin REST Assured wrappers, one per resource
└── pojo/            # User, UserEnvelope, UserListResponse, AuthRequest, ErrorResponse

src/test/java/com/himanshupawar/api/
├── config/          # Config.java — env resolution, no hardcoded paths
├── hooks/           # TestContext (per-scenario state), Hooks (before/after logging)
├── stepdefinitions/ # UsersSteps, AuthSteps, CommonSteps
└── runners/         # RunCucumberTest — JUnit Platform Suite entry point

src/test/resources/
├── config.properties
└── features/
    ├── users.feature
    └── auth.feature
```

## Running locally

```bash
mvn test                                          # full suite
mvn test -Dcucumber.filter.tags="@smoke"          # fast subset
mvn test -Dcucumber.filter.tags="@regression"     # full regression tag
mvn test -Dbase.url=https://reqres.in/api         # override target explicitly
```

Generate and view the Allure report after a run:
```bash
mvn io.qameta.allure:allure-maven:serve
```

## CI

- **On PR**: runs the `@smoke` suite only — fast feedback.
- **On push to `main`**: runs `@smoke` then the full `@regression` suite, publishes the Allure report to GitHub Pages.
- **Nightly (02:00 UTC)**: runs the full regression suite against the live API on a schedule, independent of any code change here — this is what catches the target API changing shape or behavior without anyone touching this repo (which is exactly what happened during this rewrite: documentation didn't match the live API's actual response envelopes, found only by calling it directly).

## Related project

This framework is intended to eventually gain a second target: [bizops360-app](https://github.com/himanshupawar26/bizops360-app), a ServiceNow scoped application currently in development (blocked on ServiceNow Developer Instance provisioning). Once that app exists, its REST API will be onboarded as a second suite under this same framework, to demonstrate extending an existing framework to a new system under test rather than rebuilding one per project.

## License

MIT
