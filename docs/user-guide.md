# User Guide

Contacts CLI is a Java 17 command-line address book stored in a local CSV file. It is designed for
portable personal data, scriptable commands, and simple local automation without a database server.

## Requirements

- Java 17 or newer.
- A terminal.
- The included Maven Wrapper for building from source.

Check Java:

```bash
java --version
```

Build the runnable CLI jar:

```bash
./mvnw verify
```

The executable shaded jar is created at:

```text
cli/target/contacts-cli-1.0.0-cli.jar
```

## First Run

Use `--file` to choose the CSV file. When omitted, the CLI uses `contacts.csv` in the current
directory.

```bash
java -jar cli/target/contacts-cli-1.0.0-cli.jar --file contacts.csv list
```

An empty or missing file is valid. The CLI prints:

```text
No contacts found.
```

## Add Contacts

```bash
java -jar cli/target/contacts-cli-1.0.0-cli.jar --file contacts.csv add \
  --first-name Jane \
  --last-name Doe \
  --company Acme \
  --job-title Engineer \
  --department Platform \
  --email jane@example.com \
  --phone +15550100 \
  --url https://example.com/jane \
  --social linkedin:jane-doe \
  --im jane@example.net \
  --birthday 1990-01-02 \
  --notes "Met at platform review"
```

Repeatable options can be supplied more than once:

```bash
java -jar cli/target/contacts-cli-1.0.0-cli.jar --file contacts.csv add \
  --first-name Alex \
  --last-name Rivera \
  --email alex.work@example.com \
  --email alex.personal@example.com \
  --phone +15550101 \
  --phone +15550102
```

Every added contact receives a stable generated ID. The command prints that ID after saving.

## List Contacts

```bash
java -jar cli/target/contacts-cli-1.0.0-cli.jar --file contacts.csv list
```

Example output:

```text
1. Jane Doe [11111111-1111-1111-1111-111111111111] <jane@example.com>
```

The number at the beginning is a one-based list index for interactive use. The value in brackets is
the stable contact ID for automation.

## Search Contacts

```bash
java -jar cli/target/contacts-cli-1.0.0-cli.jar --file contacts.csv search jane
```

Search is case-insensitive and checks names, organization fields, contact methods, addresses, dates,
social profiles, instant message handles, and notes.

## Show Full Details

Use a stable ID:

```bash
java -jar cli/target/contacts-cli-1.0.0-cli.jar --file contacts.csv show 11111111-1111-1111-1111-111111111111
```

Use a list index:

```bash
java -jar cli/target/contacts-cli-1.0.0-cli.jar --file contacts.csv show 1
```

IDs are preferred in scripts because list indexes can change when data changes.

## Update Contacts

Update accepts the same contact options as `add`. Only supplied scalar fields are changed. For list
fields such as `--email` and `--phone`, supplying the option replaces that list with the supplied
values.

```bash
java -jar cli/target/contacts-cli-1.0.0-cli.jar --file contacts.csv update 1 \
  --company "Acme Corporation" \
  --job-title "Staff Engineer" \
  --email jane.new@example.com
```

## Delete Contacts

```bash
java -jar cli/target/contacts-cli-1.0.0-cli.jar --file contacts.csv delete 1
```

For scripts:

```bash
java -jar cli/target/contacts-cli-1.0.0-cli.jar --file contacts.csv delete 11111111-1111-1111-1111-111111111111
```

## Import and Export

Append contacts from another CSV file:

```bash
java -jar cli/target/contacts-cli-1.0.0-cli.jar --file contacts.csv import examples/contacts.sample.csv
```

Replace the current file with imported contacts:

```bash
java -jar cli/target/contacts-cli-1.0.0-cli.jar --file contacts.csv import examples/contacts.sample.csv --replace
```

Export the current contacts:

```bash
java -jar cli/target/contacts-cli-1.0.0-cli.jar --file contacts.csv export backups/contacts.csv
```

## Clear Contacts

```bash
java -jar cli/target/contacts-cli-1.0.0-cli.jar --file contacts.csv clear
```

This writes a valid CSV file containing only the header.

## Data Safety

- Keep backups before running destructive commands such as `clear`, `delete`, or import with `--replace`.
- Prefer stable IDs over list indexes in scripts.
- Treat the CSV file as single-writer storage. Concurrent writes are not supported.
- The CSV file must use the current ID-first schema described in [Data Format](data-format.md).
