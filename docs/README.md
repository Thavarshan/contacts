# Documentation

This directory contains the working documentation for the Contacts monorepo. Start here when you
need to understand how to use, maintain, test, or release the Java core, CLI, API, or web dashboard.

## Guides

- [User Guide](user-guide.md): installation, first run, everyday contact workflows, and command examples.
- [CLI Reference](cli-reference.md): command syntax, options, exit codes, and examples.
- [API Reference](api-reference.md): HTTP endpoints, payloads, environment variables, and examples.
- [Data Format](data-format.md): CSV schema, encoding rules, IDs, and import/export behavior.
- [Architecture](architecture.md): layers, packages, design principles, and extension points.
- [Development Guide](development.md): local setup, project layout, coding standards, and change workflow.
- [Testing and Quality](testing-quality.md): unit tests, integration tests, coverage, formatting, PMD, SpotBugs, and Javadocs.
- [CI and Security](ci-security.md): GitHub Actions, CodeQL, permissions, and dependency policy.
- [Deployment](deployment.md): AWS, Terraform, Docker images, ECS, ECR, and release deployment flow.
- [Release Process](release-process.md): versioning, verification, tagging, and GitHub releases.
- [Troubleshooting](troubleshooting.md): common local, CLI, CSV, and CI issues.
- [Repository Settings](repository-settings.md): GitHub settings that cannot be fully represented in source control.

## Decision Records

Architecture decisions are captured in [ADR index](adr/README.md).

## Documentation Rules

- Keep user-facing behavior in `README.md`, `docs/user-guide.md`, and `docs/cli-reference.md` in sync with the Picocli commands.
- Update [Data Format](data-format.md) whenever the CSV header, encoding, or import/export semantics change.
- Add an ADR when a decision changes a core dependency, storage strategy, release policy, or architectural boundary.
- Prefer concise examples that can be copied into a terminal and run against a disposable CSV file.
