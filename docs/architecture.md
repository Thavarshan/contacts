# Architecture

Contacts CLI is a small layered Java application. The boundaries are intentionally explicit so the
storage adapter, domain model, application service, and terminal interface can evolve independently.

## Goals

- Keep the application runnable with only Java and a local file.
- Keep business behavior testable without invoking the terminal.
- Keep CSV schema concerns out of the domain model.
- Keep CLI parsing and terminal formatting isolated from persistence.
- Prefer simple, explicit abstractions over framework-heavy infrastructure.

## Layers

```text
CLI commands
    |
    v
ContactService
    |
    v
ContactRepository
    |
    v
CsvContactRepository + CsvContactMapper
    |
    v
CsvReader / CsvWriter
```

## Packages

- `com.contacts`: domain model, service contract, repository contract, CSV repository, and mapper.
- `com.contacts.cli`: Picocli command-line application and CLI formatting/search helpers.
- `com.filemanager`: generic row-oriented reader and writer contracts.
- `com.filemanager.csv`: OpenCSV-backed row reader and writer.

## Main Components

### Domain Model

`Contact` and `Address` represent contact data. They are plain Java objects and do not depend on
Picocli, OpenCSV, or filesystem APIs.

`Contact` owns a stable string ID. New contacts receive generated UUID strings by default.

### Application Service

`ContactService` coordinates use cases:

- Load all contacts.
- Find contacts by stable ID.
- Add contacts.
- Update contacts while preserving IDs.
- Delete contacts by ID.
- Replace all contacts.
- Append imported contacts.

The service depends on the `ContactRepository` interface, which keeps persistence replaceable.

### Persistence

`ContactRepository` defines contact persistence operations. `CsvContactRepository` implements that
contract by coordinating row reads and writes with `CsvContactMapper`.

Repository behavior:

- Blank rows are ignored.
- Header rows are skipped.
- Saves rewrite the full file with the current header followed by contact rows.
- Concurrent writes are not supported.

### CSV Mapping

`CsvContactMapper` owns the CSV schema and the translation between `Contact` objects and string
arrays. It validates that data rows use the current 17-column schema.

List fields are encoded as URL-encoded values joined by `|`. Address values are encoded as nested
URL-encoded parts joined by `~`.

### Row I/O

`Reader` and `Writer` are generic row-oriented contracts. `CsvReader` and `CsvWriter` adapt OpenCSV
to those contracts. This keeps OpenCSV-specific behavior outside the contact domain and service
layers.

### CLI

`CliApplication` is the Picocli root command. It owns CLI dependency construction and exposes
subcommands for user workflows. CLI helpers include:

- `ContactFormatter` for compact list output.
- `ContactMatcher` for case-insensitive search behavior.

Subcommands use `ContactService` rather than manipulating CSV files directly.

## Contact Identity

Every contact has a stable string ID. New contacts receive generated UUIDs. The CSV schema stores
the ID in the first column. CSV rows must match the current header exactly; incompatible development
data should be regenerated or migrated deliberately.

User-facing commands accept IDs for stable automation. Commands that take a contact reference also
accept one-based list indexes for interactive convenience.

## CSV Schema

The current CSV header is:

```text
id,firstName,middleName,lastName,nickName,company,jobTitle,department,phoneNumbers,emailAddresses,urlAddresses,addresses,birthday,dates,socialProfiles,instantMessageHandles,notes
```

List fields are encoded into one CSV cell with URL-encoded values joined by `|`. Address fields are
encoded as nested URL-encoded parts joined by `~`.

See [Data Format](data-format.md) for the complete schema contract.

## CLI

The CLI uses Picocli annotations for command parsing and help generation. `CliApplication` is the
root command and owns dependency construction. Subcommands use `ContactService` rather than talking
to CSV files directly.

Command reference is documented in [CLI Reference](cli-reference.md).

## Quality Gates

The Maven `verify` lifecycle runs:

- Unit and integration tests.
- Shaded CLI jar packaging.
- Javadocs.
- Spotless formatting checks.
- PMD.
- SpotBugs.
- JaCoCo coverage check.
- Maven Enforcer rules.

CI runs the same `./mvnw -B -ntp verify` command.

See [Testing and Quality](testing-quality.md) for the full quality workflow.

## Extension Points

### Add a New Storage Backend

Implement `ContactRepository` and wire the new implementation at the application boundary. Keep
domain and service code unchanged.

### Add a New Export Format

Add a mapper or writer specific to that format and keep CSV behavior in `CsvContactMapper`.

### Add a New CLI Workflow

Add a Picocli subcommand that depends on `ContactService`. Keep terminal output formatting in CLI
helpers when it is reused by multiple commands.

## Known Constraints

- Storage is file-based and not safe for concurrent writers.
- Search is in-memory and linear over all contacts.
- CSV schema compatibility is not preserved while the project remains pre-production.
- Duplicate contact IDs are preserved during append import; deduplication is not currently a service rule.
