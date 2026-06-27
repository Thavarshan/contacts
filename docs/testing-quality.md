# Testing and Quality

The Maven `verify` lifecycle is the source of truth for local and CI validation.

```bash
./mvnw -B -ntp verify
```

## Test Types

Unit tests cover:

- Domain model behavior.
- Contact service use cases.
- CSV mapper encoding and validation.
- CSV reader and writer behavior.
- CLI helper formatting and search matching.

Integration tests cover:

- Picocli command execution.
- File-backed CLI workflows.
- Add, list, search, show, update, delete, clear, import, and export behavior.

## Useful Commands

Run all tests:

```bash
./mvnw -B -ntp test
```

Run one test class:

```bash
./mvnw -B -ntp -Dtest=CsvContactMapperTest test
```

Run one test method:

```bash
./mvnw -B -ntp -Dtest=CsvContactMapperTest#fromRowRejectsRowsWithUnexpectedColumnCount test
```

Apply formatting:

```bash
./mvnw -B -ntp spotless:apply
```

Check formatting:

```bash
./mvnw -B -ntp spotless:check
```

Run PMD:

```bash
./mvnw -B -ntp pmd:check
```

Run SpotBugs:

```bash
./mvnw -B -ntp spotbugs:check
```

Generate Javadocs:

```bash
./mvnw -B -ntp javadoc:jar
```

## Coverage Gate

JaCoCo runs during `verify` and enforces a minimum bundle line coverage ratio of `0.70`.

Reports are generated under:

```text
target/site/jacoco/
```

When coverage fails:

1. Open `target/site/jacoco/index.html`.
2. Find the changed package or class.
3. Add tests for meaningful behavior, not only for line execution.
4. Re-run `./mvnw -B -ntp verify`.

## Static Analysis

PMD uses the Java quickstart ruleset and fails the build on violations. SpotBugs fails the build on
detected bug patterns.

Use targeted suppressions only when the code is correct and the warning is not actionable. Include a
short justification when suppressing.

## Test Data

Prefer temporary files for file-backed tests. Keep committed sample data in `examples/` small,
readable, and aligned with [Data Format](data-format.md).

## CI Parity

CI runs:

```bash
./mvnw -B -ntp verify
```

Local verification should catch the same build, test, formatting, coverage, PMD, SpotBugs, and
Javadoc failures before a pull request is opened.
