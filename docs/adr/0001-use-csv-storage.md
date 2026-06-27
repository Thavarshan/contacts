# ADR 0001: Use CSV Storage

## Status

Accepted

## Context

The application is a lightweight command-line contacts manager. It should be easy to run locally
without external infrastructure.

## Decision

Store contacts in CSV files and isolate CSV details behind `ContactRepository`, `CsvContactMapper`,
and row-level reader/writer abstractions.

## Consequences

Benefits:

- No database setup is required.
- Files are portable and easy to inspect.
- Tests can use temporary files.

Tradeoffs:

- CSV is not suitable for concurrent writes.
- Schema evolution requires deliberate migration or data regeneration decisions.
- Querying is in-memory.
