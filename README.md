# Contacts Monorepo

A small Java 17 contacts manager backed by CSV storage, with a reusable core, a Picocli command-line
adapter, a lightweight Javalin HTTP API adapter, and a Next.js web dashboard.

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
- Build runnable shaded CLI and API JARs with Maven.
- Run a Next.js dashboard that communicates with the Java API through HTTP.

## Requirements

- Java 17 or newer.
- Node.js 20.18 or newer and npm 10 or newer for the web dashboard.
- No local Maven installation required when using the included Maven Wrapper.

## Quick Start

```bash
./mvnw verify
java -jar cli/target/contacts-cli-1.0.0-cli.jar --help
```

Create and list contacts:

```bash
java -jar cli/target/contacts-cli-1.0.0-cli.jar --file contacts.csv add \
  --first-name Jane \
  --last-name Doe \
  --email jane@example.com \
  --phone +15550100

java -jar cli/target/contacts-cli-1.0.0-cli.jar --file contacts.csv list
```

Start the API:

```bash
CONTACTS_CSV_PATH=contacts.csv CONTACTS_API_PORT=7070 \
  java -jar api/target/contacts-api-1.0.0-api.jar
```

Check the API:

```bash
curl http://127.0.0.1:7070/health
curl http://127.0.0.1:7070/api/contacts
```

Install and start the web dashboard:

```bash
npm install
npm ci --prefix web
CONTACTS_API_URL=http://127.0.0.1:7070 npm --prefix web run dev
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
npm run format:check
npm run web:verify
npm run verify
```

Repository-wide configuration lives at the root: GitHub workflows, issue templates, pull request
templates, Husky hooks, Prettier settings, `.editorconfig`, and `.gitignore`. The `web/` directory
keeps only application-specific Next.js, ESLint, TypeScript, Playwright, and package configuration.

The CI pipeline runs Java verification and web verification as separate jobs. JaCoCo enforces a
minimum Java coverage gate during `verify`.

## Project Layout

```text
core/src/main/java/com/contacts       Domain, service, mapper, and repository code
core/src/main/java/com/filemanager    Generic row-oriented file abstractions
cli/src/main/java/com/contacts        Picocli CLI adapter
api/src/main/java/com/contacts/api    Javalin HTTP API adapter
web                                  Next.js dashboard
*/src/test/java                       Unit and integration tests
docs                                  Architecture notes and ADRs
examples                              Sample CSV and CLI transcript
```

## Architecture

- Domain models: `Contact`, `Address`
- Service layer: `ContactService`
- Persistence abstraction: `ContactRepository`
- CSV adapter: `CsvContactRepository`, `CsvContactMapper`
- CLI parser: Picocli command annotations
- HTTP API: Javalin routes that reuse the same service layer

See [docs/architecture.md](docs/architecture.md) for more detail.

## Documentation

- [Documentation index](docs/README.md)
- [User guide](docs/user-guide.md)
- [CLI reference](docs/cli-reference.md)
- [API reference](docs/api-reference.md)
- [CSV data format](docs/data-format.md)
- [Development guide](docs/development.md)
- [Testing and quality gates](docs/testing-quality.md)
- [CI and security](docs/ci-security.md)
- [Deployment](docs/deployment.md)
- [Release process](docs/release-process.md)
- [Troubleshooting](docs/troubleshooting.md)
- [Architecture decisions](docs/adr/README.md)

## License

This project is licensed under the MIT License. See [LICENSE](LICENSE).
