# CLI Reference

## Syntax

```text
contacts [--file contacts.csv] [--help] [--version] <command> [options]
```

The root command is implemented by `com.contacts.cli.CliApplication`. The runnable jar entry point
is `com.contacts.Main`.

```bash
java -jar cli/target/contacts-cli-1.0.0-cli.jar --help
```

## Global Options

| Option            | Default        | Description                          |
| ----------------- | -------------- | ------------------------------------ |
| `--file <path>`   | `contacts.csv` | Contacts CSV file to read and write. |
| `-h`, `--help`    | none           | Print generated usage help.          |
| `-V`, `--version` | none           | Print application version.           |

## Exit Codes

| Code | Meaning                                                                              |
| ---- | ------------------------------------------------------------------------------------ |
| `0`  | Command completed successfully.                                                      |
| `1`  | I/O failure, such as an unreadable or unwritable file.                               |
| `2`  | Validation or execution error, such as an unknown contact reference or invalid date. |

## Contact Options

These options are accepted by `add` and `update`.

| Option                    | Repeatable | Description                        |
| ------------------------- | ---------- | ---------------------------------- |
| `--first-name <text>`     | No         | First name.                        |
| `--middle-name <text>`    | No         | Middle name.                       |
| `--last-name <text>`      | No         | Last name.                         |
| `--nick-name <text>`      | No         | Nickname.                          |
| `--company <text>`        | No         | Company or organization.           |
| `--job-title <text>`      | No         | Job title.                         |
| `--department <text>`     | No         | Department or team.                |
| `--phone <text>`          | Yes        | Phone number.                      |
| `--email <text>`          | Yes        | Email address.                     |
| `--url <text>`            | Yes        | URL.                               |
| `--birthday <yyyy-MM-dd>` | No         | Birthday in ISO local-date format. |
| `--social <text>`         | Yes        | Social profile identifier or URL.  |
| `--im <text>`             | Yes        | Instant message handle.            |
| `--notes <text>`          | No         | Free-form notes.                   |

## Commands

### `list`

List all contacts in compact format.

```bash
java -jar cli/target/contacts-cli-1.0.0-cli.jar --file contacts.csv list
```

### `add`

Add a contact and print the generated ID.

```bash
java -jar cli/target/contacts-cli-1.0.0-cli.jar --file contacts.csv add \
  --first-name Jane \
  --last-name Doe \
  --email jane@example.com
```

### `search <query>`

Search contacts using case-insensitive substring matching.

```bash
java -jar cli/target/contacts-cli-1.0.0-cli.jar --file contacts.csv search acme
```

Searchable fields include names, company, job title, department, phone numbers, email addresses,
URLs, addresses, birthday, dates, social profiles, instant message handles, and notes.

### `show <id-or-index>`

Print full details for one contact.

```bash
java -jar cli/target/contacts-cli-1.0.0-cli.jar --file contacts.csv show 1
```

```bash
java -jar cli/target/contacts-cli-1.0.0-cli.jar --file contacts.csv show 11111111-1111-1111-1111-111111111111
```

### `update <id-or-index>`

Update one contact.

```bash
java -jar cli/target/contacts-cli-1.0.0-cli.jar --file contacts.csv update 1 \
  --company Acme \
  --phone +15550100
```

Scalar options are applied only when supplied. Repeatable list options replace their corresponding
list when supplied.

### `delete <id-or-index>`

Delete one contact.

```bash
java -jar cli/target/contacts-cli-1.0.0-cli.jar --file contacts.csv delete 1
```

### `clear`

Delete all contacts from the target CSV file.

```bash
java -jar cli/target/contacts-cli-1.0.0-cli.jar --file contacts.csv clear
```

### `import <path> [--replace]`

Import contacts from another CSV file. Without `--replace`, imported contacts are appended to the
current file.

```bash
java -jar cli/target/contacts-cli-1.0.0-cli.jar --file contacts.csv import import.csv
```

Replace current contacts:

```bash
java -jar cli/target/contacts-cli-1.0.0-cli.jar --file contacts.csv import import.csv --replace
```

### `export <path>`

Export contacts to another CSV file. Parent directories are created when needed.

```bash
java -jar cli/target/contacts-cli-1.0.0-cli.jar --file contacts.csv export backups/contacts.csv
```

## Contact References

Commands that target one contact accept either:

- Stable contact ID, recommended for scripts.
- One-based list index, convenient for interactive terminal use.

When a reference is numeric, the CLI first tries to interpret it as an index after failing to match
an ID. Non-numeric references are treated as IDs only.
