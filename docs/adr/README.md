# Architecture Decision Records

ADRs capture decisions that shape the project architecture, dependencies, or operating model.

## Records

- [ADR 0001: Use CSV Storage](0001-use-csv-storage.md)
- [ADR 0002: Use Picocli for CLI Parsing](0002-use-picocli.md)
- [ADR 0003: Add Stable Contact IDs](0003-stable-contact-ids.md)

## When to Add an ADR

Add or update an ADR when a change:

- Introduces or removes a core dependency.
- Changes storage format or persistence behavior.
- Changes CLI parsing strategy or command architecture.
- Changes release, CI, or compatibility policy.
- Creates a tradeoff that future maintainers should understand.

## Template

```markdown
# ADR 0000: Title

## Status

Proposed | Accepted | Superseded

## Context

What problem or constraint forced this decision?

## Decision

What was chosen?

## Consequences

Benefits:

- ...

Tradeoffs:

- ...
```
