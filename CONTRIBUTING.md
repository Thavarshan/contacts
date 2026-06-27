# Contributing

Thank you for improving Contacts CLI.

## Development Setup

1. Install Java 17 or newer.
2. Clone the repository.
3. Run the verification suite:

```bash
./mvnw verify
```

## Code Style

- Java and XML use 4-space indentation enforced by Spotless.
- Run `./mvnw spotless:apply` before opening a pull request.
- Keep changes focused and avoid unrelated refactors.
- Add or update tests for behavior changes.

## Pull Requests

Before opening a pull request:

- Run `./mvnw verify`.
- Update documentation when user-facing CLI behavior changes.
- Keep PR descriptions clear: what changed, why, and how it was tested.

## Commit Messages

Use concise, imperative messages:

```text
Add CLI search command
Fix blank CSV row handling
Document Maven build workflow
```

## Dependency Updates

This repository intentionally does not use Dependabot. Dependency updates should be reviewed manually and validated with `./mvnw verify`.
