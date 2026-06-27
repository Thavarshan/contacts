# Contributing

Thank you for improving Contacts.

## Development Setup

1. Install Java 17 or newer.
2. Install Node.js 20.18 or newer and npm 10 or newer.
3. Clone the repository.
4. Install root and web dependencies:

```bash
npm install
npm ci --prefix web
```

5. Run the verification suite:

```bash
npm run verify
```

## Code Style

- Java and XML use 4-space indentation enforced by Spotless.
- TypeScript, TSX, CSS, JSON, YAML, and Markdown use 2-space indentation enforced by Prettier.
- Repository policy files live at the root. Do not add nested `.github`, `.husky`, `.editorconfig`,
  `.gitignore`, or Prettier configuration under app folders.
- Run `npm run format` before opening a pull request.
- Keep changes focused and avoid unrelated refactors.
- Add or update tests for behavior changes.

## Pull Requests

Before opening a pull request:

- Run `npm run verify`.
- Update documentation when user-facing CLI, API, or web behavior changes.
- Keep PR descriptions clear: what changed, why, and how it was tested.

## Commit Messages

Use Conventional Commits:

```text
feat(cli): add search command
fix(core): handle blank CSV rows
docs: document Maven build workflow
```

## Dependency Updates

This repository intentionally does not use Dependabot. Dependency updates should be reviewed
manually and validated with `npm run verify`.
