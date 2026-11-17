# E-Commerce Hybrid Automation Framework

A Java 17/Maven automation starter combining Selenium UI checks and Rest Assured API checks. The hybrid scenario provisions a user through the API, authenticates that user, performs a browser checkout, then verifies the UI order through the API.

> **Application contract:** no store or API specification was supplied. Configure `config.properties` for the AUT and map its UI selectors and API resource paths. The example API contract expects JSON user creation/login and bearer-authenticated user, cart, and order resources. The sample UI expects the configured CSS selectors and a checkout action that returns a visible order number. Tests intentionally fail fast when placeholder hosts remain.

## Key features

- Page Object Model with Page Factory initialization, configurable CSS selectors, and explicit waits.
- Thread-local WebDriver lifecycle; Chrome, Edge, and Firefox with WebDriverManager and headless support.
- Rest Assured request/response specifications, bearer-token handling, Jackson POJOs, and safe request/response metadata logs. Request bodies and authorization headers are not logged.
- TestNG groups, suite files, parallel suite execution, listener, one-retry analyzer, and JSON/Excel `DataProvider` examples.
- Timestamped Extent HTML report, per-failure screenshots, Log4j2 console and rolling file logs.
- GitHub Actions and Jenkins pipelines archive test/report output.

## Architecture

```text
TestNG Suites / Tests
      |                         |
      v                         v
UI Base + Page Objects       API Base + EcommerceApi
      |                         |
WebDriverFactory / Waits     Request & Response Specs
      |                         |
Selenium WebDriver           Rest Assured + Jackson DTOs
      \                         /
       \---- Hybrid E2E flow --/
                 |
       Config / Test Data / Logs
                 |
       TestNG Listener -> Extent + Screenshots
```

## Project layout

```text
src/main/java/com/example/ecommerce/
  api/                 API client, specs, safe logging filter
  api/models/          Jackson request/response records
  config/              Property configuration
  listeners/           TestNG listener and retry analyzer
  pages/               Page Objects
  reports/             Extent lifecycle
  utilities/           Driver, waits, screenshots, Excel and JSON readers
src/main/resources/
  config.properties    AUT/API hosts, endpoint paths, selector mappings
  log4j2.xml
  extent-config.xml
  testdata/users.json
src/test/java/
  base/ dataproviders/ tests/
testng.xml              UI + hybrid suite
testng-api.xml          API-focused suite
```

## Setup and execution

Requirements: JDK 17, Maven 3.8+, network access for Maven/driver downloads, and reachable AUT/API environments.

1. Edit `src/main/resources/config.properties`:
   - Set `base.url` and `api.base.url`.
   - Map API resource paths and CSS selectors to the AUT.
   - Ensure the AUT supports user registration/login and API tokens used as bearer credentials.
2. Keep credentials and environment-specific secrets outside source control. Supply overrides using JVM system properties or CI secret injection. For example:

   ```powershell
   mvn clean test -Denv=qa -Dbrowser=chrome -Dheadless=true `
     -Dbase.url=https://qa-store.example -Dapi.base.url=https://qa-api.example
   ```

3. Execute suites:

   ```powershell
   mvn clean test
   mvn clean test -Papi-suite
   ```

   `browser` accepts `chrome`, `edge`, or `firefox`. `env` is included in reports; host configuration is supplied through system properties/configuration.

Reports are written to `target/extent-reports/`, screenshots to `target/screenshots/`, logs to `target/logs/`, and TestNG/Surefire output to `target/surefire-reports/`.

### Test data

- JSON: `testdata/users.json`, loaded through Jackson in `TestDataProviders.jsonSearchTerms`.
- Excel: `ExcelReader` reads `src/main/resources/testdata/test-users.xlsx`; the included workbook has a `users` sheet with `email` and `password` columns and placeholder data. Replace these values with disposable test-account credentials to run `UiCartLifecycleTest`.
- Do not put real user passwords or access tokens in checked-in test data.

### API contract to map

The sample calls use:

| Action | Default path | Expected behavior |
|---|---|---|
| Create user | `/api/users` | JSON `{email,password}`, 200 or 201 |
| Login/token | `/api/auth/login` | JSON `{email,password}`, token and user id in response |
| User details | `/api/users/{id}` | Bearer token, JSON user details |
| Add cart | `/api/cart` | Bearer token, JSON `{productId,quantity}` |
| Create order | `/api/orders` | Bearer token, JSON `{cartId}` |
| Order details | `/api/orders/{id}` | Bearer token, JSON order |

Change resource paths, payload DTOs, response aliases, status expectations, and UI locators to match the real product contract. Authentication may be cookie-based or use a different token format; adapt the API and login page accordingly. The example hybrid flow validates the UI-created order by order number; the AUT must expose a compatible order lookup identifier.

Set `api.test.product.id` to a product identifier available to the test account before running `ApiOrderTest`.

### CI/CD

- GitHub Actions workflow: `.github/workflows/ci.yml` (Java 17, Maven verify, artifacts uploaded even on failure).
- `Jenkinsfile` uses Jenkins tools named `JDK17` and `Maven3`; configure matching tool installations and browser dependencies on the agent. Set base URLs and secrets as Jenkins-managed properties/credentials before execution.
- MySQL validation is intentionally optional and not wired without a target schema/contract.

## Interview questions and sample answers

1. **Why combine API and UI tests?** API setup makes end-to-end workflows faster and less brittle; the UI still validates customer-facing behavior, while the final API read verifies persisted state.
2. **How is parallel browser execution isolated?** `DriverFactory` stores each WebDriver in a `ThreadLocal` and removes it after `quit()`. Test data must also be unique per concurrent execution.
3. **Why use explicit waits?** They synchronize with observable application states and avoid fixed sleeps that slow suites and create timing flakiness.
4. **How are secrets protected in logs?** The API filter records method, query-stripped URI, response status, and elapsed time only; payloads and authorization headers are omitted.
5. **How would you reduce flaky retries?** Diagnose the underlying synchronization/environment issue first, keep retry count low, and report initial failures distinctly; retries must not disguise product defects.
6. **How should this framework support multiple environments?** Inject host/selector/credential settings through environment-specific properties or CI secret stores, while keeping test logic independent of environment.
7. **What would you add before production adoption?** Align contracts with the real AUT, add schema/negative tests, controlled test-data cleanup, robust correlation IDs, browser/container provisioning, and CI quality gates.

## Resume project entry

**E-Commerce Hybrid UI/API Automation Framework | Java 17, Selenium, TestNG, Maven, Rest Assured**

- Designed a modular POM-based framework combining REST API setup/authentication with browser-based checkout and API order verification.
- Implemented reusable browser lifecycle, explicit waits, configurable selectors, typed API models, bearer authentication, and JSON/Excel data providers.
- Integrated TestNG grouping/parallel execution, failure screenshots, Extent HTML reports, Log4j2 logging, and GitHub Actions/Jenkins execution.
- Prioritized thread isolation and secret-safe API logging; adapted the framework to application-specific API/UI contracts.
