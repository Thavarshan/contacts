# CI and Security

## GitHub Actions

The repository includes these workflows:

| Workflow | File | Purpose |
| --- | --- | --- |
| CI | `.github/workflows/ci.yml` | Runs Maven verification on Ubuntu, macOS, and Windows with Java 17. |
| CodeQL | `.github/workflows/codeql.yml` | Performs Java CodeQL analysis. |
| Pull Request Hygiene | `.github/workflows/pr-title.yml` | Enforces a minimum PR title length. |
| Release | `.github/workflows/release.yml` | Builds and uploads the shaded CLI jar to a GitHub release. |

## CI Workflow

The CI workflow runs on pushes to `main` or `master`, pull requests, and manual dispatch.

It performs:

1. Checkout.
2. Java 17 setup with Maven cache.
3. Maven Wrapper version check.
4. `./mvnw -B -ntp verify`.
5. Upload of Surefire reports.
6. Upload of static-analysis and coverage reports on Ubuntu.
7. Upload of the shaded CLI jar on Ubuntu.

The workflow uses concurrency cancellation so superseded runs on the same ref are cancelled.

## CodeQL

CodeQL runs on pushes, pull requests, a weekly schedule, and manual dispatch. It builds the project
with:

```bash
./mvnw -B -ntp -DskipTests package
```

Then it performs Java analysis through GitHub's CodeQL action.

## Permissions

Workflow permissions are intentionally scoped:

- CI has read access plus check and pull request write permissions for reporting.
- CodeQL has security event write access.
- Release has contents write access so it can create releases and upload artifacts.

## Dependency Policy

Dependabot is intentionally not enabled. Dependency updates are manual and must be validated with:

```bash
./mvnw -B -ntp verify
```

Before merging dependency updates, review:

- Release notes.
- License changes.
- Transitive dependency changes.
- Security advisories.
- Any changes to generated CLI behavior or CSV handling.

## Security Reporting

Sensitive security issues should not be reported through public issues. See [Security Policy](../SECURITY.md).

Recommended GitHub settings are documented in [Repository Settings](repository-settings.md).
