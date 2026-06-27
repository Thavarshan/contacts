# Development Guide

## Local Setup

Install Java 17 or newer:

```bash
java --version
```

Build and verify with the Maven Wrapper:

```bash
./mvnw -B -ntp verify
```

No local Maven installation is required.

Install Node.js 20.18 or newer and npm 10 or newer for the dashboard:

```bash
node --version
npm --version
npm install
npm ci --prefix web
```

## Project Layout

```text
core/src/main/java/com/contacts       Domain, service, repository contracts, CSV repository, mapper
core/src/main/java/com/filemanager    Generic row reader and writer abstractions
core/src/main/java/com/filemanager/csv OpenCSV-backed reader and writer
cli/src/main/java/com/contacts        Picocli application and command-line adapter
api/src/main/java/com/contacts/api    Javalin HTTP API adapter
web                                  Next.js dashboard
*/src/test/java                       Unit and integration tests
docs                                  Architecture, operations, and process documentation
examples                              Sample CSV and command transcript
```

## Coding Standards

- Java and XML use 4 spaces for indentation.
- Markdown, YAML, JSON, TypeScript, TSX, and CSS use 2 spaces.
- Source files use UTF-8, LF line endings, final newlines, and trimmed trailing whitespace.
- Keep dependencies explicit in `pom.xml`; avoid snapshot dependencies.
- Keep command behavior covered by integration tests.
- Prefer package boundaries that already exist before adding new abstractions.

Formatting is enforced by `.editorconfig` and Spotless:

```bash
./mvnw -B -ntp spotless:apply
npm --prefix web run format
npm run format
```

Repository policy files live at the monorepo root. Do not add nested `.github`, `.husky`,
`.editorconfig`, `.gitignore`, Prettier, or commit-message configuration under `web/`.

## Design Principles

The codebase is intentionally layered:

- Domain objects do not know about CSV or Picocli.
- `ContactService` owns application use cases and delegates persistence to `ContactRepository`.
- `CsvContactRepository` adapts contact persistence to row-oriented file I/O.
- `CsvContactMapper` isolates CSV schema and encoding rules.
- CLI commands depend on `ContactService`, not directly on CSV internals except for root dependency construction.
- API routes depend on `ContactService`, not on CLI commands or CSV internals except for adapter dependency construction.
- The web dashboard depends on the Java API over HTTP, not on the CLI executable or direct CSV access.

When adding behavior, put it at the narrowest layer that owns the rule. For example:

- Formatting terminal output belongs in `com.contacts.cli`.
- CSV encoding belongs in `CsvContactMapper`.
- Persistence collection semantics belong in `CsvContactRepository`.
- Use case rules belong in `ContactService`.

## Adding a CLI Command

1. Add a nested Picocli command class in `CliApplication` or extract it if it grows large.
2. Register the command in the root `@Command(subcommands = ...)` list.
3. Use `ContactService` for contact operations.
4. Add unit tests for supporting helpers.
5. Add integration tests through the CLI entry point.
6. Update [CLI Reference](cli-reference.md), [User Guide](user-guide.md), and `README.md` if user-facing behavior changes.

## Adding an API Endpoint

1. Add the route in `ContactsApi`.
2. Keep request and response JSON in small payload records.
3. Delegate use case behavior to `ContactService`.
4. Add Javalin integration tests in `api/src/test/java`.
5. Update [API Reference](api-reference.md) and any web dashboard proxy code that consumes the endpoint.

## Changing the Web Dashboard

1. Keep reusable contact UI in `web/components`.
2. Keep contact API client behavior in `web/lib/contact-store.ts`.
3. Prefer Next API routes as the browser-facing boundary.
4. Add or update Vitest tests under `web/tests`.
5. Run `npm --prefix web run verify`.

## Changing the CSV Schema

1. Update `CsvContactMapper.HEADER`.
2. Update `fromRow` and `toRow` together.
3. Update [Data Format](data-format.md) and `examples/contacts.sample.csv`.
4. Add mapper and repository tests for the new behavior.
5. Decide whether development data should be regenerated or migrated.

This project is still in development and does not preserve compatibility with old CSV schemas by
default.

## Dependency Updates

Dependency updates are manual. Do not add Dependabot configuration.

For each dependency update:

1. Update the version property or dependency in `pom.xml`.
2. Run `./mvnw -B -ntp verify`.
3. Review release notes for breaking changes.
4. Document user-facing or operational impact.

## Before Opening a Pull Request

Run:

```bash
npm run format:check
npm run verify
```

Then confirm:

- Tests cover changed behavior.
- Documentation matches command and schema changes.
- Generated files under `target/` are not committed.
- Generated files under `web/.next/`, `web/node_modules/`, and `web/test-results/` are not committed.
- The PR description explains what changed, why, and how it was verified.
