# Data Format

Contacts are stored as CSV using the current ID-first schema. The application does not maintain
backward compatibility for older development schemas.

## Header

The required header is:

```text
id,firstName,middleName,lastName,nickName,company,jobTitle,department,phoneNumbers,emailAddresses,urlAddresses,addresses,birthday,dates,socialProfiles,instantMessageHandles,notes
```

Rows must contain exactly 17 columns. Blank rows are ignored. Header rows are skipped when reading.
Any non-blank data row with the wrong number of columns is rejected.

## Columns

| Column                  | Type   | Description                                                             |
| ----------------------- | ------ | ----------------------------------------------------------------------- |
| `id`                    | String | Stable contact identifier. New contacts receive generated UUID strings. |
| `firstName`             | String | First name.                                                             |
| `middleName`            | String | Middle name.                                                            |
| `lastName`              | String | Last name.                                                              |
| `nickName`              | String | Nickname.                                                               |
| `company`               | String | Organization name.                                                      |
| `jobTitle`              | String | Job title.                                                              |
| `department`            | String | Department or team.                                                     |
| `phoneNumbers`          | List   | Encoded phone number list.                                              |
| `emailAddresses`        | List   | Encoded email address list.                                             |
| `urlAddresses`          | List   | Encoded URL list.                                                       |
| `addresses`             | List   | Encoded address list.                                                   |
| `birthday`              | Date   | ISO local date, `yyyy-MM-dd`.                                           |
| `dates`                 | List   | Encoded ISO local-date list.                                            |
| `socialProfiles`        | List   | Encoded social profile list.                                            |
| `instantMessageHandles` | List   | Encoded instant message handle list.                                    |
| `notes`                 | String | Free-form notes.                                                        |

## Blank Values

Blank cells map to `null` for scalar string and date fields. Blank list cells map to empty lists.

When writing, `null` scalar values are written as blank cells and empty lists are written as blank
cells.

## List Encoding

List fields are stored inside one CSV cell:

1. Each item is URL-encoded using UTF-8.
2. Encoded items are joined with `|`.

This preserves delimiter characters inside values.

Example logical values:

```text
jane@example.com
jane.doe+alerts@example.com
```

Stored cell:

```text
jane%40example.com|jane.doe%2Balerts%40example.com
```

## Address Encoding

Each address is encoded as nested parts:

1. Street, city, state, postal code, and country are converted to blank-safe text.
2. Each part is URL-encoded using UTF-8.
3. Parts are joined with `~`.
4. Multiple encoded addresses are joined with `|`.

Address part order:

```text
street~city~state~postalCode~country
```

## Dates

Dates use `LocalDate` ISO format:

```text
2026-06-27
```

Invalid date strings cause command execution to fail.

## Import and Export Semantics

- `import <path>` reads contacts from the source CSV and appends them to the current contact file.
- `import <path> --replace` replaces the current contact file with imported contacts.
- `export <path>` writes all current contacts to the destination CSV and creates parent directories
  when needed.

IDs are preserved during import and export. Duplicate IDs are not currently deduplicated during
append import.

## Example

See [examples/contacts.sample.csv](../examples/contacts.sample.csv) for a valid sample file.
