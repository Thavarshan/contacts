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

## Project Layout

```text
src/main/java/com/contacts       Domain, service, repository contracts, CSV repository, mapper
src/main/java/com/contacts/cli   Picocli application, commands, formatting, search matching
src/main/java/com/filemanager    Generic row reader and writer abstractions
src/main/java/com/filemanager/csv OpenCSV-backed reader and writer
src/test/java                    Unit and integration tests
docs                             Architecture, operations, and process documentation
examples                         Sample CSV and command transcript
```

## Coding Standards

- Java and XML use 4 spaces for indentation.
- Markdown, YAML, and JSON use 2 spaces.
- Source files use UTF-8, LF line endings, final newlines, and trimmed trailing whitespace.
- Keep dependencies explicit in `pom.xml`; avoid snapshot dependencies.
- Keep command behavior covered by integration tests.
- Prefer package boundaries that already exist before adding new abstractions.

Formatting is enforced by `.editorconfig` and Spotless:

```bash
./mvnw -B -ntp spotless:apply
```

## Design Principles

The codebase is intentionally layered:

- Domain objects do not know about CSV or Picocli.
- `ContactService` owns application use cases and delegates persistence to `ContactRepository`.
- `CsvContactRepository` adapts contact persistence to row-oriented file I/O.
- `CsvContactMapper` isolates CSV schema and encoding rules.
- CLI commands depend on `ContactService`, not directly on CSV internals except for root dependency construction.

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
./mvnw -B -ntp spotless:apply
./mvnw -B -ntp verify
```

Then confirm:

- Tests cover changed behavior.
- Documentation matches command and schema changes.
- Generated files under `target/` are not committed.
- The PR description explains what changed, why, and how it was verified.
