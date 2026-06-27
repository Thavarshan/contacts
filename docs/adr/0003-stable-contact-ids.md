# ADR 0003: Add Stable Contact IDs

## Status

Accepted

## Context

Index-based commands are convenient for humans but fragile for scripts because list order can
change.

## Decision

Every contact has a stable string ID stored in CSV. Commands that operate on one contact accept the
stable ID and also accept one-based list indexes for interactive use.

## Consequences

Benefits:

- Automation can target contacts safely.
- Import/export preserves identity.
- Index compatibility keeps the CLI convenient.

Tradeoffs:

- CSV schema grows by one column.
- Development data using older schemas must be regenerated or migrated deliberately.
