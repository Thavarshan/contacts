# Contacts CLI

A small Java 17 command-line contacts manager backed by CSV storage.

The application demonstrates a layered design with domain models, repository interfaces, CSV adapters, a service layer, and focused CLI commands.

## Features

- Add contacts with names, company details, phone numbers, emails, URLs, social profiles, dates, and notes.
- List contacts in a compact terminal-friendly format.
- Search across contact fields.
- Show full details for one contact.
- Update contacts by stable ID or list index.
- Delete contacts by stable ID or list index.
- Import and export CSV files.
- Clear all contacts.
- Store data in a portable CSV file.
- Build a runnable shaded CLI JAR with Maven.

## Requirements

- Java 17 or newer.
- No local Maven installation required when using the included Maven Wrapper.

## Quick Start

```bash
./mvnw verify
java -jar target/contacts-1.0.0-SNAPSHOT-cli.jar --help
```

Create and list contacts:

```bash
java -jar target/contacts-1.0.0-SNAPSHOT-cli.jar --file contacts.csv add \
  --first-name Jane \
  --last-name Doe \
  --email jane@example.com \
  --phone +15550100

java -jar target/contacts-1.0.0-SNAPSHOT-cli.jar --file contacts.csv list
```

## CLI Commands

```text
contacts [--file contacts.csv] <command> [options]
```

Commands:

- `list`: show all contacts.
- `add`: add a contact.
- `search <query>`: search contact fields.
- `show <id-or-index>`: show full details for a stable contact ID or one-based list index.
- `update <id-or-index>`: update a contact.
- `delete <id-or-index>`: delete a contact by stable contact ID or one-based list index.
- `clear`: delete all contacts.
- `import <path> [--replace]`: import contacts from another CSV file.
- `export <path>`: export contacts to a CSV file.
- `--help`: print usage.

Add options:

- `--first-name`, `--middle-name`, `--last-name`, `--nick-name`
- `--company`, `--job-title`, `--department`
- `--phone`, `--email`, `--url`, `--social`, `--im` repeated as needed
- `--birthday yyyy-MM-dd`
- `--notes text`

Contacts have stable generated IDs. Use `list` to see IDs, then pass an ID to `show`, `update`, or
`delete` for script-safe automation. One-based indexes still work for interactive use.

## Development

Run the full local verification pipeline:

```bash
./mvnw -B -ntp verify
```

Useful commands:

```bash
./mvnw test
./mvnw spotless:check
./mvnw spotless:apply
./mvnw pmd:check
./mvnw spotbugs:check
```

The CI pipeline runs the same Maven verification and uploads test reports and built artifacts.
JaCoCo enforces a minimum coverage gate during `verify`.

## Project Layout

```text
src/main/java/com/contacts       Domain, service, mapper, and repository code
src/main/java/com/contacts/cli   CLI parsing and commands
src/main/java/com/filemanager    Generic row-oriented file abstractions
src/test/java                    Unit and integration tests
docs                             Architecture notes and ADRs
examples                         Sample CSV and CLI transcript
```

## Architecture

- Domain models: `Contact`, `Address`
- Service layer: `ContactService`
- Persistence abstraction: `ContactRepository`
- CSV adapter: `CsvContactRepository`, `CsvContactMapper`
- CLI parser: Picocli command annotations

See [docs/architecture.md](docs/architecture.md) for more detail.

## Documentation

- [Documentation index](docs/README.md)
- [User guide](docs/user-guide.md)
- [CLI reference](docs/cli-reference.md)
- [CSV data format](docs/data-format.md)
- [Development guide](docs/development.md)
- [Testing and quality gates](docs/testing-quality.md)
- [CI and security](docs/ci-security.md)
- [Release process](docs/release-process.md)
- [Troubleshooting](docs/troubleshooting.md)
- [Architecture decisions](docs/adr/README.md)

## License

This project is licensed under the MIT License. See [LICENSE](LICENSE).
