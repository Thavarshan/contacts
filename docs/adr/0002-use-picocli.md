# ADR 0002: Use Picocli for CLI Parsing

## Status

Accepted

## Context

The first CLI implementation used custom parsing. As commands grew, validation and help output
became cross-cutting concerns.

## Decision

Use Picocli annotations for command routing, options, parameters, usage help, and exit-code
handling.

## Consequences

Benefits:

- Less custom parser code.
- Better generated help.
- Consistent validation and errors.
- Easier command growth.

Tradeoffs:

- Adds a runtime dependency.
- Command classes become tied to Picocli annotations.
