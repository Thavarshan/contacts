# Troubleshooting

## `java: command not found`

Install Java 17 or newer and confirm:

```bash
java --version
```

## Maven Wrapper Is Not Executable

On Unix-like systems:

```bash
chmod +x mvnw
```

Then run:

```bash
./mvnw -B -ntp verify
```

## `Unsupported class file major version`

Use Java 17 or newer for both build and runtime. Check:

```bash
java --version
./mvnw --version
```

## CLI Cannot Read or Write the CSV File

The CLI returns exit code `1` for I/O failures. Check:

- The parent directory exists for the main `--file` path.
- The process has read and write permissions.
- The path is not a directory.
- No other process is replacing the file during the command.

The `export` command creates parent directories for the destination path. The main `--file` path
should point to a writable file location.

## `Expected 17 CSV columns but got ...`

The CSV row does not match the current schema. See [Data Format](data-format.md).

Fix by:

1. Exporting from a current version of the app.
2. Regenerating development data.
3. Manually migrating the CSV to include the current header and all 17 columns.

## Invalid Date

Dates must use ISO local-date format:

```text
yyyy-MM-dd
```

Example:

```text
2026-06-27
```

## Search Does Not Find a Contact

Search is case-insensitive substring matching. It does not support regular expressions, fuzzy
matching, ranking, or tokenization.

Try searching a smaller fragment:

```bash
java -jar cli/target/contacts-cli-1.0.0-SNAPSHOT-cli.jar --file contacts.csv search jane
```

## Update Replaced a List Field

Repeatable list options replace the full list when supplied to `update`.

For example, this leaves the contact with exactly one email:

```bash
java -jar cli/target/contacts-cli-1.0.0-SNAPSHOT-cli.jar --file contacts.csv update 1 --email new@example.com
```

To keep multiple values, provide all desired values:

```bash
java -jar cli/target/contacts-cli-1.0.0-SNAPSHOT-cli.jar --file contacts.csv update 1 \
  --email work@example.com \
  --email personal@example.com
```

## Coverage Fails Locally

Open:

```text
target/site/jacoco/index.html
```

Add meaningful tests for changed behavior and rerun:

```bash
./mvnw -B -ntp verify
```

## PMD or SpotBugs Fails

Read the failure in the Maven output and inspect generated reports under `target/`.

Prefer code changes over suppressions. If a suppression is necessary, keep it local and document why
the warning is not actionable.

## GitHub Actions Fails but Local Build Passes

Check:

- The local command exactly matches CI: `./mvnw -B -ntp verify`.
- The local Java version is 17 or newer.
- The failure is OS-specific. CI runs Ubuntu, macOS, and Windows.
- The failing workflow uploaded reports under the Actions artifacts section.
